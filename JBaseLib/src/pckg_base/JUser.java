package pckg_base;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;





public class JUser {
	public static void ShowText(String[] text_arr) {
		for(String text: text_arr) {
			System.out.println(text + System.lineSeparator());
		}
	}
	
	public static String SowUserMessage(String message) {
		System.out.println("\n\t" + message);
		System.out.println("\n\tPress Enter to continue...");
		
		//	Get the user answer.
		Scanner scanner = new Scanner(System.in);
		String user_msg = scanner.nextLine();
		
		scanner.close();
		return user_msg;
	}

	public static String Folder(String folder)
	{
		String user_folder = null;
		if(null != folder) {
			user_folder = folder;
		}
		else{
			Path currentRelativePath = Paths.get("");
			user_folder = currentRelativePath.toAbsolutePath().toString();
		}
		if(folder.trim().isEmpty() || !Files.isDirectory(Paths.get(folder))){
			user_folder = null;
		}
		return user_folder;
	}
}
