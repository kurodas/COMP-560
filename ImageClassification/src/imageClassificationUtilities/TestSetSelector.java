package imageClassificationUtilities;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
/**
 * @author skuroda
 *
 */
public class TestSetSelector {
	public static void main(String[] args){
		File imageDirectory = new File("images");
		if(imageDirectory.isDirectory()){
			resetSets(imageDirectory);
			int numberOfImages = imageDirectory.listFiles().length;
			ArrayList<File> images = new ArrayList<File>(numberOfImages);
			for(File image: imageDirectory.listFiles()){
				images.add(image);
			}
			//Randomize the order of the images
			Collections.shuffle(images);
			int trainingSetSize = (int) (numberOfImages * 0.7);
			
			for(int i = 0; i<numberOfImages; i++){
				//Ignore directories
				if(!images.get(i).isDirectory()){
					//Move the first tuningSetSize images to the training set
					File currentImage = images.get(i);
					String imageClass = getImageClass(currentImage.getName());
					//Move first images to the training set
					if(i < trainingSetSize){
						currentImage.renameTo(new File("images/trainingSet/"+ imageClass + "/" + currentImage.getName()));
					}
					//Move the remaining images to the test set
					else{
						currentImage.renameTo(new File("images/testSet/" + imageClass + "/" + currentImage.getName()));
					}
				}
			}
			File trainingDirectory = new File("images/trainingSet/");
			File[] imageClassDirectories = trainingDirectory.listFiles();
			for(int i = 0; i < imageClassDirectories.length; i++){
				splitImageClass(imageClassDirectories[i]);
			}
		}
	}
	
	private static String getImageClass(String imageName){
		if(imageName.contains("clutch")){
			return "clutchBags";
		}
		else if(imageName.contains("flat")){
			return "flatShoes";
		}
		else if(imageName.contains("hobo")){
			return "hoboBags";
		}
		else if(imageName.contains("pump")){
			return "pumpShoes";
		}
		return null;
	}
	
	private static void resetSets(File directory){
		File[] filesInDirectory = directory.listFiles();
		for(int i = 0; i < filesInDirectory.length; i++){
			File currentFile = filesInDirectory[i];
			if(!currentFile.isDirectory()){
				currentFile.renameTo(new File("images/" + currentFile.getName()));
			}
			else{
				resetSets(currentFile);
			}
		}
	}
	
	private static void splitImageClass(File classDirectory){
		File[] classImages = classDirectory.listFiles();
		String className = getImageClass(classDirectory.getName());
		for(int i = 0; i < classImages.length; i++){
			File currentImage = classImages[i];
			//Ignore directories
			if(!currentImage.isDirectory()){
				if(i<classImages.length * 0.5){
					currentImage.renameTo(new File("images/trainingSet/"+ className+"/tuningSet/"+currentImage.getName()));
				}
				else{
					currentImage.renameTo(new File("images/trainingSet/"+ className+"/trainingSet/"+currentImage.getName()));
				}
			}
		}
	}
}
