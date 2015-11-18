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
			
			for(int i = 0; i<numberOfImages; i++){
				//Ignore directories
				if(!images.get(i).isDirectory()){
					//Move the first trainingSetSize images to the training set
					if(i < trainingSetSize){
						File currentImage = images.get(i);
						currentImage.renameTo(new File("images/trainingSet/" + currentImage.getName()));
					}
					//Move the remaining images to the test set
					else{
						File currentImage = images.get(i);
						currentImage.renameTo(new File("images/testSet/" + currentImage.getName()));
					}
				}
			}
		}
	}
}
