package hello_world;



import java.util.Scanner;



public class YourName
{
	public static void main(String[] args) {

		// Ask for the user name.
		System.out.println("What is your name ?");
		Scanner scanner = new Scanner(System.in);
		
		//	Get the user answer.
		String name = scanner.nextLine();
		System.out.println("Nice to meet you " + name);
		
		//	Close the scanner.
		scanner.close();
		
		return;
	}
}
