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
//		File[] dirArray = {clutchDir, flatDir, hoboDir, pumpDir};
		int numPics = clutchLen + flatLen + hoboLen + pumpLen;
		
		
	    problem.y = new double[numPics];
	    problem.l = numPics;
	    problem.x = new svm_node[numPics][];     
		
	    int idx = 0;
	    for(File dir : dirArray){
	    	File[] fileArray = dir.listFiles();
	    	for(int i = 0; i < fileArray.length; i++){
	    		img = ImageIO.read(fileArray[i]);
//				int[] vec = makeVector(img);
////				float[] vec = makeHistogram(img);
//				problem.x[i+idx] = makeArrayOfNodes(vec);
	    	}
	    	idx += fileArray.length;
	    }
	    for(int i = 0; i < numPics; i++){
	    	problem.y[i] = 0;
	    }
	    
	    
//	    for(int i = flatLen; i < flatLen+clutchLen; i++){
//	    	problem.y[i] = 1;
//	    }
	    
	    svm_model clutchModel = null;
	
	    param.gamma = ((double) 2/numPics);

	    

	    System.out.println(param.gamma);
	    
	    
	    File vecDir = new File("vectorModels");
	    File clutchTune = new File("images/trainingSet/clutchBags/tuningSet");
	    File flatTune = new File("images/trainingSet/flatShoes/tuningSet");
	    File pumpTune = new File("images/trainingSet/pumpShoes/tuningSet");
	    File hoboTune = new File("images/trainingSet/hoboBags/tuningSet");
    	File[] neg = {flatTune, pumpTune, hoboTune};
    	
//	    for(double C = .01; C < 1000; C*= 10){
//	    	param.C = C;
////	    	param.gamma = gamma;
//	    	clutchModel = svm.svm_train(problem,param);
//	    	String str = "clutchModelRBFVectorC="+C+"G="+param.gamma;
//	    	svm.svm_save_model(str, clutchModel);
//	    	tune(clutchModel, clutchTune, neg, false);

//	    }

    	
    	
//     	a = svm.svm_load_model("clutchModelRBFVectorC=0.1G=7.251631617113851E-4");
//    	tune(a, clutchTune, neg, false);
//    	a = svm.svm_load_model("clutchModelRBFVectorC=1.0G=7.251631617113851E-4");
//    	tune(a, clutchTune, neg, false);
//    	a = svm.svm_load_model("clutchModelRBFVectorC=10.0G=7.251631617113851E-4");
//    	tune(a, clutchTune, neg, false);
//    	a = svm.svm_load_model("clutchModelRBFVectorC=100.0G=7.251631617113851E-4");
//    	tune(a, clutchTune, neg, false);


    	File clutch = new File("RBFVector/Clutch");
    	
    	neg[0] = flatTune;
    	neg[1] = pumpTune;
    	neg[2] = hoboTune;
    	
    	DataOutputStream fp = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("ClutchModelVectorRBF.csv")));
	    for(File model : clutch.listFiles()){
	    	String modelName = model.getName();
	    	svm_model cModel = svm.svm_load_model(model.getCanonicalPath());
	    	String modType = modelName.contains("Linear") ? "Linear" : "RBF";
	    	String vecType = modelName.contains("Histogram") ? "Histogram" : "Vector";
	    	int Cidx = modelName.indexOf("C=");
	    	int Gidx = modelName.indexOf("G=");
	    	float[] a = tune(cModel, clutchTune, neg, modelName.contains("Histogram"));
	    	if(Gidx < 0){
	    		String C = modelName.substring(Cidx + 2, modelName.length());
	    		String str = "Clutch,Linear/RBF," + modType + ",Histogram/Vector," + vecType + ",C,"+ C +",G,NULL,PositveTune," + a[0] + ",NegitiveTunePostive," + a[1]  + "\n";
	    		System.out.print(str);
	    		fp.writeBytes(str);
	    	}
	    	else{
	    		String C = modelName.substring(Cidx + 2, Gidx);
		    	String G = modelName.substring(Gidx + 2);
		    	String str = "Clutch,Linear/RBF," + modType + ",Histogram/Vector," + vecType + ",C,"+ C +",G,"+G+",PositveTune," + a[0] + ",NegitiveTunePostive," + a[1]  + "\n";
		    	System.out.print(str);
	    		fp.writeBytes(str);
	    	}
	    	fp.flush();
	    }
    	fp.close();
    	
    	File pump = new File("RBFVector/pump");
//    	File pump = new File("pumpModels");
    	neg[0] = clutchTune;
    	neg[1] = flatTune;
    	neg[2] = hoboTune;
    	
    	fp = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("PumpModelVectorRBF.csv")));
	    for(File model : pump.listFiles()){
	    	String modelName = model.getName();
	    	svm_model cModel = svm.svm_load_model(model.getCanonicalPath());
	    	String modType = modelName.contains("Linear") ? "Linear" : "RBF";
	    	String vecType = modelName.contains("Histogram") ? "Histogram" : "Vector";
	    	int Cidx = modelName.indexOf("C=");
	    	int Gidx = modelName.indexOf("G=");
	    	float[] a = tune(cModel, pumpTune, neg, modelName.contains("Histogram"));
	    	if(Gidx < 0){
	    		String C = modelName.substring(Cidx + 2, modelName.length());
	    		String str = "Pump,Linear/RBF," + modType + ",Histogram/Vector," + vecType + ",C,"+ C +",G,NULL,PositveTune," + a[0] + ",NegitiveTunePostive," + a[1]  + "\n";
	    		System.out.print(str);
	    		fp.writeBytes(str);
	    	}
	    	else{
	    		String C = modelName.substring(Cidx + 2, Gidx);
		    	String G = modelName.substring(Gidx + 2);
		    	String str = "Pump,Linear/RBF," + modType + ",Histogram/Vector," + vecType + ",C,"+ C +",G,"+G+",PositveTune," + a[0] + ",NegitiveTunePostive," + a[1]  + "\n";
		    	System.out.print(str);
	    		fp.writeBytes(str);
	    	}
	    	fp.flush();
	    }
    	fp.close();
    	
    	
    	File hobo = new File("RBFVector/hobo");
