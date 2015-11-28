package imageClassification;

import java.awt.image.BufferedImage;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;

public class TuneModels {

    static File clutchTune = new File("images/trainingSet/clutchBags/tuningSet");
    static File flatTune = new File("images/trainingSet/flatShoes/tuningSet");
    static File pumpTune = new File("images/trainingSet/pumpShoes/tuningSet");
    static File hoboTune = new File("images/trainingSet/hoboBags/tuningSet");

	
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

	
	private static float[] tune(svm_model model, File postiveDir, File[] negitiveDir, boolean isHistogram) throws IOException{
		float[] retArr = new float[2];
		int correctCount = 0;
		for(File imgFile : postiveDir.listFiles()){
			BufferedImage tmpImg = ImageIO.read(imgFile);
			svm_node[] nodes;
			if(isHistogram){
				float[] vec = Main.makeHistogram(tmpImg);
				nodes = Main.makeArrayOfNodes(vec);	
			}
			else{
				int[] vec = Main.makeVector(tmpImg);
				nodes = Main.makeArrayOfNodes(vec);
			}
			
			double[] prob_estimates = new double[2];
		    correctCount+= svm.svm_predict_probability(model, nodes, prob_estimates);
		}
		
		int incorrectNegCount = 0;
		int totalNeg = 0;
		for(File negDir : negitiveDir){
			totalNeg += negDir.listFiles().length;
			for(File imgFile : negDir.listFiles()){
				BufferedImage tmpImg = ImageIO.read(imgFile);
				svm_node[] nodes;
				if(isHistogram){
					float[] vec = Main.makeHistogram(tmpImg);
					nodes = Main.makeArrayOfNodes(vec);	
				}
				else{
					int[] vec = Main.makeVector(tmpImg);
					nodes = Main.makeArrayOfNodes(vec);
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
	
}
