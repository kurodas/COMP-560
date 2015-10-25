package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Scanner;

public class FeatureComputing {
	private static long wordCount;
	private static int trainingFileCount;
	
	//Hashtable keeps count of occurrences of words
	private static Hashtable<String, Long> featuresTable = new Hashtable<String, Long>();
	
	public static void computeFeatures(String folderPath, String emailType, int kValue) throws FileNotFoundException{
		File trainingFilesDirectory = new File(folderPath);
		if(trainingFilesDirectory.isDirectory()){
			File[] trainingFiles = trainingFilesDirectory.listFiles();
			trainingFileCount = 100;
			for(File email : trainingFiles){
				processEmail(email);
			}
			cullFeatures(kValue);
			if(emailType.equalsIgnoreCase("HAM") || emailType.equalsIgnoreCase("SPAM"))
				createResultsFile(emailType);
		}
	}
	
	public static int getTrainingFileCount() {
		return trainingFileCount;
	}
	
	/**
	 * Scans email for features
	 * Adds strings that have not been previously seen to featuresTable
	 * Increments count for features that have already been previously seen
	 * @param email
	 * @throws FileNotFoundException
	 */
	private static void processEmail(File email) throws FileNotFoundException{
		Scanner emailScanner = new Scanner(email);
		while(emailScanner.hasNext()){
			String currentString = emailScanner.next();
//			if (!currentString.equalsIgnoreCase(".")
//					&& !currentString.equalsIgnoreCase(",")
//					&& !currentString.equalsIgnoreCase("-")
//					&& !currentString.equalsIgnoreCase("/")
//					&& !currentString.equalsIgnoreCase("\\")
//					&& !currentString.equalsIgnoreCase("\'")
//					&& !currentString.equalsIgnoreCase("\"")) {
			wordCount++;
				if (!featuresTable.containsKey(currentString)) {
					featuresTable.put(currentString, new Long(1));
				} else {
					Long previousCount = featuresTable.get(currentString);
					Long newCount = new Long(previousCount.longValue() + 1);
					featuresTable.put(currentString, newCount);
				}
//			}
		}
		emailScanner.close();
	}
	
	//Removes words with fewer than K occurrences from featuresTable
	private static void cullFeatures(int kValue){
		Enumeration<String> strings = featuresTable.keys();
		while(strings.hasMoreElements()){
			String nextString = strings.nextElement();
			Long occurrenceCount = featuresTable.get(nextString);
			if(occurrenceCount.longValue() < kValue){
				featuresTable.remove(nextString);
			}
		}
	}
	/**
	 * Outputs results of feature computing as text file
	 * First line has total word count in training examples and size of lexicon
	 * Each line after that has a word and the number of times 
	 * that that word was seen in the training examples for emailType
	 * @param emailType
	 */
	private static void createResultsFile(String emailType){
		try {
			PrintWriter writer = new PrintWriter(emailType.toUpperCase() + "FeatureResults.txt", "UTF-8");
			writer.println(wordCount + " " + featuresTable.size());
			Enumeration<String> strings = featuresTable.keys();
			while(strings.hasMoreElements()){
				String nextString = strings.nextElement();
				Long occurrenceCount = featuresTable.get(nextString);
				writer.println(nextString + " " + occurrenceCount.longValue());
			}
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	
}
