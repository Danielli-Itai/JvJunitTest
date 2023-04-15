package pckg_app_main;





import java.time.Month;

import pckg_base.JTime;
import pckg_base.JUser;
import pkg_java_run.SubmissionRun;





public class SupervisorApp {
	private static String RUN_SUBMISSION = "run_submission";
	
	private static final String[] TEXT_COMMAND_ERROR =  {
			"Code tester by Itai Danielli"
		,	"You should provide the following arguments:"
		,	"1. \"run_submission\" to run on local folder"
		,	"2. The path to your the workspace"				};

	
	public static void main(String[] args) {
		System.out.println("\n\tSuperviser Code Tester - Itai Danielli");
			
		long months = JTime.DateTimeDuration(2023,Month.MARCH, 1);
		if(months>4) {
			JUser.SowUserMessage("New version required contact - Danielli.Itai@gmail.com.");
			return;
		}

		if(args.length > 0)
		{
			if(SubmissionRun.IsCommand(args[0])) 
			{
				String command = args[0];
				String submissions_dir = args.length>1 ?JUser.Folder(args[1]):JUser.Folder(null);
				if(null != submissions_dir)
				{		
					try {
						String[] results = null;
						if (RUN_SUBMISSION.equals(command)) {
							results = SubmissionRun.SubmissionRun(submissions_dir);
						}
						SubmissionRun.TestReport(submissions_dir, results);
					}catch(Exception e) {
						System.out.println("Project test failed!");
					}
				}
				else {
					JUser.SowUserMessage("Path does not exists: " + submissions_dir);
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
