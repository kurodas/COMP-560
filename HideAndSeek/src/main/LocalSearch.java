package main;

import java.util.Vector;

import main.Space.SpaceValue;
/**
 * Runs local search on forest to generate 
 * a consistent placement of friends.
 * "order" parameter specifies order 
 * in which friends are moved 
 * @author skuroda
 *
 */
public class LocalSearch {
	Forest forest;
	
	public LocalSearch(Forest f, String order){
		forest = f;
		//Initialize the numberOfRemainingConflictsRemaining to -1 to enter loop
		int numberOfConflictsRemaining = -1;
		
		//Tracks number of iterations in order to know when to reset the forest
		int iterationCount = 0;
		
		//Tracks number of reset to know when to give up
		int resetCount = 0;
		
		// Move friends around while there are conflicts
		while(numberOfConflictsRemaining != 0 && resetCount < 1000){
			//Change processing order depending on command line argument
			if(order.equalsIgnoreCase("O")){
				numberOfConflictsRemaining = processLeftToRight();
			}
			else if(order.equalsIgnoreCase("M")){
				numberOfConflictsRemaining = processMostConflictedFirst();
			}
			else
				numberOfConflictsRemaining = processRandomly();
			
			iterationCount++;
			//Reset the forest after 100 iterations without solution
			//Placing the friends in new random locations
			if(numberOfConflictsRemaining > 0 && iterationCount > 100){
				System.out.println("Reinitializing forest");
				forest.initializeForest(null);
				iterationCount = 0;
				resetCount++;
			}
		}
		if(resetCount > 100){
			System.out.println("No solution found after 100 resets");
		}
		System.out.println("Iteration count: " + iterationCount);
		
	}
	//Removes any friends in spaces in column #columnNum
	private void clearColumn(int columnNum){
		for (int rowIndex = 0; rowIndex < forest.getDimension(); rowIndex++) {
			if (!forest.getSpace(rowIndex, columnNum).hasTree()) {
				forest.getSpace(rowIndex, columnNum).setValue(
						SpaceValue.EMPTY);
			}
		}
	}
	/**
	 * Checks whether moving the friend to currentSpace, if possible,
	 * causes fewer conflicts than a previously seen assignment
	 * @param currentSpace
	 * @param minConflictCount The smallest number of conflicts seen so far from an assignment
	 * @return
	 */
	private boolean currentSpaceCausesFewerConflicts(Space currentSpace, int minConflictCount){
		//If friend can be in this space
		if (!currentSpace.hasTree()) {
			// Remove any existing friends from the column that currentSpace is in.
			clearColumn(currentSpace.getColumnNumber());
			
			// Try moving the friend to space at (columnNum,rowNum)
			currentSpace.setValue(SpaceValue.FRIEND);
			
			// If this space causes fewer conflicts, return true
			return (currentSpace.getConflictCount(true) < minConflictCount);
		}
		// Current space has tree and cannot be assigned to
		else
			return false;
	}
	/**
	 * Attempts to move the friend in column #columnIndex to a less conflicting row.
	 * @param columnIndex 
	 * @return the number of remaining conflicts from the chosen assignment
	 */
	private int moveWithinColumn(int columnIndex){
		Space currentFriendSpace = forest.getFriendLocations().get(columnIndex);
		//If friend location in current column causes conflicts, 
		if(currentFriendSpace.getConflictCount(true) != 0){
			//Initialize minConflictCount to be larger than any possible value
			int minConflictCount = Integer.MAX_VALUE;
			
			// Initialize possible row to move friend to 
			// as row of the friend currently in this column
			int currentCandidateSpaceRowNum = currentFriendSpace.getRowNumber();
			
			// Check the number of conflicts that moving 
			// to each space in this column would cause 
			for(int rowIndex = 0; rowIndex < forest.getDimension(); rowIndex++){
				Space currentSpace = forest.getSpace(rowIndex, columnIndex);
				
				//Check if currentSpace would cause fewer conflicts
				if(currentSpaceCausesFewerConflicts(currentSpace, minConflictCount)){
					minConflictCount = currentSpace.getConflictCount(false);
					currentCandidateSpaceRowNum = rowIndex;
				}
			}
			//Make sure that no friends are still placed  
			//in this column from the search process
			clearColumn(columnIndex);
			
			//Set space to move to as space in row #currentCandidateSpaceRowNum
			Space spaceToMoveTo = forest.getSpace(currentCandidateSpaceRowNum, columnIndex);
			
			//Update friendLocations ArrayList
			forest.getFriendLocations().set(columnIndex, spaceToMoveTo);
			
			//Place friend in space
			spaceToMoveTo.setValue(SpaceValue.FRIEND);
			
			//Increment conflict counter by number of conflicts caused by new assignment
			return spaceToMoveTo.getConflictCount(false);
		}
		//The current friend placement in this column causes 0 conflicts
		else
			return 0;
	}
	
	
	// Reassigns students within columns in order from the 
	// leftmost column to the rightmost column 
	private int processLeftToRight(){
		int numberOfConflictsRemaining = 0;
		for(int columnIndex = 0; columnIndex < forest.getDimension(); columnIndex++){
			numberOfConflictsRemaining += moveWithinColumn(columnIndex);
		}
		return numberOfConflictsRemaining;
	}
	
	private int processMostConflictedFirst(){
		int numberOfConflictsRemaining = 0;
		int mostConflictedColumnNum = -1;
		//Stores indices of columns left to reassign
		Vector<Integer> columnsLeftToProcess = new Vector<Integer>();
		
		for(int i = 0; i < forest.getDimension(); i++){
			columnsLeftToProcess.add(new Integer(i));
		}
		while (!columnsLeftToProcess.isEmpty()) {
			int maxConflictCount = Integer.MIN_VALUE;
			mostConflictedColumnNum = -1;
			for (Integer i : columnsLeftToProcess) {
				Space friendSpaceInColumnI = forest.getFriendLocations().get(i.intValue());
				if (friendSpaceInColumnI.getConflictCount(true) > maxConflictCount) {
					maxConflictCount = friendSpaceInColumnI
							.getConflictCount(true);
					mostConflictedColumnNum = i.intValue();
				}
			}
			numberOfConflictsRemaining += moveWithinColumn(mostConflictedColumnNum);
			columnsLeftToProcess.remove(new Integer(mostConflictedColumnNum));
		}
		return numberOfConflictsRemaining;
	}
	
	private int processRandomly(){
		int numberOfConflictsRemaining = 0;
		//Stores indices of columns left to reassign
		Vector<Integer> columnsLeftToProcess = new Vector<Integer>();

		for (int i = 0; i < forest.getDimension(); i++) {
			columnsLeftToProcess.add(new Integer(i));
		}
		while (!columnsLeftToProcess.isEmpty()) {
			//Randomly select a column to process from the ones left to process
			int indexToProcess = (int)(Math.random() * columnsLeftToProcess.size() - 1);
			int columnNumToProcess = columnsLeftToProcess.get(indexToProcess).intValue(); 
			numberOfConflictsRemaining += moveWithinColumn(columnNumToProcess);
			columnsLeftToProcess.remove(new Integer(columnNumToProcess));
		}
		return numberOfConflictsRemaining;
	}
	
}
