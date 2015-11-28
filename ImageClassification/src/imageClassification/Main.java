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

    static File clutchTune = new File("images/trainingSet/clutchBags/tuningSet");
    static File flatTune = new File("images/trainingSet/flatShoes/tuningSet");
    static File pumpTune = new File("images/trainingSet/pumpShoes/tuningSet");
    static File hoboTune = new File("images/trainingSet/hoboBags/tuningSet");
	
	public static void main(String[] args) throws IOException{
		BufferedImage img = null;		

		svm_parameter param = new svm_parameter();
		param.kernel_type = svm_parameter.RBF;
		param.svm_type = svm_parameter.C_SVC;
		param.C = .01;
	    param.probability = 1;
//	    param.shrinking = 0;
	    param.cache_size = 100000;
	    param.eps = 0.001;  
//	    param.eps = 0.01;
	    
		svm_problem problem = new svm_problem();
		
		File clutchDir = new File("images/trainingSet/clutchBags/trainingSet");
		int clutchLen = clutchDir.listFiles().length;
		
		File flatDir = new File("images/trainingSet/flatShoes/trainingSet");
		int flatLen = flatDir.listFiles().length;
		
		File hoboDir = new File("images/trainingSet/hoboBags/trainingSet");
		int hoboLen = hoboDir.listFiles().length;
		
		File pumpDir = new File("images/trainingSet/pumpShoes/trainingSet");

		int pumpLen = pumpDir.listFiles().length;
		
		File[] dirArray = {clutchDir, flatDir, hoboDir, pumpDir};
//		File[] dirArray = {flatDir, clutchDir, hoboDir, pumpDir};
		int numPics = clutchLen + flatLen + hoboLen + pumpLen;
		
		
	    problem.y = new double[numPics];
	    problem.l = numPics;
	    problem.x = new svm_node[numPics][];     
		
//	    int idx = 0;
//	    for(File dir : dirArray){
//	    	File[] fileArray = dir.listFiles();
//	    	for(int i = 0; i < fileArray.length; i++){
//	    		img = ImageIO.read(fileArray[i]);
//				int[] vec = makeVector(img);
//////				float[] vec = makeHistogram(img);
//				problem.x[i+idx] = makeArrayOfNodes(vec);
//	    	}
//	    	idx += fileArray.length;
//	    }
	    for(int i = 0; i < numPics; i++){
	    	problem.y[i] = 0;
	    }
	    
	    
//	    for(int i = flatLen; i < flatLen+clutchLen; i++){
//	    	problem.y[i] = 1;
//	    }
	    
	    svm_model clutchModel = null;
	
	    param.gamma = ((double) 1/(20000*numPics));

	    

	    System.out.println(param.gamma);
    
    	
//    	DataOutputStream fp = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("ClutchModelVectorRBFAttempt2.csv")));
//	    for(int G = 1; G < 100; G*= 10){
//	    	for(int C = 1; C < 101; C*= 10){
//		    	param.C = C;
//		    	param.gamma = ((double) G/(20000*numPics));
//		    	//param.gamma = gamma;
//		    	clutchModel = svm.svm_train(problem,param);
//		    	String str = "clutchModelRBFHistogramC="+C+"G="+param.gamma;
//		    	reportTuneData(clutchModel, str, fp);
////		    	tune(clutchModel, clutchTune, neg, false);
//	    }
//	  }
//	  fp.close();
    	
    	
	    int idx = 0;
//	    svm_node[][] picturesNodes = new svm_node[numPics][]; 
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
	    
	    svm_model flatModel = null;//svm.svm_train(problem, param);
	    
    	DataOutputStream fp = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("FlatModelVectorRBFAttempt2.csv")));
	    for(int G = 1; G < 1000; G*= 10){
	    	for(int C = 1; C < 101; C*= 10){
		    	param.C = C;
		    	param.gamma = ((double) G/(20000*numPics));
		    	//param.gamma = gamma;
		    	clutchModel = svm.svm_train(problem,param);
		    	String str = "flatModelRBFVectorC="+C+"G="+param.gamma;
		    	reportTuneData(clutchModel, str, fp);
//		    	tune(clutchModel, clutchTune, neg, false);
	    }
	  }
	  fp.close();
	    
	    for(int i = clutchLen; i < clutchLen + flatLen; i++){
	    	problem.y[i] = 0;
	    }
	    for(int i = clutchLen + flatLen; i < clutchLen + flatLen + hoboLen; i++){
	    	problem.y[i] = 1;
	    }
	    
