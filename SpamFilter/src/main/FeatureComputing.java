package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Scanner;

public class FeatureComputing {
	private static long wordCount;
	private static int hamTrainingFileCount, spamTrainingFileCount, k;
	
	//Hashtable keeps count of occurrences of words
	private static Hashtable<String, Feature> featuresTable = new Hashtable<String, Feature>();
	
	public static void computeFeatures(String hamFolderPath, String spamFolderPath, int kValue) throws FileNotFoundException{
		//Reset values for back-to-back execution
		wordCount = 0;
		featuresTable.clear();
		hamTrainingFileCount = 0;
		spamTrainingFileCount = 0;
		
		k = kValue;
		
		
		//Compute features of ham training files
		File hamtrainingFilesDirectory = new File(hamFolderPath);
		if(hamtrainingFilesDirectory.isDirectory()){
			File[] trainingFiles = hamtrainingFilesDirectory.listFiles();
			hamTrainingFileCount += trainingFiles.length;
			for(File email : trainingFiles){
				processEmail(email, "HAM");
			}
		}
		
		//Compute features of spam training files 
		File spamtrainingFilesDirectory = new File(spamFolderPath);
		if(spamtrainingFilesDirectory.isDirectory()){
			File[] trainingFiles = spamtrainingFilesDirectory.listFiles();
			spamTrainingFileCount += trainingFiles.length;
			for(File email : trainingFiles){
				processEmail(email, "SPAM");
			}
		}
		cullFeatures();
		createResultsFile();
	}
	
	public static int getHamTrainingFileCount() {
		return hamTrainingFileCount;
	}
	
	public static int getSpamTrainingFileCount() {
		return spamTrainingFileCount;
	}
	
	/**
	 * Scans email for features
	 * Adds strings that have not been previously seen to featuresTable
	 * Increments count for features that have already been previously seen
	 * @param email
	 * @throws FileNotFoundException
	 */
	private static void processEmail(File email, String emailType) throws FileNotFoundException{
		Scanner emailScanner = new Scanner(email);
		while(emailScanner.hasNext()){
			String currentString = emailScanner.next();
			wordCount++;
			//Insert previously unseen features into the feature table
			if (!featuresTable.containsKey(currentString)) {
				Feature newFeature = new Feature(currentString);
				//Increment the count of the appropriate type
				if(emailType.equalsIgnoreCase("SPAM")){
					newFeature.spamOccurrenceCount++;
				}
				else{
					newFeature.hamOccurrenceCount++;
				}
				featuresTable.put(currentString, newFeature);
			} else {
				//Update count for previously seen feature
				Feature existingFeature = featuresTable.get(currentString);
				//Increment the count of the appropriate type
				if(emailType.equalsIgnoreCase("SPAM")){
					existingFeature.spamOccurrenceCount++;
				}
				else{
					existingFeature.hamOccurrenceCount++;
				}
			}
		}
		emailScanner.close();
	}
	
	//Removes words with fewer than K occurrences in either class from featuresTable 
	private static void cullFeatures(){
		Enumeration<String> strings = featuresTable.keys();
		while(strings.hasMoreElements()){
			String currentString = strings.nextElement();
			Feature currentFeature = featuresTable.get(currentString);
			if(currentFeature.hamOccurrenceCount <= k && currentFeature.spamOccurrenceCount <= k){
				featuresTable.remove(currentString);
			}
		}
	}
	/**
	 * Outputs results of feature computing as text file
	 * First line has total word count in training examples and size of lexicon
	 * Each line after that has a word and the number of times 
	 * that that word was seen in the training examples for spam and ham, in that order.
	 * @param emailType
	 */
	private static void createResultsFile(){
		try {
			PrintWriter writer = new PrintWriter("k=" + k + " FeatureResults.txt", "UTF-8");
			writer.println(wordCount + " " + featuresTable.size());
			Enumeration<String> strings = featuresTable.keys();
			while(strings.hasMoreElements()){
				String nextString = strings.nextElement();
				long spamOccurrenceCount = featuresTable.get(nextString).spamOccurrenceCount;
				long hamOccurrenceCount = featuresTable.get(nextString).hamOccurrenceCount;
				writer.println(nextString + " " + spamOccurrenceCount + " " + hamOccurrenceCount);
			}
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	
}
