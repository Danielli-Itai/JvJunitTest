package pckg_app_main;





import java.io.File;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;

import pckg_base.JTime;
import pckg_base.JUser;
import pckg_java_build.JavaProjects;
import pckg_java_run.DevelopTestRun;




public class DeveloperApp {
	private static String RUN_ROJECT = "run_project";
	private static String RUN_WORKSPACE = "run_workspace";
	
	private static String[] TEXT_COMMAND_ERROR =  {
				"Code tester by Itai Danielli"
			,	"You should provide the following arguments:"
			,	"1.\"auto_run\" to run on local folder"
			,	"2.The path to your the workspace"				};

	
	
	
	public static boolean IsCommand(String command) {
		return (RUN_ROJECT.equals(command) || RUN_WORKSPACE.equals(command));
	}
	
	
	public static void main(String[] args) {
		System.out.println("\n\tDeveloper Code Tester - Itai Danielli");
		
		long months = JTime.DateTimeDuration(2023,Month.MARCH, 1);
		if(months>4) {
			JUser.SowUserMessage("New version required contact - Danielli.Itai@gmail.com.");
			return;
		}

		
		if(args.length > 0)
		{
			if(IsCommand(args[0])) {
				String WORKSPACE_DIR = args.length>1 ?JUser.Folder(args[1]):JUser.Folder(null);
				if(null != WORKSPACE_DIR)
				{		
					try {
						String[] results = null;
						if(RUN_ROJECT.equals(args[0])) {
							JavaProjects workspace_run = new JavaProjects(WORKSPACE_DIR);
							results = workspace_run.ProjectRun(new File(WORKSPACE_DIR).getName()); 
						}
						else if (RUN_WORKSPACE.equals(args[0])) {
							results = DevelopTestRun.WorkspaceRun(WORKSPACE_DIR);
						}
						DevelopTestRun.TestReport(WORKSPACE_DIR, JavaProjects.RES_HEADERS,results);
					}catch(Exception e) {
						System.out.println("Project test failed!");
					}
				}
				else {
					JUser.SowUserMessage("Path does not exists: " + WORKSPACE_DIR);
				}

			}
			else{
				JUser.ShowText(TEXT_COMMAND_ERROR);
				JUser.SowUserMessage("Press Enter.");
			}
		}
		return;
	}
}
