package imageClassification;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;
import libsvm.svm_parameter;
import libsvm.svm_problem;

import org.imgscalr.Scalr;


public class Main {

	static File clutchDir = new File("images/trainingSet/clutchBags/trainingSet");
	static File flatDir = new File("images/trainingSet/flatShoes/trainingSet");
	static File hoboDir = new File("images/trainingSet/hoboBags/trainingSet");	
	static File pumpDir = new File("images/trainingSet/pumpShoes/trainingSet");
	
	static File clutchTestDir = new File("images/testSet/clutchBags");
	static File flatTestDir = new File("images/testSet/flatShoes");
	static File hoboTestDir = new File("images/testSet/hoboBags");
	static File pumpTestDir = new File("images/testSet/pumpShoes");
	
    static File clutchTune = new File("images/trainingSet/clutchBags/tuningSet");
    static File flatTune = new File("images/trainingSet/flatShoes/tuningSet");
    static File pumpTune = new File("images/trainingSet/pumpShoes/tuningSet");
    static File hoboTune = new File("images/trainingSet/hoboBags/tuningSet");
	
    static final int clutchLinearHistogramC = 4;
    static final int flatLinearHistogramC = 1;
    static final int hoboLinearHistogramC = 8;
    static final int pumpLinearHistogramC = 1;
    
    static final int clutchLinearVectorC = 9;
    static final int flatLinearVectorC = 4;
    static final int hoboLinearVectorC = 1;
    static final int pumpLinearVectorC = 8;
    
    static final int clutchRBFHistogramC = 6;
    static final double clutchRBFHistogramGamma = 100;
    static final int flatRBFHistogramC = 1;
    static final double flatRBFHistogramGamma = 100;
    static final int hoboRBFHistogramC = 2;
    static final double hoboRBFHistogramGamma = 100;
    static final int pumpRBFHistogramC = 1;
    static final double pumpRBFHistogramGamma = 100;

    static final int clutchRBFVectorC = 10;
    static final double clutchRBFVectorGamma = 0.00000003625815809;
    static final int flatRBFVectorC = 100;
    static final double flatRBFVectorGamma = 0.00000003625815809;
    static final int hoboRBFVectorC = 100;
    static final double hoboRBFVectorGamma = 0.00000003625815809;
    static final int pumpRBFVectorC = 100;
    static final double pumpRBFVectorGamma = 0.00000003625815809;
    
    
	public static void main(String[] args) throws IOException{
		BufferedImage img = null;
		svm_parameter param = new svm_parameter();
		param.kernel_type = svm_parameter.LINEAR;
		param.svm_type = svm_parameter.C_SVC;
	    param.probability = 1;
	    param.cache_size = 100000;
	    param.eps = 0.001;  
	    
		svm_problem problem = new svm_problem();
		
		int clutchLen = clutchDir.listFiles().length;
		int flatLen = flatDir.listFiles().length;
		int hoboLen = hoboDir.listFiles().length;
		int pumpLen = pumpDir.listFiles().length;
		
//		File[] dirArray = {clutchDir, flatDir, hoboDir, pumpDir};
		File[] dirArray = {flatDir, clutchDir, hoboDir, pumpDir};
		int numPics = clutchLen + flatLen + hoboLen + pumpLen;
		
		
	    problem.y = new double[numPics];
	    problem.l = numPics;
	    problem.x = new svm_node[numPics][];     
		
	    
	    
	    int idx = 0;
	    for(File dir : dirArray){
	    	File[] fileArray = dir.listFiles();
	    	for(int i = 0; i < fileArray.length; i++){
	    		img = ImageIO.read(fileArray[i]);
				int[] vec = makeVector(img);
//	    		float[] vec = makeHistogram(img);
				problem.x[i+idx] = makeArrayOfNodes(vec);
	    	}
	    	idx += fileArray.length;
	    }
	    
	    for(int i = 0; i < numPics; i++){
	    	problem.y[i] = 0;
	    }
	    
	    
	    for(int i = flatLen; i < flatLen+clutchLen; i++){
	    	problem.y[i] = 1;
	    }
	    
	    param.C = clutchLinearVectorC;
	    svm_model clutchModel = svm.svm_train(problem, param);
	    
	    dirArray[0] = clutchDir;
	    dirArray[1] = flatDir;
	    
	    idx = 0;
	    for(File dir : dirArray){
	    	File[] fileArray = dir.listFiles();
	    	for(int i = 0; i < fileArray.length; i++){
	    		img = ImageIO.read(fileArray[i]);
				int[] vec = makeVector(img);
//				float[] vec = makeHistogram(img);
				problem.x[i+idx] = makeArrayOfNodes(vec);
	    	}
	    	idx += fileArray.length;
	    }


	    
	    
	    for(int i = 0; i < numPics; i++){
	    	problem.y[i] = 0;
	    }
	    for(int i = clutchLen; i < clutchLen + flatLen; i++){
	    	problem.y[i] = 1;
	    }
	    
	    param.C = flatLinearVectorC;
	    svm_model flatModel = svm.svm_train(problem, param);

	    for(int i = clutchLen; i < clutchLen + flatLen; i++){
	    	problem.y[i] = 0;
	    }
	    for(int i = clutchLen + flatLen; i < clutchLen + flatLen + hoboLen; i++){
	    	problem.y[i] = 1;
	    }
	    
	    param.C = hoboLinearVectorC;
	    svm_model hoboModel = svm.svm_train(problem, param);

	    for(int i = clutchLen + flatLen; i < clutchLen + flatLen + hoboLen; i++){
	    	problem.y[i] = 0;
	    }
	    for(int i = clutchLen + flatLen + hoboLen; i < numPics; i++){
	    	problem.y[i] = 1;
	    }
	    
	    param.C = pumpLinearVectorC;
	    svm_model pumpModel = svm.svm_train(problem, param);

	    classifyTestImages(clutchModel, flatModel, hoboModel, pumpModel, false);
	 	       
	}    
	
