package main;

import main.Space.SpaceValue;

public class LocalSearch {
	Forest forest;
	
	public LocalSearch(Forest f){
		forest = f;
		//Initialize the numberOfRemainingConflictsRemaining to -1 to enter loop
		int numberOfConflictsRemaining = -1;
		
		//Tracks number of iterations in order to know when to reset the forest
		int iterationCount = 1;
		
		//Tracks number of reset to know when to give up
		int resetCount = 0;
		
		// Move friends around while there are conflicts
		while(numberOfConflictsRemaining != 0 && resetCount < 1000){
			//Reset numberOfRemainingConflictsRemaining to 0 for each iteration
			numberOfConflictsRemaining = 0;
			for(int columnIndex = 0; columnIndex < forest.getDimension(); columnIndex++){
				Space currentFriendSpace = forest.getFriendLocations().get(columnIndex);
				//If friend location in current column does not cause conflicts, 
				//go to next column
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
					numberOfConflictsRemaining += spaceToMoveTo.getConflictCount(false);
				}
			}
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
	
	
	
}
