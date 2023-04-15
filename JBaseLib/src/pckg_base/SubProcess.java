package pckg_base;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;





public class SubProcess
{
	//	Time to sleep between commands for animation.
	private final int SLEEP_TIME = 1000;
	
	//	Process handler;
	protected Process sub_proc = null;
	
	
	
	//	Standard IO readers and writers.
//	private BufferedReader stdInput = null;
//	private BufferedReader stdError = null;
//	private BufferedWriter stdOut = null;

	private InputStreamReader stdInput = null;
	private InputStreamReader stdError = null;
	private OutputStreamWriter stdOut = null;

	public SubProcess() {
		
	}
	
	//	Initiate a new process.
	@SuppressWarnings("deprecation")
	private Boolean  ProcNew(String proc_command, String work_dir) throws IOException
	{
		//	Write to console view.
		System.out.println(String.join(" ", proc_command));

		//	Run the sub process.
		Runtime rt = Runtime.getRuntime();
		String[] command = proc_command.split(" ");
		if(null != work_dir) {
			this.sub_proc = rt.exec(command, null, new File(work_dir));
		}else {
			this.sub_proc = rt.exec(command);			
		}
		
		//	Create standard IO readers/writer.
		this.stdInput = new InputStreamReader(sub_proc.getInputStream());
		this.stdError = new InputStreamReader(sub_proc.getErrorStream());
		this.stdOut =  new OutputStreamWriter(sub_proc.getOutputStream());
		
		return(true);
	}
	
	@Override
	protected void finalize() {
	    try {
			this.sub_proc.waitFor();
			
//			this.stdInput.close();
//			this.stdError.close();
//			this.stdOut.close();
	    } catch (Exception e) {
	        // ...
	    }
	}
	
	private Boolean  ProcNew(String proc_command) throws IOException
	{
		return this.ProcNew(proc_command, null); 
	}
	
	//	Read the sub-process output stream.
	private static String StdInputGet(InputStreamReader stdInput) throws IOException
	{
		String std_out = "";
		char cbuf[] = new char[1024];
		
		
		//	While output stream not empty.
		while(stdInput.ready())
		{
			//	Read the stream.
			StringBuffer sb = new StringBuffer();
			int length = stdInput.read(cbuf, 0, 1024);
			//	Add the buffer characters to the string buffer.
			for(int i = 0; i< length; i++) {
				sb.append(cbuf[i]);
			}
			//	Add the characters as a  string.
			std_out += sb.toString();
		}
		return std_out;
	}
	
	//	Get the sub process error standard output.
//	public String ErrorGet() throws IOException
//	{
//		String std_error = "";
//		if(this.stdError.ready()) {
//			std_error = this.stdError.readLine();
//		}
//		return std_error;
//	}

	

	
	
	/**	Execute command line application.
	*	provide executable and command line parameters as an array of strings.
	*	To print all directory files provide : {"dir", "c:\\"};
	*/
	static final int BUFF_SIZE = 100;
	public String Execute(String proc_command, String work_dir, String[] input, int timeout) throws Exception
	{
		String std_input = "";
		String std_error = "";
				
		Duration timeElapsed = null;
		Instant start = Instant.now();
		
		//	Create new process.
		this.ProcNew(proc_command, work_dir);
		
		int write_line = 0;
		do
		{
			Thread.sleep(SLEEP_TIME);
			
			// Read the output from the command
			std_input += StdInputGet(this.stdInput);
			
			if(null != input) {
				if(write_line < input.length){
					this.stdOut.write(input[write_line],0, input[write_line].length());
					this.stdOut.flush();
					write_line++;
				}
			}
			
			// Read any errors from the attempted command.
			std_error += StdInputGet(this.stdError);
			
			Instant end = Instant.now();
			timeElapsed = Duration.between(start, end);
			
		}while(this.sub_proc.isAlive() && (timeElapsed.toMillis() < timeout));
		std_input += StdInputGet(this.stdInput);
		
		if(!std_error.isEmpty()) {
			System.out.println(std_error);
			throw new Exception(std_error);
		}
		
		//	Write to console view.
		System.out.println(std_input);
		return(std_input);
	}
	public String Execute(String proc_command, String work_dir, int milisec) throws Exception
	{
		return Execute(proc_command, work_dir, null, milisec);
	}

	public String Execute(String proc_command, int milisec) throws Exception
	{
		return Execute(proc_command, null, null, milisec);
	}

	/**	Execute a sub-process application.
	*	provide executable and command line parameters as an array of strings.
	*	To print all directory files provide : {"dir", "c:\\"};
	*/	
	public String Run(String proc_command, String work_dir) throws Exception
	{	
		//	Run the sub process.
		this.ProcNew(proc_command, work_dir);
		//	Wait for sub process output.
		while(!this.stdInput.ready());
		
		// Read the output from the sub-process.
		String std_in = StdInputGet(this.stdInput);
		std_in = std_in.trim();
		
		// Read any errors from the attempted command.
		String error = StdInputGet(this.stdError);
		if(!error.isEmpty()) {
			throw new Exception(error);
		}
		
		System.out.println(std_in);
		return(std_in);
	}
	public String Run(String proc_command) throws Exception
	{
		return Run(proc_command, null);
	}
	
	//	Write command string to the sub process and return it's response.
	public String Command(String command) throws Exception
	{	
		//	Write to console view.
		//System.out.println(command);
		
		
		//	Write to standard output.
		this.stdOut.write(command + System.lineSeparator());
		this.stdOut.flush();
		
		//	Wait for sub process response.
		while(!this.stdInput.ready());
		
		// Read the output from the command
		String std_in = StdInputGet(this.stdInput);

		//	Get sub process standard error.
		String error = StdInputGet(this.stdError);;
		if(!error.isEmpty()) {
			//	Throw exception in case of error.
			throw new Exception(error);
		}
		
		//	Write to console view.		
		System.out.println(std_in);
		return(std_in);
	}

	
	public String Exit() throws Exception
	{
		this.stdOut.write("exit()");
		this.stdOut.flush();

		// Read the output from the command
		String std_out = StdInputGet(this.stdInput);
		
		//	Get sub process standard error.
		String error = StdInputGet(this.stdError);
		if(!error.isEmpty()) {
			//	Throw exception in case of error.
			throw new Exception(error);
		}
		return(std_out);
	}
	
	protected void SetWorkingDir(String curr_dir) {
		System.setProperty("user.dir", curr_dir);
	}
}