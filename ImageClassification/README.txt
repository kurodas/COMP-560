Run the command:
java -jar ImageClassification.jar
The output is the results of running our testing code on the images in the directories images/testSet/*
where * is one of clutchBags, flatShoes, hoboShoes, or pumpShoes.
The output is divided by each combination of kernel type and representation type. The fraction of images correctly and incorrectly classified as each image class is printed along with the overall fraction correctly classified using the parameters that we determined through tuning.

Note: The argument -Xmx3072m may need to be added after “-jar” to prevent a java.lang.OutOfMemoryError exception. This allows the program to use up to 3072 MB of memory.