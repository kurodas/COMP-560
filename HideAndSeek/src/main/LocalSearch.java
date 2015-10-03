package main;

import main.Space.SpaceValue;

public class LocalSearch {
	Forest forest;
	
	public LocalSearch(Forest f){
		forest = f;
		int numberOfConflictsRemaining = -1;
		while(numberOfConflictsRemaining != 0){
			numberOfConflictsRemaining = 0;
			for(int columnIndex = 0; columnIndex < f.getDimension(); columnIndex++){
				Space currentFriendSpace = f.getFriendLocations().get(columnIndex);
				//If friend location in current column does not cause conflicts, 
				//go to next column
				if(currentFriendSpace.getConflictCount(true) != 0){
					int minimumConflictCount = Integer.MAX_VALUE;
					int currentCandidateSpaceRowNum = currentFriendSpace.getRowNumber();
					for(int rowIndex = 0; rowIndex < f.getDimension(); rowIndex++){
						//Set all in column non-tree spaces to empty 
						for (int i = 0; i < f.getDimension(); i++) {
							if (!f.getSpace(rowIndex, columnIndex).hasTree()) {
								f.getSpace(rowIndex, columnIndex).setValue(
										SpaceValue.OPEN);
							}
						}
						//Try moving the friend to space at (columnNum, rowNum)
						f.getSpace(rowIndex, columnIndex).setValue(SpaceValue.FRIEND);
						if(f.getSpace(rowIndex, columnIndex).getConflictCount(true) < minimumConflictCount){
							minimumConflictCount = f.getSpace(rowIndex, columnIndex).getConflictCount(false);
							currentCandidateSpaceRowNum = rowIndex;
						}
					}
					//Set all in column non-tree spaces to empty 
					for (int rowIndex = 0; rowIndex < f.getDimension(); rowIndex++) {
						if (!f.getSpace(rowIndex, columnIndex).hasTree()) {
							f.getSpace(rowIndex, columnIndex).setValue(
									SpaceValue.OPEN);
						}
					}
					f.getFriendLocations().set(columnIndex, f.getSpace(currentCandidateSpaceRowNum, columnIndex));
					f.getSpace(currentCandidateSpaceRowNum, columnIndex).setValue(SpaceValue.FRIEND);
					numberOfConflictsRemaining += f.getSpace(currentCandidateSpaceRowNum, columnIndex).getConflictCount(false);
					
				}
			}
		}
		
	}
	
	
	
}
