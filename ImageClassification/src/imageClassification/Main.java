package imageClassification;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import libsvm.svm;
import libsvm.svm_node;
import libsvm.svm_parameter;
import libsvm.svm_problem;

import org.imgscalr.Scalr;

public class Main {

	public static void main(String[] args) {
		BufferedImage img = null;
		try {
			File f = new File("images/trainingSet/img_bags_clutch_272.jpg");
		    img = ImageIO.read(f);
		} catch (IOException e) {
			System.out.println("Error");
		}	
		System.out.println("Hi");
		int numberOfPictures = 0;
		//int[][] vec = new int[numberOfPictures][30]; 
				//makeVector(img);
		System.out.println(vec.toString());
		float[] histogram = makeHistogram(img);
		System.out.println(histogram.toString());
		svm_parameter param = new svm_parameter();
		param.kernel_type = svm_parameter.LINEAR;//Linear kernel
		param.svm_type = svm_parameter.ONE_CLASS;
		param.C = 1;
//		svm.train();
		
		svm_problem problem = new svm_problem();
//		problem.
		for(int i = 0; i < numberOfPictures; i++){
			int[] vec = makeVector(img[i]);
			problem.x[i] = new svm_node[3072];
			problem.y[i] = 1;//1 if nodes in i are in class, 0 if nodes are not in class
			for(int j = 0; j < 3072; j++){
				svm_node n = new svm_node();
				n.index = j+1;
				n.value = vec[i][j];
				problem.x[i][j] = n;
			}
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