//	    svm_model hoboModel = null;//svm.svm_train(problem, param);
	    fp = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("HoboModelVectorRBFAttempt2.csv")));
	    for(int G = 1; G < 1000; G*= 10){
	    	for(int C = 1; C < 101; C*= 10){
		    	param.C = C;
		    	param.gamma = ((double) G/(20000*numPics));
		    	//param.gamma = gamma;
		    	clutchModel = svm.svm_train(problem,param);
		    	String str = "hoboModelRBFVectorC="+C+"G="+param.gamma;
		    	reportTuneData(clutchModel, str, fp);
//		    	tune(clutchModel, clutchTune, neg, false);
	    }
	  }
	  fp.close();
	    
	    
	    for(int i = clutchLen + flatLen; i < clutchLen + flatLen + hoboLen; i++){
	    	problem.y[i] = 0;
	    }
	    for(int i = clutchLen + flatLen + hoboLen; i < numPics; i++){
	    	problem.y[i] = 1;
	    }
	    
//	    svm_model pumpModel = null;//svm.svm_train(problem, param);
	    fp = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("PumpModelVectorRBFAttempt2.csv")));
	    for(int G = 1; G < 1000; G*= 10){
	    	for(int C = 1; C < 101; C*= 10){
		    	param.C = C;
		    	param.gamma = ((double) G/(20000*numPics));
		    	//param.gamma = gamma;
		    	clutchModel = svm.svm_train(problem,param);
		    	String str = "pumpModelRBFVectorC="+C+"G="+param.gamma;
		    	reportTuneData(clutchModel, str, fp);
//		    	tune(clutchModel, clutchTune, neg, false);
	    }
	  }
	  fp.close();
	    
	    
	  
	  
	  
	  
	  
	  
//	  
//	  
//	  
//	  
//	  
//	  
//	  
//	  
//	  
//	  
//	  
	  
	    idx = 0;
//	    svm_node[][] picturesNodes = new svm_node[numPics][]; 
	    for(File dir : dirArray){
	    	File[] fileArray = dir.listFiles();
	    	for(int i = 0; i < fileArray.length; i++){
	    		img = ImageIO.read(fileArray[i]);
//				int[] vec = makeVector(img);
				float[] vec = makeHistogram(img);
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
	    
	    fp = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("FlatModelHistogramRBFAttempt2.csv")));
	    for(int G = 1; G < 1000; G*= 10){
	    	for(int C = 1; C < 101; C*= 10){
		    	param.C = C;
		    	param.gamma = ((double) G/(20000*numPics));
		    	//param.gamma = gamma;
		    	clutchModel = svm.svm_train(problem,param);
		    	String str = "flatModelRBFHistogramC="+C+"G="+param.gamma;
		    	reportTuneData(clutchModel, str, fp);
//		    	tune(clutchModel, clutchTune, neg, false);
	    }
	  }
	  fp.close();
	    
	    for(int i = clutchLen; i < clutchLen + flatLen; i++){
	    	problem.y[i] = 0;
	    }
	    for(int i = clutchLen + flatLen; i < clutchLen + flatLen + hoboLen; i++){
	    	problem.y[i] = 1;
	    }
	    
//	    svm_model hoboModel = null;//svm.svm_train(problem, param);
	    fp = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("HoboModelHistogramRBFAttempt2.csv")));
	    for(int G = 1; G < 1000; G*= 10){
	    	for(int C = 1; C < 101; C*= 10){
		    	param.C = C;
		    	param.gamma = ((double) G/(20000*numPics));
		    	//param.gamma = gamma;
		    	clutchModel = svm.svm_train(problem,param);
		    	String str = "hoboModelRBFHistogramC="+C+"G="+param.gamma;
		    	reportTuneData(clutchModel, str, fp);
//		    	tune(clutchModel, clutchTune, neg, false);
	    }
	  }
	  fp.close();
	    
	    
	    for(int i = clutchLen + flatLen; i < clutchLen + flatLen + hoboLen; i++){
	    	problem.y[i] = 0;
	    }
	    for(int i = clutchLen + flatLen + hoboLen; i < numPics; i++){
	    	problem.y[i] = 1;
	    }
	    
//	    svm_model pumpModel = null;//svm.svm_train(problem, param);
	    fp = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("PumpModelHistogramRBFAttempt2.csv")));
	    for(int G = 1; G < 1000; G*= 10){
	    	for(int C = 1; C < 101; C*= 10){
		    	param.C = C;
		    	param.gamma = ((double) G/(20000*numPics));
		    	//param.gamma = gamma;
		    	clutchModel = svm.svm_train(problem,param);
		    	String str = "pumpModelRBFHistogramC="+C+"G="+param.gamma;
		    	reportTuneData(clutchModel, str, fp);
//		    	tune(clutchModel, clutchTune, neg, false);
	    }
	  }
	  fp.close();
	  
	    
	    
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
	
	public static int getMaxIdx(double a, double b, double c, double d){
		if(a > b && a > c && a > d){
			return 0;
		}
		else if(b > c && b > d){
			return 1;
		}
		else if(c > d){
			return 2;
		}
		else{
			return 3;
		}
	}
	
//	public static int tuneModel(svm_problem prob, svm_parameter param, File positiveDir, File[] negitiveDir){

//	}
	
	
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