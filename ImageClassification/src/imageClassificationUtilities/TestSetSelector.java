package imageClassificationUtilities;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
/**
 * Divides image files into test set and training set
 * Images must be restored to "image" directory if
 * redividing for whatever reason.
 * @author skuroda
 *
 */
public class TestSetSelector {
	public static void main(String[] args){
		File imageDirectory = new File("images");
		if(imageDirectory.isDirectory()){
			int numberOfImages = imageDirectory.listFiles().length;
			ArrayList<File> images = new ArrayList<File>(numberOfImages);
			for(File image: imageDirectory.listFiles()){
				images.add(image);
			}
			//Randomize the order of the images
			Collections.shuffle(images);
			int trainingSetSize = (int) (numberOfImages * 0.7);
			int tuningSetSize = (int) (trainingSetSize * 0.5);
			
			for(int i = 0; i<numberOfImages; i++){
				//Ignore directories
				if(!images.get(i).isDirectory()){
					//Move the first tuningSetSize images to the training set
					File currentImage = images.get(i);
					String imageClass = getImageClass(currentImage.getName());
					if(i < tuningSetSize){
						currentImage.renameTo(new File("images/tuningSet/"+ imageClass + "/" + currentImage.getName()));
					}
					//Move next tuningSetSize images to the training set
					else if(i < trainingSetSize){
						currentImage.renameTo(new File("images/trainingSet/"+ imageClass + "/" + currentImage.getName()));
					}
					//Move the remaining images to the test set
					else{
						currentImage.renameTo(new File("images/testSet/" + imageClass + "/" + currentImage.getName()));
					}
				}
			}
		}
	}
	
	private static String getImageClass(String imageName){
		if(imageName.contains("clutch")){
			return "clutchBags";
		}
		else if(imageName.contains("flats")){
			return "flatShoes";
		}
		else if(imageName.contains("hobo")){
			return "hoboBags";
		}
		else if(imageName.contains("pumps")){
			return "pumpShoes";
		}
		return null;
	}
}
