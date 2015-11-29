package imageClassification;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;
import libsvm.svm_parameter;
import libsvm.svm_problem;

public class TestModels {

	static File clutchDir = new File("images/trainingSet/clutchBags/trainingSet");
	static File flatDir = new File("images/trainingSet/flatShoes/trainingSet");
	static File hoboDir = new File("images/trainingSet/hoboBags/trainingSet");	
	static File pumpDir = new File("images/trainingSet/pumpShoes/trainingSet");
	
	static File clutchTestDir = new File("images/testSet/clutchBags");
	static int clutchTestLen = clutchTestDir.listFiles().length;
	static File flatTestDir = new File("images/testSet/flatShoes");
	static int flatTestLen = flatTestDir.listFiles().length;
	static File hoboTestDir = new File("images/testSet/hoboBags");
	static int hoboTestLen = hoboTestDir.listFiles().length;
	static File pumpTestDir = new File("images/testSet/pumpShoes");
	static int pumpTestLen = pumpTestDir.listFiles().length;
		
	public static void test(int[] cValues, double[] gammaValues, int parameterType, boolean isHistogram) throws IOException{
		BufferedImage img = null;
		svm_parameter param = new svm_parameter();
		param.kernel_type = parameterType;
		param.svm_type = svm_parameter.C_SVC;
	    param.probability = 1;
	    param.cache_size = 100000;
	    param.eps = 0.001;  
		svm_problem problem = new svm_problem();
		
		int clutchLen = clutchDir.listFiles().length;
		int flatLen = flatDir.listFiles().length;
		int hoboLen = hoboDir.listFiles().length;
		int pumpLen = pumpDir.listFiles().length;
		
		File[] dirArray = {flatDir, clutchDir, hoboDir, pumpDir};
		int numPics = clutchLen + flatLen + hoboLen + pumpLen;
		//The data must be ordered with the flat pictures first, because the way libSVM computes probability indexing is based on which classification is seen first
		//so we make flat the first classification seen by our clutch model
		
	    problem.y = new double[numPics];
	    problem.l = numPics;
	    problem.x = new svm_node[numPics][];     
		
	    
	    
	    int idx = 0;
	    for(File dir : dirArray){
	    	File[] fileArray = dir.listFiles();
	    	for(int i = 0; i < fileArray.length; i++){
	    		img = ImageIO.read(fileArray[i]);
	    		if(isHistogram){
	    			float[] vec = Main.makeHistogram(img);
					problem.x[i+idx] = Main.makeArrayOfNodes(vec);
	    		}
	    		else{
	    			int[] vec = Main.makeVector(img);
					problem.x[i+idx] = Main.makeArrayOfNodes(vec);
	    		}
	    	}
	    	idx += fileArray.length;
	    }
	    
	    for(int i = 0; i < numPics; i++){
	    	problem.y[i] = 0;
	    }
	    
	    
	    for(int i = flatLen; i < flatLen+clutchLen; i++){
	    	problem.y[i] = 1;
	    }
	    
	    param.C = cValues[0];
	    param.gamma = gammaValues[0];
	    svm_model clutchModel = svm.svm_train(problem, param);
	 	    
	    
	    dirArray[0] = clutchDir;
	    dirArray[1] = flatDir;
	    //Once the clutch model is made, we must reorder the data and regenerate the problem information
	    //for the flat, hobo, and pump models
	    
	    
	    idx = 0;
	    for(File dir : dirArray){
	    	File[] fileArray = dir.listFiles();
	    	for(int i = 0; i < fileArray.length; i++){
	    		img = ImageIO.read(fileArray[i]);
	    		if(isHistogram){
	    			float[] vec = Main.makeHistogram(img);
					problem.x[i+idx] = Main.makeArrayOfNodes(vec);
	    		}
	    		else{
	    			int[] vec = Main.makeVector(img);
					problem.x[i+idx] = Main.makeArrayOfNodes(vec);
	    		}
	    	}
	    	idx += fileArray.length;
	    }	    
	    
	    for(int i = 0; i < numPics; i++){
	    	problem.y[i] = 0;
	    }
	    for(int i = clutchLen; i < clutchLen + flatLen; i++){
	    	problem.y[i] = 1;
	    }
	    param.C = cValues[1];
	    param.gamma = gammaValues[1];
	    svm_model flatModel = svm.svm_train(problem, param);	    

	    for(int i = clutchLen; i < clutchLen + flatLen; i++){
	    	problem.y[i] = 0;
	    }
	    for(int i = clutchLen + flatLen; i < clutchLen + flatLen + hoboLen; i++){
	    	problem.y[i] = 1;
	    }

	    param.C = cValues[2];
	    param.gamma = gammaValues[2];
	    svm_model hoboModel = svm.svm_train(problem, param);

	    for(int i = clutchLen + flatLen; i < clutchLen + flatLen + hoboLen; i++){
	    	problem.y[i] = 0;
	    }
	    for(int i = clutchLen + flatLen + hoboLen; i < numPics; i++){
	    	problem.y[i] = 1;
	    }
	    
	    param.C = cValues[3];
	    param.gamma = gammaValues[3];
	    svm_model pumpModel = svm.svm_train(problem, param);
	    
	    classifyTestImages(clutchModel, flatModel, hoboModel, pumpModel, isHistogram);
	    

	}
	

