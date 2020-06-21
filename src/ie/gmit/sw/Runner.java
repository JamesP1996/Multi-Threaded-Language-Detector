package ie.gmit.sw;


import java.util.Map;

import java.util.Scanner;

/**
 * <b>Runner Class</b> which works like a Menu and shows the <i>User How to Operate</i> the Program
 * And <i>Manages their Input so that the User May Retrieve
 * The Language of their Query'd file based off a Subject Wili File</i>
 * @author James Porter
 * @version 1.0
 *
 */
public class Runner {

	
	/**
	 * User Is Greeted By Console Asking for The NGram Break Up Number
	 * Which Can be 1,2,3 or 4. 
	 * User Enters this number and program wont run till they enter a valid number between 1 and 4
	 * Asks user to enter the Wili Data Location
	 * and then Parses it into a Subject Database Using Threads
	 * Then Asks user to enter the Query File Location
	 * To Construct The Query Files NGram Map
	 * <b>Finally it Compares the DataSets using Database.GetLanguage()
	 * And Returns the Language that the Code Estimates the Language Is</b>
	 * 
	 * @param args Default Arguments for main Class
	 * @throws Throwable If Issues Occur in Code Throw
	 */
	public static void main(String[] args) throws Throwable {
		//Console Colors
		
		
		String wiliData;
		String queryFile;
		int ngram;
		Scanner sc = new Scanner(System.in);
		
		System.out.println("==================================================");
		System.out.println("*GMIT - Dept. Computer Science & Applied Physics*");
		System.out.println("*Text Language Detector*");
		System.out.println("==================================================");
		System.out.println("Enter N-Gram Breakup Number 1,2,3,4 or 5");
		do {
			ngram = sc.nextInt();
		}
		while(ngram < 1 && ngram > 4);
		System.out.println("Enter Wili Data Location >");
		wiliData = sc.next();
		try {
			Parser parse = new Parser(wiliData, ngram);
			Database db = new Database();
			parse.setDb(db);
			Thread t = new Thread(parse);
			t.start();
			System.out.println("Building Subject Database...please..wait...");
			t.join();
			System.out.println("Enter Query File Location >");
			queryFile = sc.next();
			Map<Integer, LanguageEntry> query = 
					parse.queryFileParse(queryFile,ngram);
			System.out.println("Processing Query...Please...Wait");
			System.out.println("This Text Appears to be written in : "+ db.getLanguage(query));
			System.out.println("If this is not Correct. Re-Run Program and Try with a Different N-Gram Amount");
			System.out.println("Closing ....");
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		
		
		
		
		
		
	
		
		
	}
}
