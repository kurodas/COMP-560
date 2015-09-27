package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
/**
 * Forest is represented as 2-D array of chars
 * ' ' represents empty space
 * 't' represents tree space
 * 'f' represents friend space
 * @author skuroda
 *
 */
public class Forest {
	
	int numberOfFriends, dimension, numberOfTrees;
	char[][] forestGrid;
	
	public Forest(String fileName) throws FileNotFoundException{
		Scanner input = new Scanner(new File(fileName));
		//First integer is number of friends and dimensions of forest
		numberOfFriends = dimension = input.nextInt();
		forestGrid = new char[dimension][dimension];
		System.out.println("Number of friends: " + numberOfFriends);
		System.out.println("Dimensions: " + dimension + " x " + dimension);
		numberOfTrees = input.nextInt();
		System.out.println("Number of trees: "+ numberOfTrees);
		initializeForest(input);
	}
	/**
	 * Initializes the forest grid
	 * 1. Marks all spaces as empty
	 * 2. Marks locations of trees
	 * 3. Randomly places friends
	 * 4. Prints out forest grid
	 * @param input
	 */
	private void initializeForest(Scanner input){
		markAllSpacesEmpty();
		markTreeSpaces(input);
		randomlyPlaceFriends();
		printForestGrid();
	}
	/**
	 * Test to see if the space is open
	 * @param columnNumber
	 * @param rowNumber
	 * @return true if space is not occupied by tree or friend
	 *         false otherwise
	 */
	private boolean isOpen(int columnNumber, int rowNumber){
		return forestGrid[columnNumber][rowNumber] == ' ';	
	}
	
	/**
	 * Initializes all spaces to empty value ' '
	 */
	private void markAllSpacesEmpty(){
		for (int y = 0; y < dimension; y++) {
			for (int x = 0; x < dimension; x++) {
				forestGrid[y][x] = ' ';
			}
		}
	}
	/**
	 * Marks tree spaces with 't'
	 * @param input Reads in input file.
	 */
	private void markTreeSpaces(Scanner input){
		for(int i = 0; i < numberOfTrees; i++){
			int treeRowNumber = input.nextInt() - 1;
			int treeColumnNumber = input.nextInt() - 1;
			forestGrid[treeRowNumber][treeColumnNumber] = 'T';
		}
	}
	/**
	 * Randomly places friends in empty spaces.
	 */
	private void randomlyPlaceFriends(){
		for(int i = 0; i < numberOfFriends; i++){
			int columnNumber = -1;
			int rowNumber = -1;
			//Generate random column and row numbers until an empty space is selected
			while(columnNumber < 0 || rowNumber < 0 
					|| !isOpen(columnNumber, rowNumber)){
				columnNumber = (int) (Math.random() * dimension - 1);
				rowNumber = (int) (Math.random() * dimension - 1);
			}
			forestGrid[columnNumber][rowNumber] = 'F';
		}
	}
	
	/**
	 * Prints out forest grid surrounded by '*'
	 */
	public void printForestGrid(){
		for(int i = 0; i < dimension+2; i++){
			System.out.print("*");
		}
		System.out.println();
		for(int y = 0; y < dimension; y++){
			System.out.print("*");
			for(int x = 0; x < dimension; x++){
				System.out.print(forestGrid[y][x]);
			}
			System.out.println("*");
		}
		for(int i = 0; i < dimension+2; i++){
			System.out.print("*");
		}
	}
}