	private static float[] tune(svm_model model, File postiveDir, File[] negitiveDir, boolean isHistogram) throws IOException{
		float[] retArr = new float[2];
		int correctCount = 0;
		for(File imgFile : postiveDir.listFiles()){
			BufferedImage tmpImg = ImageIO.read(imgFile);
			svm_node[] nodes;
			if(isHistogram){
				float[] vec = makeHistogram(tmpImg);
				nodes = makeArrayOfNodes(vec);	
			}
			else{
				int[] vec = makeVector(tmpImg);
				nodes = makeArrayOfNodes(vec);
			}
			
			double[] prob_estimates = new double[2];
		    correctCount+= svm.svm_predict_probability(model, nodes, prob_estimates);
		    int a = 0;
		    //if(values[i] != prob_estimates[1]){
		    //	System.out.println(values[i] + " " + prob_estimates[1]);
		}
		
		int incorrectNegCount = 0;
		int totalNeg = 0;
		for(File negDir : negitiveDir){
			totalNeg += negDir.listFiles().length;
			for(File imgFile : negDir.listFiles()){
				BufferedImage tmpImg = ImageIO.read(imgFile);
				svm_node[] nodes;
				if(isHistogram){
					float[] vec = makeHistogram(tmpImg);
					nodes = makeArrayOfNodes(vec);	
				}
				else{
					int[] vec = makeVector(tmpImg);
					nodes = makeArrayOfNodes(vec);
				}
			
				double[] prob_estimates = new double[2];
				incorrectNegCount += svm.svm_predict_probability(model, nodes, prob_estimates);
			}
		}
		
		
		System.out.println("Correctly categorized files " + correctCount + "/" + postiveDir.listFiles().length + "=" +  ((float)correctCount/postiveDir.listFiles().length));
		retArr[0] = ((float)correctCount/postiveDir.listFiles().length);
		System.out.println("Incorrectly categorized files " + incorrectNegCount + "/" + totalNeg + "=" +  ((float)incorrectNegCount/totalNeg));
		retArr[1] = ((float)incorrectNegCount/totalNeg);
		return retArr;
	}
	
	private static svm_node[] makeArrayOfNodes(float[] vec) {
		svm_node[] nodes = new svm_node[vec.length];
	    for (int i = 0; i < vec.length; i++){
	        svm_node node = new svm_node();
	        node.index = i + 1;
	        node.value = vec[i];

	        nodes[i] = node;
	    }
	    return nodes;
	}
	
