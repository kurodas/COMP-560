package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
/**
 * Forest is represented as 2-D Space array
 * @author skuroda
 *
 */
public class Forest {
	
	int numberOfFriends, dimension, numberOfTrees;
	Space[][] forestGrid;
	ArrayList<Space> treeLocations = new ArrayList<Space>();
	ArrayList<Space> friendLocations = new ArrayList<Space>();
	
	public Forest(String fileName) throws FileNotFoundException{
		Scanner input = new Scanner(new File(fileName));
		//First integer of input is number of friends and dimensions of forest
		numberOfFriends = dimension = input.nextInt();
		//Second integer of input is number of trees in forest
		numberOfTrees = input.nextInt();
		
		//Outputs number of friends and trees and the size of the forest
		System.out.println("Number of friends: " + numberOfFriends);
		System.out.println("Dimensions: " + dimension + " x " + dimension);
		System.out.println("Number of trees: "+ numberOfTrees);
		
		initializeForest(input);
	}
	
	/**
	 * Initializes the forest grid
	 * 1. Marks all spaces as empty
	 * 2. Marks locations of trees
	 * 3. Randomly places friends
	 * 4. Prints out forest grid
	 * @param input Reads in input file.
	 */
	private void initializeForest(Scanner input){
		forestGrid = new Space[dimension][dimension];
		markAllSpacesEmpty();
		markTreeSpaces(input);
		randomlyPlaceFriends();
		printForestGrid();
	}
	
	/**
	 * Initializes all spaces to empty value ' '
	 */
	private void markAllSpacesEmpty(){
		for (int rowNumber = 0; rowNumber < dimension; rowNumber++) {
			for (int columnNumber = 0; columnNumber < dimension; columnNumber++) {
				forestGrid[rowNumber][columnNumber] = new Space(rowNumber, columnNumber, ' ');
			}
		}
	}
	
	/**
	 * Marks tree spaces with 'T'
	 * Stores spaces in treeLocations ArrayList
	 * @param input Reads in input file.
	 */
	private void markTreeSpaces(Scanner input){
		for(int i = 0; i < numberOfTrees; i++){
			int rowNumber = input.nextInt() - 1;
			int columnNumber = input.nextInt() - 1;
			forestGrid[rowNumber][columnNumber].setValue('T');
			treeLocations.add(forestGrid[rowNumber][columnNumber]);
		}
	}
	
	/**
	 * Marks friend spaces with 'F'
	 * Places friends in random empty spaces.
	 * One friend per column.
	 * Stores spaces in friendLocations ArrayList
	 */
	private void randomlyPlaceFriends(){
		for(int i = 0; i < numberOfFriends; i++){
			int columnNumber = i;
			int rowNumber = -1;
			//Generate row numbers until an empty space is selected
			while(rowNumber < 0 
					|| !forestGrid[columnNumber][rowNumber].isOpen()){
				rowNumber = (int) (Math.random() * dimension - 1);
			}
			forestGrid[columnNumber][rowNumber].setValue('F');
			friendLocations.add(forestGrid[rowNumber][columnNumber]);
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
				System.out.print(forestGrid[y][x].getValue());
			}
			System.out.println("*");
		}
		for(int i = 0; i < dimension+2; i++){
			System.out.print("*");
		}
	}
}
