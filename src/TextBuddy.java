/* This program uses a command line interface which involves a class TextBuddy.
 * This class TextBuddy prompts the user for commands to either 'add', 'delete', 'display',
 * or 'clear' content on a text file. The text file is also created by the program with 
 * the user defining the name of the text file. In addition to the 4 functions stated,
 * an 'exit' command is used to close the program.
 */


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class TextBuddy {

	private static boolean isValidCommand = true;
	
	public static void main(String[] args) throws IOException {

		Scanner sc = new Scanner(System.in);
		String fileName = args[0];
		System.out.println("Welcome to TextBuddy. " + fileName + " is ready for use");
		
		File file = new File(fileName);
		
		//This logic is to create the file if the file is not already present
		if (!file.exists()) {
			file.createNewFile();
		}

		isValidCommand = executeCommand(sc, fileName, file, isValidCommand);
	
		sc.close();
	}

	private static boolean executeCommand(Scanner sc, String fileName, File file, boolean isValidCommand)
			throws IOException, FileNotFoundException {
		while (isValidCommand) {
			System.out.print("command: ");
			String commandType = sc.next();

			// Adding the content to the file	
			if (commandType.equalsIgnoreCase("add")) {
				String commandContent = sc.nextLine();
				addText(file, commandContent);
					
				// Printing confirmation statement
				System.out.println("added to " + fileName + ": " + (char)34 + commandContent.trim() + (char)34);
			}
			
			// Displaying all the content in the file	
			else if (commandType.equalsIgnoreCase("display")) {	
				displayText(fileName, file);
			}
			
			// Deleting a specific line of text from the file
			else if (commandType.equalsIgnoreCase("delete")) {
				int lineIndex = sc.nextInt();
				String textToDelete = deleteSpecificText(file, lineIndex);
				System.out.println("deleted from " + fileName + ": " + (char)34 + textToDelete + (char)34);
			}
			
			// Clearing the entire file of content
			else if (commandType.equalsIgnoreCase("clear")) {
				clearTextFile(fileName, file);
			}

			// Exiting the program
			else if (commandType.equalsIgnoreCase("exit")) {
				isValidCommand = false;
			}

			// Error statement if invalid command type is entered
			else {
				System.out.println("Invalid command type entered! Try again!");
			}

		}
		return isValidCommand;
	}

	private static String deleteSpecificText(File file, int lineIndex) throws IOException, FileNotFoundException {
		int numLines = lineCounter(file);
		ArrayList<String> textArray = new ArrayList<String>();
		BufferedReader br = readFromFile(file);
		String textLine;
		
		String textToDelete = fileToArraylist(lineIndex, numLines, textArray, br);
		
		PrintWriter pw = new PrintWriter(file);
		pw.write("");
		pw.close();
		
		arraylistToFile(file, textArray);
		return textToDelete;
	}

	private static void arraylistToFile(File file, ArrayList<String> textArray) throws IOException {
		for (int k=0; k<textArray.size(); k++) {
			addText(file, textArray.get(k));
		}
	}

	private static String fileToArraylist(int lineIndex, int numLines, ArrayList<String> textArray, BufferedReader br)
			throws IOException {
		String textLine;
		for (int i=1; i<lineIndex; i++) {
			textLine = br.readLine();
			textArray.add(textLine);
		}
		
		// To skip the line being deleted
		textLine = br.readLine();
		String textToDelete = textLine;
		
		for (int j=lineIndex+1; j<=numLines; j++) {
			textLine = br.readLine();
			textArray.add(textLine);
		}
		return textToDelete;
	}

	private static BufferedReader readFromFile(File file) throws FileNotFoundException {
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		return br;
	}

	private static void clearTextFile(String fileName, File file) throws FileNotFoundException {
		PrintWriter pw = new PrintWriter(file);
		pw.write("");
		pw.close();
		System.out.println("all content deleted from " + fileName);
	}

	private static void displayText(String fileName, File file) throws IOException, FileNotFoundException {
		int numLines = lineCounter(file);
		BufferedReader br = readFromFile(file);
		String textLine = br.readLine();
		
		if (textLine == null){
			System.out.println(fileName + " is empty");
		}
		
		else {
			for (int i=1; i<=numLines; i++) {
				System.out.println(i + ". " + textLine);
				textLine = br.readLine();
			}
		}
		br.close();
	}

	private static void addText(File file, String commandContent) throws IOException {
		PrintWriter pw = writeToFile(file);
		pw.println(commandContent.trim());
		pw.close();
	}

	private static PrintWriter writeToFile(File file) throws IOException {
		FileWriter fw = new FileWriter(file, true);
		BufferedWriter bw = new BufferedWriter(fw);
		PrintWriter pw = new PrintWriter(bw);
		return pw;
	}

	//Method to count number of lines already existing in text file
	private static int lineCounter(File file) throws IOException {
		BufferedReader br = readFromFile(file);
		
		int lineCount = 0;
		
		while (br.readLine() != null) {
			lineCount++;
		}
		br.close();
		return lineCount;
	}
	
}
