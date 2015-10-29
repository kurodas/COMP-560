package main;

import java.io.File;
import java.io.FileNotFoundException;

public class SpamFilterRunner {
	
	public static final int EXPECTED_ARG_COUNT = 2;
	
	public static final String HAM_TRAINING_FILES_DIRECTORY_PATH = "emails/hamtraining/";
	public static final String SPAM_TRAINING_FILES_DIRECTORY_PATH = "emails/spamtraining/";
	public static final String HAM_TEST_FILES_DIRECTORY_PATH = "emails/hamtesting/";
	public static final String SPAM_TEST_FILES_DIRECTORY_PATH = "emails/spamtesting/";
	
	public static final int DEFAULT_K = 1;
	public static final int DEFAULT_M = 1;
	
	public static final int[] K_VALUES = {1,2,3,4,5,6,10,25};
	public static final int[] M_VALUES = {1, 5, 10, 25, 50};
	
	private static double totalTrainingFileCount, spamPriorLikelihood, hamPriorLikelihood;
	
	/**
	 * 
	 * @param args [1]: Value for k
	 *             [2]: Value for m
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException {
		int kValue, mValue;
		//Check to see if arguments were inputed through the command line 

//		if (args.length == EXPECTED_ARG_COUNT ) {
//			kValue = Integer.parseInt(args[0]);
//			mValue = Integer.parseInt(args[1]);
//		}
//		//Else use default values
//		else{
//			kValue = DEFAULT_K;
//			mValue = DEFAULT_M;
//		}
		for(int kIndex=0; kIndex < K_VALUES.length; kIndex++){
			kValue = K_VALUES[kIndex];
			for(int mIndex = 0; mIndex < M_VALUES.length; mIndex++){
				mValue = M_VALUES[mIndex];

				File directoryTester = new File(HAM_TRAINING_FILES_DIRECTORY_PATH);
				//Check to see if directory exists
				if (directoryTester.isDirectory()) {
					FeatureComputing.computeFeatures(HAM_TRAINING_FILES_DIRECTORY_PATH, SPAM_TRAINING_FILES_DIRECTORY_PATH, kValue);
					int hamTrainingFileCount = FeatureComputing.getHamTrainingFileCount();
					int spamTrainingFileCount = FeatureComputing.getSpamTrainingFileCount();
					
					totalTrainingFileCount = hamTrainingFileCount + spamTrainingFileCount;
					spamPriorLikelihood = spamTrainingFileCount/totalTrainingFileCount;
					hamPriorLikelihood = hamTrainingFileCount/totalTrainingFileCount;
					
					Training.train(mValue, kValue);
					Testing.test(mValue, kValue, HAM_TEST_FILES_DIRECTORY_PATH,
							spamPriorLikelihood, hamPriorLikelihood, "HAM");
					Testing.test(mValue, kValue,
							SPAM_TEST_FILES_DIRECTORY_PATH,
							spamPriorLikelihood, hamPriorLikelihood, "SPAM");
				}
			}
		}
		
	}

}
