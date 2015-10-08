package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import main.Space.SpaceValue;
/**
 * Forest is represented as 2-D Space array
 * @author skuroda
 *
 */
public class Forest {
	
	private int numberOfFriends, dimension, numberOfTrees;
	private Space[][] forestGrid;
	private ArrayList<Space> treeLocations;// = new ArrayList<Space>();
	private ArrayList<Space> friendLocations;// = new ArrayList<Space>();
	
	public Forest(String fileName) throws FileNotFoundException{
		Scanner input = new Scanner(new File(fileName));
		//First integer of input is number of friends and dimensions of forest
		numberOfFriends = dimension = input.nextInt();
		//Second integer of input is number of trees in forest
		numberOfTrees = input.nextInt();
		
		//Outputs number of friends and trees and the size of the forest
//		System.out.println("Number of friends: " + numberOfFriends);
//		System.out.println("Forest dimensions: " + dimension + " x " + dimension);
//		System.out.println("Number of trees: "+ numberOfTrees);
		
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
	public void initializeForest(Scanner input){
		forestGrid = new Space[dimension][dimension];
		markAllSpacesEmpty();
		markTreeSpaces(input);
		randomlyPlaceFriends();
//		printForestGrid();
		//Calculate initial conflicts count for each friend
		for(int i = 0; i < dimension; i++){
			friendLocations.get(i).getConflictCount(true);
		}
//		for(Space f: friendLocations){
//			System.out.println((f.getRowNumber() + 1) + " " + (f.getColumnNumber() + 1));
//		}
	}
	
	/**
	 * Initializes all spaces to empty value ' '
	 */
	private void markAllSpacesEmpty(){
		for (int rowNumber = 0; rowNumber < dimension; rowNumber++) {
			for (int columnNumber = 0; columnNumber < dimension; columnNumber++) {
				forestGrid[rowNumber][columnNumber] = new Space(rowNumber, columnNumber, SpaceValue.EMPTY, this);
			}
		}
	}
	
	/**
	 * Marks tree spaces with 'T'
	 * Stores spaces in treeLocations ArrayList
	 * @param input Reads in input file. Null if reinitializing forest grid
	 * 			Replace trees using treeLocations ArrayList
	 */
	private void markTreeSpaces(Scanner input){
		if (input != null) {
			treeLocations = new ArrayList<Space>();
			for (int i = 0; i < numberOfTrees; i++) {
				int rowNumber = input.nextInt() - 1;
				int columnNumber = input.nextInt() - 1;
				forestGrid[rowNumber][columnNumber].setValue(SpaceValue.TREE);
				treeLocations.add(forestGrid[rowNumber][columnNumber]);
			}
		}
		else{
			for(int i = 0; i < numberOfTrees; i++){
				Space nextLocation = treeLocations.get(i); 
				forestGrid[nextLocation.getRowNumber()][nextLocation.getColumnNumber()].setValue(SpaceValue.TREE);
				treeLocations.set(i, forestGrid[nextLocation.getRowNumber()][nextLocation.getColumnNumber()]);
			}
		}
	}
	
	/**
	 * Marks friend spaces with 'F'
	 * Places friends in random empty spaces.
	 * One friend per column.
	 * Stores spaces in friendLocations ArrayList
	 */
	private void randomlyPlaceFriends(){
		friendLocations = new ArrayList<Space>();
		for(int i = 0; i < numberOfFriends; i++){
			int columnNumber = i;
			int rowNumber = -1;
			//Generate row numbers until an empty space is selected
			while(rowNumber < 0 
					|| !forestGrid[rowNumber][columnNumber].isEmpty()){
				rowNumber = (int) (Math.random() * dimension - 1);
			}
			//Place friend once valid row number is generated
			forestGrid[rowNumber][columnNumber].setValue(SpaceValue.FRIEND);
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
				System.out.print(forestGrid[y][x].getPrintValue());
			}
			System.out.println("*");
		}
		for(int i = 0; i < dimension+2; i++){
			System.out.print("*");
		}
		System.out.println();
	}
	
	public ArrayList<Space> getTreeLocations(){
		return treeLocations;
	}
	
	public ArrayList<Space> getFriendLocations(){
		return friendLocations;
	}
	
	public int getDimension(){
		return dimension;
	}
	
	public Space getSpace(int row, int column){
		return forestGrid[row][column];
	}
}