//    	File hobo = new File("hoboModels");
    	neg[0] = clutchTune;
    	neg[1] = flatTune;
    	neg[2] = pumpTune;
    	
    	fp = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("HoboModelVectorRBF.csv")));
	    for(File model : hobo.listFiles()){
	    	String modelName = model.getName();
	    	svm_model cModel = svm.svm_load_model(model.getCanonicalPath());
	    	String modType = modelName.contains("Linear") ? "Linear" : "RBF";
	    	String vecType = modelName.contains("Histogram") ? "Histogram" : "Vector";
	    	int Cidx = modelName.indexOf("C=");
	    	int Gidx = modelName.indexOf("G=");
	    	float[] a = tune(cModel, hoboTune, neg, modelName.contains("Histogram"));
	    	if(Gidx < 0){
	    		String C = modelName.substring(Cidx + 2, modelName.length());
	    		String str = "Hobo,Linear/RBF," + modType + ",Histogram/Vector," + vecType + ",C,"+ C +",G,NULL,PositveTune," + a[0] + ",NegitiveTunePostive," + a[1]  + "\n";
	    		System.out.print(str);
	    		fp.writeBytes(str);
	    	}
	    	else{
	    		String C = modelName.substring(Cidx + 2, Gidx);
		    	String G = modelName.substring(Gidx + 2);
		    	String str = "Hobo,Linear/RBF," + modType + ",Histogram/Vector," + vecType + ",C,"+ C +",G,"+G+",PositveTune," + a[0] + ",NegitiveTunePostive," + a[1]  + "\n";
		    	System.out.print(str);
	    		fp.writeBytes(str);
	    	}
	    	fp.flush();
	    }
    	fp.close();
    	
    	
    	
//	    idx = 0;
////	    svm_node[][] picturesNodes = new svm_node[numPics][]; 
//	    for(File dir : dirArray){
//	    	File[] fileArray = dir.listFiles();
//	    	for(int i = 0; i < fileArray.length; i++){
//	    		img = ImageIO.read(fileArray[i]);
//				int[] vec = makeVector(img);
////				float[] vec = makeHistogram(img);
//				problem.x[i+idx] = makeArrayOfNodes(vec);
//	    	}
//	    	idx += fileArray.length;
//	    }


	    
	    
	    for(int i = 0; i < clutchLen; i++){
	    	problem.y[i] = 0;
	    }
	    for(int i = clutchLen; i < clutchLen + flatLen; i++){
	    	problem.y[i] = 1;
	    }
	    
	    svm_model flatModel = null;//svm.svm_train(problem, param);
	    
//	    for(double gamma = 0.01; gamma < 10000; gamma*= 10){
//	    	for(int C = 1; C < 11; C++){
//	    		param.C = C;
//	    		param.gamma = gamma;
//	    		flatModel = svm.svm_train(problem,param);
//	    		String str = "RBFVector/Flat/flatModelRBFVectorC="+C+"G="+gamma;
//	    		svm.svm_save_model(str, flatModel);
//	    	}
//	    }
	    
	    for(int i = clutchLen; i < clutchLen + flatLen; i++){
	    	problem.y[i] = 0;
	    }
	    for(int i = clutchLen + flatLen; i < clutchLen + flatLen + hoboLen; i++){
	    	problem.y[i] = 1;
	    }
	    svm_model hoboModel = null;//svm.svm_train(problem, param);

//	    for(double gamma = 0.01; gamma < 10000; gamma*= 10){
//	    	for(int C = 1; C < 11; C++){
//	    		param.C = C;
//	    		param.gamma = gamma;
//	    		hoboModel = svm.svm_train(problem,param);
//	    		String str = "RBFVector/Hobo/hoboModelRBFVectorC="+C+"G="+gamma;
//	    		svm.svm_save_model(str, hoboModel);
//	    	}
//	    }
	    
	    for(int i = clutchLen + flatLen; i < clutchLen + flatLen + hoboLen; i++){
	    	problem.y[i] = 0;
	    }
	    for(int i = clutchLen + flatLen + hoboLen; i < numPics; i++){
	    	problem.y[i] = 1;
	    }
	    svm_model pumpModel = null;//svm.svm_train(problem, param);

//	    for(double gamma = 0.01; gamma < 10000; gamma*= 10){
//	    	for(int C = 1; C < 11; C++){
//	    		param.C = C;
//	    		param.gamma = gamma;
//	    		pumpModel = svm.svm_train(problem,param);
//	    		String str = "RBFVector/Pump/pumpModelRBFVectorC="+C+"G="+gamma;
//	    		svm.svm_save_model(str, pumpModel);
//	    	}
//	    }

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