	public static svm_node[] makeArrayOfNodes(int[] vec){
		svm_node[] nodes = new svm_node[vec.length];
	    for (int i = 0; i < vec.length; i++){
	        svm_node node = new svm_node();
	        node.index = i + 1;
	        node.value = vec[i];

	        nodes[i] = node;
	    }
	    return nodes;
	}
	
	public static void reportTuneData(svm_model model, String modelName, DataOutputStream dos) throws IOException{
		File pos = null;
    	File[] neg = {flatTune, pumpTune, hoboTune};
    	String category = null;
    	if(modelName.contains("clutch")){
    		pos = clutchTune;
    		category = "Clutch";
    	}
    	else if(modelName.contains("flat")){
    		pos = flatTune;
    		neg[0] = clutchTune;
    		category = "Flat";
    	}
    	else if(modelName.contains("pump")){
    		pos = pumpTune;
    		neg[1] = clutchTune;
    		category = "Pump";
    	}
    	else{
    		pos = hoboTune;
    		neg[2] = clutchTune;
    		category = "Hobo";
    	}
       	String modType = modelName.contains("Linear") ? "Linear" : "RBF";
    	String vecType = modelName.contains("Histogram") ? "Histogram" : "Vector";
    	int Cidx = modelName.indexOf("C=");
    	int Gidx = modelName.indexOf("G=");
    	float[] a = tune(model, pos, neg, modelName.contains("Histogram"));
    	if(Gidx < 0){
    		String C = modelName.substring(Cidx + 2, modelName.length());
    		String str = category + ",Linear/RBF," + modType + ",Histogram/Vector," + vecType + ",C,"+ C +",G,NULL,PositveTune," + a[0] + ",NegitiveTunePostive," + a[1]  + "\n";
    		System.out.print(str);
    		dos.writeBytes(str);
    	}
    	else{
    		String C = modelName.substring(Cidx + 2, Gidx);
	    	String G = modelName.substring(Gidx + 2);
	    	String str = category+",Linear/RBF," + modType + ",Histogram/Vector," + vecType + ",C,"+ C +",G,"+G+",PositveTune," + a[0] + ",NegitiveTunePostive," + a[1]  + "\n";
	    	System.out.print(str);
    		dos.writeBytes(str);
    	}
    	dos.flush();
	}	
	
	public static void classifyTestImages(svm_model clutchModel, svm_model flatModel, svm_model hoboModel, svm_model pumpModel, boolean isHistogram) throws IOException{
		BufferedImage img = null;
		int correctClutch = 0;
		int correctFlat = 0;
		int correctHobo = 0;
		int correctPump = 0;
		for(File clutchImageFile : clutchTestDir.listFiles()){
			img = ImageIO.read(clutchImageFile);
			String classifier = classifyImage(clutchModel,flatModel,hoboModel,pumpModel,img,isHistogram);
			if(classifier == "Clutch"){
				correctClutch++;
			}
		}
		System.out.println("Correctly Classified Clutch Bag Images " + correctClutch + " / " + clutchTestDir.listFiles().length);
		
		for(File flatImageFile : flatTestDir.listFiles()){
			img = ImageIO.read(flatImageFile);
			String classifier = classifyImage(clutchModel,flatModel,hoboModel,pumpModel,img,isHistogram);
			if(classifier == "Flat"){
				correctFlat++;
			}
		}
		System.out.println("Correctly Classified Flat Shoe Images " + correctFlat + " / " + flatTestDir.listFiles().length);

		for(File hoboImageFile : hoboTestDir.listFiles()){
			img = ImageIO.read(hoboImageFile);
			String classifier = classifyImage(clutchModel,flatModel,hoboModel,pumpModel,img,isHistogram);
			if(classifier == "Hobo"){
				correctHobo++;
			}
		}
		System.out.println("Correctly Classified Hobo Bag Images " + correctHobo + " / " + hoboTestDir.listFiles().length);

		for(File pumpImageFile : pumpTestDir.listFiles()){
			img = ImageIO.read(pumpImageFile);
			String classifier = classifyImage(clutchModel,flatModel,hoboModel,pumpModel,img,isHistogram);
			if(classifier == "Pump"){
				correctPump++;
			}
		}
		System.out.println("Correctly Classified Clutch Images " + correctPump + " / " + pumpTestDir.listFiles().length);

		int totalCorrect = correctClutch + correctFlat + correctHobo + correctPump;
		int totalImages = clutchTestDir.listFiles().length + flatTestDir.listFiles().length + hoboTestDir.listFiles().length + pumpTestDir.listFiles().length;
		System.out.println("Correctly Classifed Images " + totalCorrect + " / " + totalImages);
	}
	
