package pckg_java_build;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import pckg_base.JText;
import pckg_base.JFiles;
import pckg_base.JException;

public class JavaProjects {

	private String workspace_dir = null;
	
	private static String ERROR = "Error: ";
	//private static final String TERM = System.lineSeparator();
	private static final String RUN_OK = "Ok Run app: ";
	private static final String TEST_OK = "Ok Test IO Match: ";
	private static final String RUN_EROR = "Error Run Fail ";
	private static final String TEST_ERROR = "Error Test IO Fail: ";
	

	public static final String SEP = ",";
	public static final String RES_HEADERS = "Result, Project, File, Workspace, Project_folder";
	private String Result(String result, String project, String file_path, String workspace_dir, String project_folder) {
		String task_name = JFiles.getName(file_path); 
		String result_info = result + SEP + task_name + SEP + file_path +  SEP  + " at: " + new File(this.workspace_dir, project_folder).toString();
		return result_info;
	}

	private String TestResult(String result, String project, String file, String workspace_dir, String project_folder) {
		String result_info = Result(result, project, file, workspace_dir, project_folder);
		return result_info;
	}
	private String TestError(String result, String project, String file, String workspace_dir, String project_folder, String info) {
		String result_info = TestResult(result, project, file, workspace_dir, project_folder) + info;
		return result_info;
	}

	

	public JavaProjects(String workspace_dir) {
		this.workspace_dir = workspace_dir;
	}

	public String BuildJar(String project, String source_folder, String[] file_names) throws Exception {
		JavaBuild java_build = new JavaBuild(this.workspace_dir);
		java_build.ChangeDir(source_folder);

		String result = "";
		for (String file : file_names) {
			result += java_build.CleanFile(file);
		}

		String file = JText.TextStr(file_names, " ");
		result += java_build.BuildClass(file);

		result += java_build.BuildJar(project, java_build.getBuildPath());
		return result;
	}

	private String TestRun(String project, String project_folder, String file) throws Exception {
		String result = "";
		//String result_info = "\"" + file + "\" at: \"" + new File(this.workspace_dir, project_folder).toString() + "\"";

		JavaBuild java_build = new JavaBuild(this.workspace_dir);

		try {
			java_build.ChangeDir(project_folder);
			file = java_build.getRelativePath(file);
			if (java_build.ExecutablkChk(file)) {
				
				String output_txt = null;
				if(null != project) {
					output_txt = java_build.ExecuteJar(project, file);
				}else {
					output_txt = java_build.Execute(file);
				}
				
				String expected = java_build.AppExpOutput(file);
				if (!expected.trim().isEmpty()) {
					if (JText.EQUAL.equals(JText.Compare(expected, output_txt))) {
						result = TestResult(TEST_OK, project, file, workspace_dir, project_folder);
					} else {
						result =  TestResult(TEST_ERROR , project, file, workspace_dir, project_folder);
					}
				} else {
					result = TestResult(RUN_OK , project, file, workspace_dir, project_folder);
				}
				java_build.CleanFile(file);
			}
		} catch (Exception e) {
			JException exp = new JException(e, file);
			result = TestError(RUN_EROR , project, file, workspace_dir, project_folder, exp.getInfo());
			throw exp;
		}
		return result;
	}

	public String TestFile(String project_folder, String file) throws Exception {
		return TestRun(null, project_folder, file);
	}
	public String TestJar(String project, String project_folder, String file) throws Exception {
		return TestRun(project, project_folder, file);
	}
	

	public ArrayList<String> SourceBuild(String project, String source_folder) throws Exception {
		ArrayList<String> results = new ArrayList<String>();

		String project_path = new File(this.workspace_dir, source_folder).toString();
		String[] code_files = JavaFiles.getFilesFind(project_path, JavaFiles.EXT_JAVA);

		if (code_files.length > 0) {
			String[] file_names = new String[code_files.length];
			for (int i = 0; i < code_files.length; i++) {
				file_names[i] = JFiles.getRelativePath(project_path, code_files[i]);
			}

			String result = BuildJar(project, source_folder, file_names);
		}

		return results;
	}

	public String CleanJar(String project, String project_folder) {
		String result = "Clean " + project + ":";
		JavaBuild java_build = new JavaBuild(this.workspace_dir);
		try {
			java_build.ChangeDir(project_folder);
			result += java_build.CleanJar(project);
		} catch (Exception e) {
			result += " " + ERROR + e.getMessage().split(System.lineSeparator(), 2)[0] + "\" at: \"" + this.workspace_dir + "\"";
			e.printStackTrace();
		}

		return result;
	}

	public ArrayList<String> SourceRun(String project, String source_folder) {
		ArrayList<String> results = new ArrayList<String>();

		String project_path = new File(this.workspace_dir, source_folder).toString();

		String[] code_files;
		code_files = JavaFiles.getFilesFind(project_path, JavaFiles.EXT_JAVA);
		for (String file : code_files) {
			try {
				String result = TestRun(project, source_folder, file);
				if(!result.isEmpty()) {		//	None executable Java file returns empty result.
					results.add(result);
				}
			} catch (Exception e) {
				String result = "File run error: " + e.getMessage().split(System.lineSeparator(), 2)[0] + "\" at: \"" + this.workspace_dir + "\"";
				results.add(result);
			}
		}

		CleanJar(project, source_folder);
		return results;
	}

	public String[] ProjectRun(String project) {
		ArrayList<String> ws_results = new ArrayList<String>();

		String[] project_folders = JavaFiles.getFoldersNames(this.workspace_dir, ".");
		
		for (String folder : project_folders) {
			if(JFiles.filetypeExistsRec(JFiles.getFilePath(this.workspace_dir,folder), JavaFiles.EXT_JAVA)) {
				try {
					SourceBuild(project, folder);
					ArrayList<String> run_res = SourceRun(project, folder);
					ws_results.addAll(run_res);
				} catch (Exception e) {
					String result = "Code build error: " + e.getMessage().split(System.lineSeparator(), 2)[0];
					ws_results.add(result);						
				}
			}
		}

		return ws_results.toArray(new String[0]);
	}
	
	public String ProjectClean(String project_folder) throws Exception {
		ArrayList<String> results = new ArrayList<String>();

		String project_path = new File(this.workspace_dir, project_folder).toString();
		String[] class_files = JavaFiles.getFilesFind(project_path, JavaFiles.EXT_CLASS);
		for (String file : class_files) {
			file = JFiles.getRelativePath(project_path, file);

			String deleted_files = JavaBuild.DeleteFile(project_path, file);

			results.add(deleted_files);
			System.out.println("Deleted files : " + deleted_files);
		}

		return JText.TextStr(results.toArray(new String[0]));
	}
}
