package imageClassification;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;
import libsvm.svm_parameter;
import libsvm.svm_problem;

import org.imgscalr.Scalr;

public class Main {

	public static void main(String[] args) throws IOException {
		BufferedImage img = null;
		try {
			File f = new File("images/trainingSet/img_bags_clutch_272.jpg");
		    img = ImageIO.read(f);
		} catch (IOException e) {
			System.out.println("Error");
		}
		File testDir = new File("images/testSet");
		File[] testFiles = testDir.listFiles();
		
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
		System.out.println("Hi");
//		int numberOfPictures = 0;
		//int[][] vec = new int[numberOfPictures][30]; 
				//makeVector(img);-
//		System.out.println(vec.toString());
//		float[] histogram = makeHistogram(img);
//		System.out.println(histogram.toString());
		svm_parameter param = new svm_parameter();
		param.kernel_type = svm_parameter.LINEAR;
//		param.svm_type = svm_parameter.ONE_CLASS;
		param.svm_type = svm_parameter.C_SVC;
		param.C = 5;
	    param.probability = 1;
//	    param.gamma = 0.5;
//	    param.nu = 0.5;
//	    param.C = 1;
	    
//	    param.svm_type = svm_parameter.C_SVC;
//	    param.svm_type = svm_parameter.ONE_CLASS;
	    
	    param.kernel_type = svm_parameter.LINEAR;       
	    param.cache_size = 20000;
	    param.eps = 0.001;  
//		svm.train();
		
		svm_problem problem = new svm_problem();
		
		File clutchDir = new File("images/trainingSet/clutchBags");
		int clutchLen = clutchDir.listFiles().length;
		
		File flatDir = new File("images/trainingSet/flatShoes");
		int flatLen = flatDir.listFiles().length;
		
		File hoboDir = new File("images/trainingSet/hoboBags");
		int hoboLen = hoboDir.listFiles().length;
		
		File pumpDir = new File("images/trainingSet/pumpShoes");

		int pumpLen = pumpDir.listFiles().length;
		
		File[] dirArray = {clutchDir, flatDir, hoboDir, pumpDir};
		int numPics = clutchLen + flatLen + hoboLen + pumpLen;
		
		
	    problem.y = new double[numPics];
	    problem.l = numPics;
	    problem.x = new svm_node[numPics][];     
		
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
	    
	    
	    for(int i = 0; i < clutchLen; i++){
	    	problem.y[i] = 1;
	    }
	    svm_model clutchModel = svm.svm_train(problem, param);
	    
	    for(int i = 0; i < clutchLen; i++){
	    	problem.y[i] = 0;
	    }
	    for(int i = clutchLen; i < clutchLen + flatLen; i++){
	    	problem.y[i] = 1;
	    }
	    svm_model flatModel = svm.svm_train(problem, param);
	    
	    for(int i = clutchLen; i < clutchLen + flatLen; i++){
	    	problem.y[i] = 0;
	    }
	    for(int i = clutchLen + flatLen; i < clutchLen + flatLen + hoboLen; i++){
	    	problem.y[i] = 1;
	    }
	    svm_model hoboModel = svm.svm_train(problem, param);
	    
	    for(int i = clutchLen + flatLen; i < clutchLen + flatLen + hoboLen; i++){
	    	problem.y[i] = 0;
	    }
	    for(int i = clutchLen + flatLen + hoboLen; i < numPics; i++){
	    	problem.y[i] = 1;
	    }
	    svm_model pumpModel = svm.svm_train(problem, param);
	    
/*	    for(int i = 0; i < clutchTrainingPics.length; i++){
//			img = ImageIO.read(clutchTrainingPics[i]);
//			int[] vec = makeVector(img);
//			problem.x[i] = new svm_node[3072];
//			problem.y[i] = 1;//1 if nodes in i are in class, 0 if nodes are not in class
//			for(int j = 0; j < 3072; j++){
//				svm_node n = new svm_node();
//				n.index = j+1;
//				n.value = vec[j];
//				problem.x[i][j] = n;
//			}
//		}
	    for(int i = 0; i < flatTrainingPics.length; i++){
	    	img = ImageIO.read(flatTrainingPics[i]);
	    	int[] vec =  makeVector(img);
	    	problem.x[i+clutchTrainingPics.length] = new svm_node[3072];
	    	problem.y[i+clutchTrainingPics.length] = 0;
	    	for(int j = 0; j < 3072; j++){
	    		svm_node n = new svm_node();
	    		n.index = j+1;
	    		n.value = vec[j];
	    		problem.x[i+clutchTrainingPics.length][j] = n;
	    	}
	    }*/
//		clutchModel.param.C = 
//		svm_model model = svm.svm_train(problem, param);

		for(File dir : testFiles){
		for(File f : dir.listFiles()){
			
			System.out.println(f.getName());
			BufferedImage tmpImg = ImageIO.read(f);
			int[] vec = makeVector(tmpImg);
			svm_node[] nodes = makeArrayOfNodes(vec);
			
			double[] prob_estimates = new double[2];
		    svm.svm_predict_probability(clutchModel, nodes, prob_estimates);
		    double clutchProb = prob_estimates[0];
		    svm.svm_predict_probability(flatModel, nodes, prob_estimates);
		    double flatProb = prob_estimates[0];
		    svm.svm_predict_probability(hoboModel, nodes, prob_estimates);
		    double hoboProb = prob_estimates[0];
		    svm.svm_predict_probability(pumpModel, nodes, prob_estimates);
		    double pumpProb = prob_estimates[0];

//		    System.out.println("NEW IMAGE");
		    System.out.println("Clutch Prob " + clutchProb);
		    System.out.println("Flat Prob " + flatProb);
		    System.out.println("Hobo Prob " + hoboProb);
		    System.out.println("Pump Prob " + pumpProb);
		    System.out.println();
		    
			}	
		}
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