	public static void classifyTestImages(svm_model clutchModel, svm_model flatModel, svm_model hoboModel, svm_model pumpModel, boolean isHistogram) throws IOException{
		BufferedImage img = null;
		int correctClutch = 0;
		int incorrectClutch = 0;
		int correctFlat = 0;
		int incorrectFlat = 0;
		int correctHobo = 0;
		int incorrectHobo = 0;
		int correctPump = 0;
		int incorrectPump = 0;
		for(File clutchImageFile : clutchTestDir.listFiles()){
			img = ImageIO.read(clutchImageFile);
			String classifier = classifyImage(clutchModel,flatModel,hoboModel,pumpModel,img,isHistogram);
			switch(classifier){
				case("Clutch"):{
					correctClutch++;
					break; }
				case("Flat"):{
					incorrectFlat++;
					break; }
				case("Hobo"):{
					incorrectHobo++;
					break; }
				case("Pump"):{
					incorrectPump++;
				}
			}
		}
		
		for(File flatImageFile : flatTestDir.listFiles()){
			img = ImageIO.read(flatImageFile);
			String classifier = classifyImage(clutchModel,flatModel,hoboModel,pumpModel,img,isHistogram);
			switch(classifier){
				case("Clutch"):{
					incorrectClutch++;
					break; }
				case("Flat"):{
					correctFlat++;
					break; }
				case("Hobo"):{
					incorrectHobo++;
					break; }
				case("Pump"):{
					incorrectPump++;
				}
			}
	
		}
	
		for(File hoboImageFile : hoboTestDir.listFiles()){
			img = ImageIO.read(hoboImageFile);
			String classifier = classifyImage(clutchModel,flatModel,hoboModel,pumpModel,img,isHistogram);
			switch(classifier){
			case("Clutch"):{
				incorrectClutch++;
				break; }
			case("Flat"):{
				incorrectFlat++;
				break; }
			case("Hobo"):{
				correctHobo++;
				break; }
			case("Pump"):{
				incorrectPump++;
			}
		}		
		}
		for(File pumpImageFile : pumpTestDir.listFiles()){
			img = ImageIO.read(pumpImageFile);
			String classifier = classifyImage(clutchModel,flatModel,hoboModel,pumpModel,img,isHistogram);
			switch(classifier){
			case("Clutch"):{
				incorrectClutch++;
				break; }
			case("Flat"):{
				incorrectFlat++;
				break; }
			case("Hobo"):{
				incorrectHobo++;
				break; }
			case("Pump"):{
				correctPump++;
			}
		}
		}
		System.out.println("Correctly Classified as Clutch Image " + correctClutch + " / " + clutchTestLen);
		System.out.println("Incorrectly Classified as Clutch Image " + incorrectClutch + " / " + (flatTestLen + hoboTestLen + pumpTestLen));
		System.out.println("Correctly Classified as Flat Image " + correctFlat + " / " + flatTestLen);
		System.out.println("Incorrectly Classified as Flat Image " + incorrectFlat + " / " + (clutchTestLen + hoboTestLen + pumpTestLen));
		System.out.println("Correctly Classified as Hobo Image " + correctHobo + " / " + hoboTestLen);
		System.out.println("Incorrectly Classified as Hobo Image " + incorrectHobo + " / " + (flatTestLen + clutchTestLen + pumpTestLen));
		System.out.println("Correctly Classified as Pump Image " + correctPump + " / " + pumpTestLen);
		System.out.println("Incorrectly Classified as Pump Image " + incorrectPump + " / " + (flatTestLen + clutchTestLen + hoboTestLen));
		
		int totalCorrect = correctClutch + correctFlat + correctHobo + correctPump;
		System.out.println("Correctly Classifed Images " + totalCorrect + " / " + (clutchTestLen+flatTestLen+hoboTestLen+pumpTestLen));
	}

	public static String classifyImage(svm_model clutchModel, svm_model flatModel, svm_model hoboModel, svm_model pumpModel, BufferedImage img, boolean isHistogram){
		svm_node[] nodes = null;
		if(isHistogram){
			float[] histogram = Main.makeHistogram(img);
			nodes = Main.makeArrayOfNodes(histogram);
		}
		else{
			int[] vector = Main.makeVector(img);
			nodes = Main.makeArrayOfNodes(vector);
		}
		double[] prob_estimates = new double[2];
		svm.svm_predict_probability(clutchModel, nodes, prob_estimates);
		double clutchProb = prob_estimates[1];
		svm.svm_predict_probability(flatModel, nodes, prob_estimates);
		double flatProb = prob_estimates[1];
		svm.svm_predict_probability(hoboModel, nodes, prob_estimates);
		double hoboProb = prob_estimates[1];
		svm.svm_predict_probability(pumpModel, nodes, prob_estimates);
		double pumpProb = prob_estimates[1];
		if(clutchProb > flatProb & clutchProb > hoboProb & clutchProb > pumpProb){
			return "Clutch";
		}
		else if(flatProb > hoboProb & flatProb > pumpProb){
			return "Flat";
		}
		else if(hoboProb > pumpProb){
			return "Hobo";
		}
		else{
			return "Pump";
		}
	}
	
	
	
}