package main;

import java.io.File;
import java.io.FileNotFoundException;

public class SpamFilterRunner {
	
	public static final int EXPECTED_ARG_COUNT = 4;
	
	public static final String DEFAULT_HAM_TRAINING_FILES_DIRECTORY_PATH = "emails/hamtraining/";
	public static final String DEFAULT_SPAM_TRAINING_FILES_DIRECTORY_PATH = "emails/hamtraining/";
	//public static final String DEFAULT_TRAINING_FILES_TYPE = "HAM";
	public static final int DEFAULT_K = 1;
	public static final int DEFAULT_M = 1;
	
	private static double totalTrainingFileCount, spamPriorLikelihood, hamPriorLikelihood;
	
	/**
	 * 
	 * @param args [0]: Path to training files directory
	 *             [1]: Training files email type
	 *             [2]: Value for k
	 *             [3]: Name of feature computing results file
	 *             [4]: Value for m
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException {
		String trainingHamFilesDirectoryPath, trainingSpamFilesDirectoryPath, featureResultsFileName;
		int kValue, mValue;
		//Check to see if arguments were inputed through the command line 
		if (args.length == EXPECTED_ARG_COUNT ) {
			trainingHamFilesDirectoryPath = args[0];
			trainingSpamFilesDirectoryPath = args[1];
			kValue = Integer.parseInt(args[2]);
			featureResultsFileName = args[3];
			mValue = Integer.parseInt(args[4]);
		}
		//Else use default values
		else{
			trainingHamFilesDirectoryPath = DEFAULT_HAM_TRAINING_FILES_DIRECTORY_PATH;
			trainingSpamFilesDirectoryPath = DEFAULT_SPAM_TRAINING_FILES_DIRECTORY_PATH;
			kValue = DEFAULT_K;
			featureResultsFileName = null;
			mValue = DEFAULT_M;
		}
		File directoryTester = new File(trainingHamFilesDirectoryPath);
		//Check to see if directory exists
		if (directoryTester.isDirectory()) {
			FeatureComputing.computeFeatures(trainingHamFilesDirectoryPath, "HAM", kValue);
			int hamTrainingFileCount = FeatureComputing.getTrainingFileCount();
			FeatureComputing.computeFeatures(trainingSpamFilesDirectoryPath, "SPAM", kValue);
			int spamTrainingFileCount = FeatureComputing.getTrainingFileCount();
			totalTrainingFileCount = hamTrainingFileCount + spamTrainingFileCount;
			spamPriorLikelihood = spamTrainingFileCount/totalTrainingFileCount;
			hamPriorLikelihood = hamTrainingFileCount/totalTrainingFileCount;
			Training.Train(featureResultsFileName,"HAM", mValue);
			Training.Train(featureResultsFileName,"SPAM", mValue);
		}
		
	}

}
