package pckg_java_build;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.*;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Arrays;

import pckg_base.JText;
import pckg_base.JFiles;
import pckg_base.SubProcess;





public class JavaBuild
{
	private static final String CMD = "cmd";
	private static final String CHANGE_DIR = "cd";

	private static final String JAVA = "java";
	private static final String JAVAC = "javac";
	private static final String JAR = "jar";
	private static final String EOL = System.lineSeparator();


	public static final String EXT_EXPCT = "expct";
	public static final String EXT_INPUT = "input";
	public static final String EXT_REPORT = ".csv";
	
	//private static final String BUILD_DIR = "bin";
	private static final int PROC_TIMOUT_MS = 5000;
	
	
	
	
	private Path build_path = null;
	public String getBuildPath() {
		return this.build_path.toString();
	}
	public String getRelativePath(String file_path) {
		String relative_path = "." + file_path.substring(getBuildPath().length(),file_path.length());
		return relative_path;
	}
	
	
	SubProcess sub_proc = null;
	
	public String AppExpOutput(String file_path) throws FileNotFoundException
	{
		String file_name = JFiles.getNameNoExt(file_path);
		String [] text_lines = JFiles.TextRead(this.build_path.toString(), file_name+ "." + EXT_EXPCT);
		
		return JText.TextStr(text_lines);
	}
	
	/***
	 * 
	 * Class constructors initialize the build path.
	 */
	public JavaBuild(String build_path) {
		this.build_path = Paths.get(build_path);
		return;
	}
	public JavaBuild() {
		this(Paths.get("").toString());
		return;
	}

	
	
	
	public void Init() throws Exception
	{
		this.sub_proc = new SubProcess();
		this.sub_proc.Run(CMD, this.build_path.toString());
	}
	
	public String Command(String exec_inst) throws Exception 
	{
		if(null == this.sub_proc) {
			this.Init();
		}
		String replay = this.sub_proc.Command(exec_inst + EOL);
		return replay;
	}
	
	public String ChangeDir(String build_path) throws Exception 
	{
		String replay = this.Command(CHANGE_DIR + " " + build_path);
		this.build_path = Paths.get(this.build_path.toString(), build_path);
		return replay;
	}
	
	
	public boolean ExecutablkChk(String java_file) throws IOException
	{
		String[] file_text = JFiles.TextRead(this.build_path.toString(), java_file);
		boolean executable = JavaFiles.ExecutablkChk(file_text);
		return executable;
	}	

	


	
	public String BuildClass(String file_path, String destination) throws Exception
	{
		//String file_name = IoFiles.getName(file_path);
		
		String exec_cmd = JAVAC + " " + file_path;
		if(null != destination) {
			exec_cmd += " -d "+ destination; 
		}

		SubProcess sub_proc = new SubProcess();
		String replay = sub_proc.Execute(exec_cmd, this.build_path.toString(), PROC_TIMOUT_MS);

		return replay;	
	}

	public String BuildClass(String file_name) throws Exception
	{
		return BuildClass(file_name, null);
	}

	
	public String BuildJar(String jar_name, String file_path) throws Exception
	{
		String[] folders = JavaFiles.getAllFolders(file_path);
		for(int i=0; i<folders.length;i++) {
			folders[i] = "./" +folders[i] +"/*.class"; 
		}
		String folders_text = JText.TextStr(folders, " ");
		
		String exec_cmd = JAR + " cf " + jar_name + ".jar" + " " + folders_text;

		SubProcess sub_proc = new SubProcess();
		String replay = sub_proc.Execute(exec_cmd, this.build_path.toString(), PROC_TIMOUT_MS);
		return replay;	
	}

	
	
	
	//	Run a class within current process.
	public String Run(String file_name) throws Exception
	{
		String code_name = file_name + ".java";
		String replay = this.Command(JAVA + " " + code_name);
		return replay;
	}
	
	//	Execute a class file in a separate process..
	private String Execute(String java_file, String[] input_file, int milisec) throws Exception
	{
		String class_name = JFiles.getNameNoExt(java_file);
		class_name = class_name.trim().replace("."+File.separator,"").replace(File.separator,".");
		String exec_cmd = JAVA + " " + class_name;
		
		SubProcess sub_proc = new SubProcess();
		String output = sub_proc.Execute(exec_cmd, this.build_path.toString(), input_file, milisec);
		
		return output;
	}
	public String Execute(String java_file) throws Exception
	{
		String input_file = JFiles.setNameExt(java_file, EXT_INPUT);
		String[] input = JFiles.TextRead(this.build_path.toString(), input_file);
		return Execute(java_file, input, PROC_TIMOUT_MS);
	}

	
	
	private String ExecuteJar(String project, String java_file, String[] input_file, int milisec) throws Exception
	{
		String class_name = JFiles.getNameNoExt(java_file);
		class_name = class_name.trim().replace("."+File.separator,"").replace(File.separator,".");
		String exec_cmd = JAVA + " -cp " + project +".jar " + class_name;
		
		SubProcess sub_proc = new SubProcess();
		String output = sub_proc.Execute(exec_cmd, this.build_path.toString(), input_file, milisec);
		
		return output;
	}
	
	public String ExecuteJar(String project, String java_file) throws Exception
	{
		String input_file = JFiles.setNameExt(java_file, EXT_INPUT);
		String[] input = JFiles.TextRead(this.build_path.toString(), input_file);
		return ExecuteJar(project, java_file, input, PROC_TIMOUT_MS);
	}

	
	
	
	// Clean a folder with specific file name. 
	public static String  DeleteFile(String destination, String file_path) throws Exception
	{
		String file_name = new File(file_path).getName();
		file_name = file_name.substring(0, file_name.lastIndexOf("."));
	
		String[] clean_class = JavaFiles.getFilesFind(destination, file_name, JavaFiles.EXT_CLASS);
		JavaFiles.DeleteFiles(clean_class);
	
		return JText.TextStr(clean_class );
	}
	public String CleanFile(String file_path) throws Exception
	{
		return DeleteFile(this.build_path.toString(), file_path);
	}

	private static String  DeleteJar(String project, String destination) throws Exception
	{
		String[] clean_jars = JavaFiles.getFilesFind(destination, project, JavaFiles.EXT_JAR);
		JavaFiles.DeleteFiles(clean_jars);
		
		return JText.TextStr(clean_jars);
	}
	public String CleanJar(String project) throws Exception
	{
		return DeleteJar(project, this.build_path.toString());
	}

}
