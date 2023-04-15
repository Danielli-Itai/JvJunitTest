package pkg_java_run;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;

import pckg_base.JFiles;
import pckg_base.JTime;
import pckg_base.JUser;
import pckg_java_build.JavaFiles;
import pckg_java_build.JavaProjects;
import pckg_java_run.DevelopTestRun;





public class SubmissionRun {

	private static final String RUN_SUBMISSION = "run_submission";
	private static final String RES_HEADERS = "Developer" + JavaProjects.SEP + JavaProjects.RES_HEADERS;
	

	public static String[] SubmissionRun(String dev_path) {
		ArrayList<String> dev_results = new ArrayList<String>();

		try {
			File[] sumission_folder = JavaFiles.getFolders(dev_path, ".");
			Arrays.sort(sumission_folder);
			
			for (File developer : sumission_folder) {
				String dev_name = JFiles.getName(developer.getPath());
				try {
					String[] results = DevelopTestRun.WorkspaceRun(developer.getPath());
					for(int i=0; i< results.length; i++) {
						results[i] = dev_name + JavaProjects.SEP + results[i]; 
					}

					dev_results.addAll(Arrays.asList(results));
				} catch (Exception e) {
					System.out.println("Project failed!");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return dev_results.toArray(new String[0]);
	}

	public static void TestReport(String folder, String[] results) {
		DevelopTestRun.TestReport(folder, RES_HEADERS, results);
	}
	public static boolean IsCommand(String command) {
		return (RUN_SUBMISSION.equals(command));
	}
	
}
