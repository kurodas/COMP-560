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
		int[] linearVectorCValues = {clutchLinearVectorC, flatLinearVectorC, hoboLinearVectorC, pumpLinearVectorC};
		double[] zeroGammas = {0,0,0,0};
//		System.out.println("Linear Vector");
//		TestModels.test(linearVectorCValues, zeroGammas, svm_parameter.LINEAR, false);
		int[] linearHistogramCValues = {clutchLinearHistogramC, flatLinearHistogramC, hoboLinearHistogramC, pumpLinearHistogramC};
		System.out.println("Linear Histogram");
//		TestModels.test(linearHistogramCValues, zeroGammas, svm_parameter.LINEAR, true);
//		int[] RBFVectorCValues = {clutchRBFVectorC, flatRBFVectorC, hoboRBFVectorC, pumpRBFVectorC};
//		double[] RBFVectorGammaValues = {clutchRBFVectorGamma, flatRBFVectorGamma, hoboRBFVectorGamma, pumpRBFVectorGamma};
//		System.out.println("RBF Vector");
//		TestModels.test(RBFVectorCValues, RBFVectorGammaValues, svm_parameter.RBF, false);
//	
		int[] RBFHistogramCValues = {clutchRBFHistogramC, flatRBFHistogramC, hoboRBFHistogramC, pumpRBFHistogramC};
		double[] RBFHistogramGammaValues = {clutchRBFHistogramGamma, flatRBFHistogramGamma, hoboRBFHistogramGamma, pumpRBFHistogramGamma};
		System.out.println("RBF Histogram");
		TestModels.test(RBFHistogramCValues, RBFHistogramGammaValues, svm_parameter.RBF, true);
	
		
		
	}    
	
	
	
	public static svm_node[] makeArrayOfNodes(float[] vec) {
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
/*	
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
	*/
		
	
	
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