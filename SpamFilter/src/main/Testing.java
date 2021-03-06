package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Scanner;

public class Testing {
	
	public static String TRAINING_RESULTS_FILE_NAME_SUFFIX = " TrainingResults.txt";
	
	private static int m, k; 
	private static double spamPriorLikelihoodLog, hamPriorLikelihoodLog, numberOfTestFiles, correctlyClassifiedCount; 
	private static long totalWordCount, lexiconSize;
	
	private static Hashtable<String, Double> spamLogTable = new Hashtable<String, Double>();
	private static Hashtable<String, Double> hamLogTable = new Hashtable<String, Double>();
	private static ArrayList<ClassifiedEmail> classifiedEmails = new ArrayList<ClassifiedEmail>();

	private static String testFileClass;
	
//	The values to be used for words that were not seen during training
//	private static double spamDefaultLikelihoodLog, hamDefaultLikelihoodLog;
	
	public static void test(int mValue, int kValue, String testFileDirectoryPath,
			Double spamPrior, Double hamPrior, String testClass)
			throws FileNotFoundException {
		//Reset values for back-to-back execution
		spamLogTable.clear();
		hamLogTable.clear();
		classifiedEmails.clear();
		correctlyClassifiedCount = 0;
		
		m = mValue;
		k = kValue;
		String spamTrainingResultsFileNameFull = "SPAM k=" + k + " m=" + m + TRAINING_RESULTS_FILE_NAME_SUFFIX;
		String hamTrainingResultsFileNameFull = "HAM k=" + k + " m=" + m + TRAINING_RESULTS_FILE_NAME_SUFFIX;
		spamPriorLikelihoodLog = Math.log(spamPrior);
		hamPriorLikelihoodLog = Math.log(hamPrior);
		populateTable(spamTrainingResultsFileNameFull, "SPAM");
		populateTable(hamTrainingResultsFileNameFull, "HAM");
		testFileClass = testClass;
		File testFilesDirectory = new File(testFileDirectoryPath);
		if(testFilesDirectory.isDirectory()){
			File[] testEmails = testFilesDirectory.listFiles();
			numberOfTestFiles = testEmails.length;
			for(File email: testEmails){
				classifyEmail(email);
			}
		}
		createResultsFile();
		
	}
	
	private static void populateTable(String trainingFileName, String emailType) throws FileNotFoundException{
		Scanner fileScanner = new Scanner(new File(trainingFileName));
		Hashtable<String, Double> currentTable;
		if(emailType.equalsIgnoreCase("HAM")){
			totalWordCount = fileScanner.nextLong();
			lexiconSize = fileScanner.nextLong();
			currentTable = hamLogTable;
		}
		else{
			totalWordCount = fileScanner.nextLong();
			lexiconSize = fileScanner.nextLong();
			currentTable = spamLogTable;
		}
		while(fileScanner.hasNext()){
			String currentWord = fileScanner.next();
			Double likelihoodLog = Math.log(fileScanner.nextDouble());
			currentTable.put(currentWord, likelihoodLog);
		}

		fileScanner.close();
	}
	
	private static void classifyEmail(File email) throws FileNotFoundException{
		Double spamPosterior = spamPriorLikelihoodLog; 
		Double hamPosterior = hamPriorLikelihoodLog;
		Scanner emailScanner = new Scanner(email);
		while(emailScanner.hasNext()){
			String currentWord = emailScanner.next();
			Double hamLikelihood = hamLogTable.get(currentWord);
			Double spamLikelihood = spamLogTable.get(currentWord);
			if(hamLikelihood != null){
				hamPosterior += hamLikelihood.doubleValue();
			}
			if(spamLikelihood != null){
				spamPosterior += spamLikelihood.doubleValue();
			}
		}
		if(hamPosterior >= spamPosterior){
			classifiedEmails.add(new ClassifiedEmail(email.getName(), "HAM", spamPosterior, hamPosterior));
			if(testFileClass.equalsIgnoreCase("HAM")){
				correctlyClassifiedCount++;
			}
		}
		else{
			classifiedEmails.add(new ClassifiedEmail(email.getName(), "SPAM", spamPosterior, hamPosterior));
			if(testFileClass.equalsIgnoreCase("SPAM")){
				correctlyClassifiedCount++;
			}
		}
		emailScanner.close();
	}
	//Creates the output file of the testing results
	private static void createResultsFile(){
		try {
			String outputFileName = testFileClass.toUpperCase() + " k=" + k + " m=" + m + " TestingResults.txt";
			PrintWriter writer = new PrintWriter(outputFileName, "UTF-8");
//			System.out.println("With k=" + k + " and m=" + m);
//			System.out.println("fraction of emails correctly classified as " + testFileClass + ": " + correctlyClassifiedCount/numberOfTestFiles);
			writer.println("With k=" + k + " and m=" + m);
			writer.println("fraction of emails correctly classified as " + testFileClass + ": " + correctlyClassifiedCount/numberOfTestFiles);
			for(int i = 0; i < numberOfTestFiles; i++){
				ClassifiedEmail currentEmail = classifiedEmails.get(i);
				writer.println(currentEmail.getEmailFileName()
						+ " was classified as "
						+ currentEmail.getEmailClassification());
				writer.println("Ham posterior log: " + currentEmail.getHamProbabilityLog());
				writer.println("Spam posterior log: " + currentEmail.getSpamProbabilityLog());
				writer.println();
			}
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
}
