package pckg_java_run;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import pckg_base.JFiles;
import pckg_java_build.JavaBuild;
import pckg_java_build.JavaFiles;
import pckg_java_build.JavaProjects;





public class DevelopTestRun {
	
	public static String[] WorkspaceRun(String ws_path) {
		ArrayList<String> ws_results = new ArrayList<String>();

		try {
			File[] ws_projects = JavaFiles.getFolders(ws_path, ".");
			Arrays.sort(ws_projects);
			
			for (File project : ws_projects) {				
				JavaProjects workspace_run = new JavaProjects(project.getPath());
				try {
					String[] results = workspace_run.ProjectRun(project.getName());
					ws_results.addAll(Arrays.asList(results));
				} catch (Exception e) {
					System.out.println("Project failed!");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ws_results.toArray(new String[0]);
	}

	public static void TestReport(String WORKSPACE_DIR, String headers, String[] results) {
		System.out.println("***   Test Report   ***");
		for(String result: results) {
			System.out.println(result);
		}
		System.out.println("***   Test Completed   ***");							

		String file_name = new File(WORKSPACE_DIR).getName() + JavaBuild.EXT_REPORT;
		file_name = new File(WORKSPACE_DIR, file_name).toString();
		try {
			JFiles.TextSave(file_name, headers, results);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
