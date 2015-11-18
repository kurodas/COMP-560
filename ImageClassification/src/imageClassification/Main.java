package imageClassification;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import libsvm.svm;
import libsvm.svm_parameter;
import libsvm.svm_problem;

import org.imgscalr.Scalr;

public class Main {

	public static void main(String[] args) {
		BufferedImage img = null;
		try {
			File f = new File("images/img_bags_clutch_272.jpg");
		    img = ImageIO.read(f);
		} catch (IOException e) {
			System.out.println("Error");
		}	
		System.out.println("Hi");
		int[] vec = makeVector(img);
		svm_parameter param = new svm_parameter();
		param.kernel_type = svm_parameter.LINEAR;//Linear kernal
		param.svm_type = svm_parameter.ONE_CLASS;
		param.C = 1;
//		svm.train();
		
		svm_problem problem = new svm_problem();
//		problem.
		
		
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
	
	public static float[][][] makeHistogram(BufferedImage img){
		int RGBBuckets[][][] = new int[8][8][8];
		float RGBFloat[][][] = new float[8][8][8];
		
		for(int x = 0; x < img.getWidth(); x++){
			for(int y = 0; y < img.getHeight(); y++){
				int pix = img.getRGB(x, y);
				int red = getRed(pix)/32;
				int green = getGreen(pix)/32;
				int blue = getBlue(pix)/32;
				RGBBuckets[red][green][blue]++;
			}
		}
		for(int r = 0; r < 8; r++){
			for(int g = 0; g < 8; g++){
				for(int b = 0; b < 8; b++){
					RGBFloat[r][g][b] = ((float)RGBBuckets[r][g][b])/(img.getWidth()*img.getHeight());
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
				int pix = scaledImage.getRGB(x, y);
				int red = getRed(pix);
				int green = getGreen(pix);
				int blue = getBlue(pix);
				vector[y*32 + 3*x] = red;
				vector[y*32 + 3*x + 1] = green;
				vector[y*32 + 3*x + 2] = blue;
			}
		}
		
		
		return vector;
	}

}


