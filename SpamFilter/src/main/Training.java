package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Scanner;

public class Training {

	private static Hashtable<String, Double> likelihoodTable = new Hashtable<String, Double>();
	private static int m;
	private static long totalWordCount, lexiconSize;
	
	public static final String FEATURE_RESULTS_FILE_NAME_SUFFIX = " FeatureResults.txt";
	
	public static void train(String emailType, int mValue, int kValue) throws FileNotFoundException{
		//Reset values for back-to-back execution
		totalWordCount = 0;
		lexiconSize = 0;
		likelihoodTable.clear();
		
		String featureResultsFileNameFull = emailType.toUpperCase() + " k=" + kValue + FEATURE_RESULTS_FILE_NAME_SUFFIX;
		File featureResultsFile = new File(featureResultsFileNameFull);
		m = mValue;
		computeLikelihoods(featureResultsFile);
		createResultsFile(emailType);
	}
	
	private static void computeLikelihoods(File featureResultsFile) throws FileNotFoundException{
		Scanner trainingScanner = new Scanner(featureResultsFile);
		totalWordCount = trainingScanner.nextLong();
		lexiconSize = trainingScanner.nextLong();
		while(trainingScanner.hasNext()){
			String currentWord = trainingScanner.next();
			if(trainingScanner.hasNext()){
				double count = trainingScanner.nextLong();
				Double likelihood = (count + m)/(totalWordCount + lexiconSize * m); 
				likelihoodTable.put(currentWord, likelihood);
			}
		}
		trainingScanner.close();
	}
	
	/**
	 * Outputs results of feature computing as text file
	 * First line has total word count in training examples and size of lexicon
	 * Each line after that has a word and the likelihood of that
	 * word appearing in the email of type emailType
	 * @param emailType
	 */
	private static void createResultsFile(String emailType){
		try {
			PrintWriter writer = new PrintWriter(emailType.toUpperCase() + " m=" + m + " TrainingResults.txt", "UTF-8");
			writer.println(totalWordCount + " " + lexiconSize);
			Enumeration<String> strings = likelihoodTable.keys();
			while(strings.hasMoreElements()){
				String nextString = strings.nextElement();
				Double likelihood = likelihoodTable.get(nextString);
				writer.println(nextString + " " + likelihood.doubleValue());
			}
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
}