	public static String classifyImage(svm_model clutchModel, svm_model flatModel, svm_model hoboModel, svm_model pumpModel, BufferedImage img, boolean isHistogram){
		svm_node[] nodes = null;
		if(isHistogram){
			float[] histogram = makeHistogram(img);
			nodes = makeArrayOfNodes(histogram);
		}
		else{
			int[] vector = makeVector(img);
			nodes = makeArrayOfNodes(vector);
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
	
	public static int getRed(int color){
		return (color >>> 16) & 0xFF;
	}
	public static int getGreen(int color){
		return (color >>> 8) & 0xFF;
	}
	public static int getBlue(int color){
		return color & 0xFF;
	}
	
	public static float[] makeHistogram(BufferedImage img){
		int RGBBuckets[] = new int[8*8*8];
		float RGBFloat[] = new float[8*8*8];
		
		for(int x = 0; x < img.getWidth(); x++){
			for(int y = 0; y < img.getHeight(); y++){
				int pixel = img.getRGB(x, y);
				int red = getRed(pixel)/32;
				int green = getGreen(pixel)/32;
				int blue = getBlue(pixel)/32;
				RGBBuckets[red + 8*green + 64*blue]++;
			}
		}
		for(int r = 0; r < 8; r++){
			for(int g = 0; g < 8; g++){
				for(int b = 0; b < 8; b++){
					RGBFloat[r+ 8*g + 64*b] = ((float)RGBBuckets[r + 8*g + 64*b])/(img.getWidth()*img.getHeight());
				}
			}
		}
		return RGBFloat;
	}
	
	public static int[] makeVector(BufferedImage img){
		int[] vector = new int[3072];
		
		BufferedImage scaledImage = Scalr.resize(img, 32);//Uses the ImgScalr library to achieve this task
		
		for(int y = 0; y < 32; y++){
			for(int x = 0; x < 32; x++){
				int pixel = scaledImage.getRGB(x, y);
				int red = getRed(pixel);
				int green = getGreen(pixel);
				int blue = getBlue(pixel);
				vector[y*96 + 3*x] = red;
				vector[y*96 + 3*x + 1] = green;
				vector[y*96 + 3*x + 2] = blue;
			}
		}
		
		
		return vector;
	}

}


/*		BufferedImage img1 = ImageIO.read(new File("images/testSet/img_bags_clutch_104.jpg"));
BufferedImage img2 = ImageIO.read(new File("images/testSet/img_bags_clutch_152.jpg"));
BufferedImage img3 = ImageIO.read(new File("images/testSet/img_bags_hobo_386.jpg"));
BufferedImage img4 = ImageIO.read(new File("images/testSet/img_bags_hobo_390.jpg"));
BufferedImage img5 = ImageIO.read(new File("images/testSet/img_womens_flats_366.jpg"));
BufferedImage img6 = ImageIO.read(new File("images/testSet/img_womens_flats_422.jpg"));
BufferedImage img7 = ImageIO.read(new File("images/testSet/img_womens_pumps_275.jpg"));
BufferedImage img8 = ImageIO.read(new File("images/testSet/img_womens_pumps_289.jpg"));
BufferedImage[] testImages = {img1, img2, img3, img4, img5, img6, img7, img8};
*/

//double[] prob_estimates = new double[2];
//svm.svm_predict_probability(clutchModel, nodes, prob_estimates);
//double clutchProb = prob_estimates[1];
//svm.svm_predict_probability(flatModel, nodes, prob_estimates);
//double flatProb = prob_estimates[1];
//svm.svm_predict_probability(hoboModel, nodes, prob_estimates);
//double hoboProb = prob_estimates[1];
//svm.svm_predict_probability(pumpModel, nodes, prob_estimates);
//double pumpProb = prob_estimates[1];
//System.out.println("Clutch Prob " + clutchProb);
//System.out.println("Flat Prob " + flatProb);
//System.out.println("Hobo Prob " + hoboProb);
//System.out.println("Pump Prob " + pumpProb);
//System.out.println();