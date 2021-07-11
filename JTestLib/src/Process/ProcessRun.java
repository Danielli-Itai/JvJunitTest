package Process;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;





public class ProcessRun
{
	//	Process handler;
	private Process proc = null;
	
	//	Standard IO readers and writers.
	private BufferedReader stdInput = null;
	private BufferedReader stdError = null;
	private BufferedWriter stdOut = null;

	
	
	//	Initiate a new process.
	private Boolean  ProcNew(String[] proc_command) throws IOException
	{
		//	Run the sub process.
		Runtime rt = Runtime.getRuntime();
		this.proc = rt.exec(proc_command);
		
		//	Create standard IO readers/writer.
		this.stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
		this.stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
		this.stdOut = new BufferedWriter( new OutputStreamWriter(proc.getOutputStream()));
		return(true);
	}
	
	
	//	Read the sub-process output stream.
	private String OutputGet() throws IOException
	{
		String std_out = "";
		char cbuf[] = new char[1024];
		
		//	While output stream not empty.
		while(this.stdInput.ready())
		{
			//	Read the stream.
			StringBuffer sb = new StringBuffer();
			int length = this.stdInput.read(cbuf, 0, 1024);
			//	Add the buffer charcters to the string buffer.
			for(int i = 0; i< length; i++) {
				sb.append(cbuf[i]);
			}
			//	Add the characters as a  string.
			std_out += sb.toString();
		}
		return std_out;
	}
	
	//	Get the sub process error standard output.
	public String ErrorGet() throws IOException
	{
		String std_error = "";
		if(this.stdError.ready()) {
			std_error = this.stdError.readLine();
		}
		return std_error;
	}

	

	
	
	/**	Execute command line application.
	*	provide executable and command line parameters as an array of strings.
	*	To print all directory files provide : {"dir", "c:\\"};
	*/
	public String Execute(String[] proc_command) throws Exception
	{
		String std_out = "";
		String std_error = "";
		
		this.ProcNew(proc_command);
		
		// Read the output from the command
		String out = null;
		while ((out = this.stdInput.readLine()) != null) {
			std_out += out;
		}

		// Read any errors from the attempted command.
		String error = null;
		while ((error = this.stdError.readLine()) != null) {
			std_error += error;
		}
		if(!std_error.isEmpty()) {
			throw new Exception(std_error);
		}

		return(std_out);
	}
	

	/**	Execute a sub-process application.
	*	provide executable and command line parameters as an array of strings.
	*	To print all directory files provide : {"dir", "c:\\"};
	*/	
	public String Run(String[] proc_command) throws Exception
	{	
		//	Run the sub process.
		this.ProcNew(proc_command);
		//	Waite for sub process output.
		while(!this.stdInput.ready());
		
		// Read the output from the sub-process.
		String std_out = this.OutputGet();
		std_out = std_out.trim();
		
		// Read any errors from the attempted command.
		String error = this.ErrorGet();
		if(!error.isEmpty()) {
			throw new Exception(error);
		}
		
		return(std_out);
	}
	
	
	//	Write command string to the sub process and return it's response.
	public String Command(String command) throws Exception
	{	
		//	Write to standard output.
		this.stdOut.write(command + System.lineSeparator());
		this.stdOut.flush();
		
		//	Wait for sub process response.
		while(!this.stdInput.ready());
		
		// Read the output from the command
		String std_in = this.OutputGet();

		//	Get sub process standard error.
		String error = this.ErrorGet();
		if(!error.isEmpty()) {
			//	Throw exception in case of error.
			throw new Exception(error);
		}
		return(std_in);
	}

	
	public String Exit() throws Exception
	{
		this.stdOut.write("exit()");
		this.stdOut.flush();

		// Read the output from the command
		String std_out = this.OutputGet();
		
		//	Get sub process standard error.
		String error = this.ErrorGet();
		if(!error.isEmpty()) {
			//	Throw exception in case of error.
			throw new Exception(error);
		}
		return(std_out);
	}
}