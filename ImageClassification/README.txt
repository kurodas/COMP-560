The ImageClassification java package trains and classifies models from the package libSVM.

In the main package we have the following methods
MakeVector - Which takes as an input a BufferedImage and returns an array of integers of length 3072. The array is a vector representation of a 32X32 red, green, and blue pixel values.
To perform the conversion of the BufferedImage into a 32 X 32 image we use the freely available imgScalr java library.

MakeHistogram - Which takes as an input a BufferedImage and returns an array of floats of length 512. It does this by dividing the rgb values of each pixel by 32 and getting a bucket value
between 0 and 7. It then counts each pixel of each bucket value configuration, and divides this count by the total number of pixels. The array indexes of each float are determined by taking
the red bucket value adding the green bucket * 8 and adding the blue bucket * 64.

The TuneModels.java file expects as an input, a model that you wish to calculate he performance on the tuning set, a String that contains a model "name", with information on which class the model represents
whether the model is a linear or RBF model, if it uses Vector or Histogram to represent images, and information on gamma (if the model is of type RBF). An example name
would be "clutchRBFHistogramModelC=5G=0.7". The final expected input is an open DataOutputStream, that represents a CSV file. 
The code will output to a row in the CSV file information on the model class
if it's linear or RBF, how the model represents the data, it's C and Gamma parameter (Gamma will be NULL if the type of the model is linear), the ratio of true positives/all files that should be positive,
and the ratio of false postivies / all files that should be negitive.

The TestModels.java file expects as an input an array of 4 integers, of the C values, index 0 is the C value of the clutch model, index 1 is the flat model's C Value, index 2 is the hobo model's C Value, and index
3 is the pump model's C value. The next input is the is an array of 4 doubles, representing the gamma values, with the indexing the same as the C values. The next input expects the svm_parameter ENUM whether the models
are of type svm_parameter.LINEAR or svm_parameter.RBF. The final parameter is a boolean indicating whether the model's use histogram as their representation (true indicates use Histograms to represent the images,
false indicates use Vectors to represent the images).
TestModels will output to the console information on performance on the test set, which includes ratio of correctly identified pictures / total pictures of that class,
 false positives for that class / total pictures that are not in the clas, and total correct classifications / total number of images.