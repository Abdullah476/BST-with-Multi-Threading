import java.util.Vector;
import java.util.StringTokenizer;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class MainClass {
	
	static public VocabBST vocabTree = new VocabBST();
	static public Vector<Word> words = new Vector<Word>();
	static public int numberOfInputFiles;
	static public ArrayList<String> fileNames = new ArrayList<>();
	static public Vector<Vector<String>> inputFiles;
	
	static void WordsCreation(String word) {
		//Check if the word is present in the BST
		if(vocabTree.search(word) == true) {
			boolean status = false;
			for(int i = 0; i < words.size(); i++) {
				if(words.get(i).getWord().compareTo(word) == 0) {		
					//if word found in the vector list update its frequency
					words.get(i).updateFrequency();
					status = true;
				}
			}
			if(status == false) {
				//if word not found in the vector list create a new object of a word and add it to the vector list
				Word tempObject = new Word(word);
				words.addElement(tempObject);
			}
		}
		//if the word is not present in the BST do nothing
	}

	static void WordsMatching() {		
		String[] tempString = new String[numberOfInputFiles];
		for(int i = 0; i < numberOfInputFiles; i++) {
			tempString[i] = inputFiles.get(i).get(1);
		}
		
		for(int i = 0; i < numberOfInputFiles; i++) {
			StringTokenizer st = new StringTokenizer(tempString[i], " ");
			while(st.hasMoreElements()) {
				WordsCreation(st.nextToken());
			}
		}
		
	}
	
	static void filesChecking(String[] args)throws FilesNotFoundException {
		String temp = "vocabulary.txt";
		boolean status = false;
		for(int i = 0; i < args.length; i++) {
			if(args[i].compareTo(temp) == 0) {
				status = true;
			}
			else {
				File f = new File(args[i]);
				if(f.exists()) {
					fileNames.add(args[i]);
				}
				else {
					throw new FilesNotFoundException(args[i] + " not found!");
				}
			}
		}
		if(status == true) {
			fileNames.add(temp);
		}
		else {
			throw new FilesNotFoundException(temp + " not found!");
		}
	}
	
	static void displayBST() {
		System.out.println();
		System.out.println("Displaying BST of Vocabulary File in Pre-Order");
		vocabTree.display();
		System.out.println();
	}
		
	static void displayVectors() {
		System.out.println();
		System.out.println("Displaying Vectors build from Input Files");
		for(int i = 0; i < inputFiles.size(); i++) {
			System.out.println(inputFiles.get(i));
		}
		System.out.println();
	}
	
	static void displayMatchingWords() {
		System.out.println();
		System.out.println("Displaying Match Words and their frequencies");
		for(int i = 0; i < words.size(); i++) {
			System.out.println("Word: " + words.get(i).getWord() + "\tFrequency: " + words.get(i).getFrequency());
		}
		System.out.println();
	}
	
	static void searchQuery(String query) {
		System.out.println("Results of Query: ");
		boolean status = true;
		//Searching query in vocabulary tree
		if(vocabTree.search(query) == true) {
			System.out.println("File: vocabulary.txt\tFrequency: 1");
			status = false;
		}
		
		//Searching query in inputFile vectors and displaying results with their frequency
		for(int i = 0; i < inputFiles.size(); i++) {
			int tempCounter = 0;
			String temp = inputFiles.get(i).get(1);
			StringTokenizer t = new StringTokenizer(temp, " ");
			while(t.hasMoreElements()) {
				if(t.nextToken().compareTo(query) == 0) {
					tempCounter++;
					status = false;
					
				}
			}
			if(tempCounter != 0) {
				System.out.println("File: " + inputFiles.get(i).get(0) + "\tFrequency: " + tempCounter);
			}
		}
		
		if(status == true) {
			System.out.println("Query not found!");
		}
		
		System.out.println();		
	}
	
	public static void main(String[] args) {				
		//Getting the number of files present in the directory including vocabulary and input files and saving their paths
		try {
			filesChecking(args);
		}catch(FilesNotFoundException e) {	//catch exception if the vocabulary file and at least one input file is missing
			System.out.println(e);
			e.printStackTrace();
			return;
		}
		
		//Saving the number of input files
		numberOfInputFiles = fileNames.size() - 1;
		
		//Initializing the vector of vector with total number of input files
		inputFiles = new Vector<Vector<String>>(numberOfInputFiles);	
		
		//Creating Dynamic number of threads for input files reading and a thread for vocabulary file reading
		InputFileReadingThread inputfile[] = new InputFileReadingThread[numberOfInputFiles];
		VocabFileReadingThread vocabulary = new VocabFileReadingThread(numberOfInputFiles);
		
		//Starting vocabulary thread for file reading
		vocabulary.start();
		
		//Starting input threads for file reading
		for(int i = 0; i < numberOfInputFiles; i++) {
			inputfile[i] = new InputFileReadingThread(i);
			inputfile[i].start();
		}
		
		//Joining vocabulary Thread
		try {
			vocabulary.join();
		}
		catch(Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}
		
		//Joining input Threads 
		try {
			for(int i = 0; i < numberOfInputFiles; i++) {
				inputfile[i].join();
			}
		}
		catch(Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}
						
		
		//Matching words and creating objects
		WordsMatching();
		
		
		Scanner in = new Scanner(System.in);
		while(true) {
			//Displaying Menu to User
			int option;
			do{
				System.out.println();
				System.out.println("***Menu***");
				System.out.println("Enter (1) for Displaying BST build from Vocabulary File");
				System.out.println("Enter (2) for Displaying Vectors build from Input files");
				System.out.println("Enter (3) for Viewing Match Words and its frequency");
				System.out.println("Enter (4) for Searching a query");
				System.out.println("Enter (5) for Exiting...");
				System.out.print("Option: ");
				option = in.nextInt();
				in.nextLine();
			}while(option < 1 || option > 5);
			
			
			if(option == 1) {
				displayBST();
			}
			else if(option == 2) {
				displayVectors();
			}
			else if(option == 3) {
				displayMatchingWords();
			}
			else if(option == 4) {
				System.out.println();
				System.out.print("Enter the Word: ");
				String query = in.next();
				searchQuery(query);
			}
			else {
				in.close();
				System.out.println("Exiting...");
				return;
			}	
		}			
	}
}
