package main;
/**
 * Represents a space in the Forest.
 * value can be one of the following: 
 * 		' ' represents empty space
 * 		'T' represents tree space
 * 		'F' represents friend space
 * @author skuroda
 *
 */
public class Space {
	public enum Direction{UPLEFT, UP, UPRIGHT, RIGHT, DOWNRIGHT, DOWN, DOWNLEFT, LEFT}
	public enum SpaceValue{OPEN, FRIEND, TREE};
	
	private int rowNumber, columnNumber, conflictCount;
	SpaceValue value;
	Forest forest;
	
	
	public Space(int row, int column, SpaceValue val, Forest f){
		rowNumber = row;
		columnNumber = column;
		value = val;
		forest = f;
	}
	
	/**
	 * Returns the conflict count for this space Recalculates depending on
	 * parameter value
	 * 
	 * @param recalculate
	 *            if true, recalculate conflictCount before returning
	 * @return conflictCount
	 */
	public int getConflictCount(boolean recalculate){
		if(recalculate){
			// Reset conflictCount
			conflictCount = 0;
			// Check each direction
			for (Direction d : Direction.values()) {
				// If a conflict is found, increment conflictCount
				if (conflictsInDirection(d))
					conflictCount++;
			}
		}
		return conflictCount;
	}
	/**
	 * Recursively checks for conflict in direction d
	 * @param d
	 * @return true if conflict is found
	 * 		   	  false otherwise
	 */
	private boolean conflictsInDirection(Direction d){
		//If there is a tree in this space, there are no conflicts in direction d
		if (hasTree()) {
			return false;
		} else {
			//Check to see if the current space is the 
			//last possible space in direction d
			//If so, return false
			//else, set nextSpace to be the space in direction d from this space
			Space nextSpace = null;
			switch (d) {
			case UPLEFT:
				if (rowNumber == 0 || columnNumber == 0)
					return false;
				else{
					nextSpace = forest.getSpace(rowNumber - 1, columnNumber - 1);
					break;
				}
			//Technically unnecessary case because only one 
			//friend will ever be in each column.
			case UP:
				if (rowNumber == 0)
					return false;
				else{
					nextSpace = forest.getSpace(rowNumber - 1, columnNumber);
					break;
				}
			case UPRIGHT:
				if (rowNumber == 0 || columnNumber == forest.getDimension() - 1)
					return false;
				else{
					nextSpace = forest.getSpace(rowNumber - 1, columnNumber + 1);
					break;
				}
			case RIGHT:
				if (columnNumber == forest.getDimension() - 1)
					return false;
				else{
					nextSpace = forest.getSpace(rowNumber, columnNumber + 1);
					break;
				}
			case DOWNRIGHT:
				if (rowNumber == forest.getDimension() - 1
						|| columnNumber == forest.getDimension() - 1)
					return false;
				else{
					nextSpace = forest.getSpace(rowNumber + 1, columnNumber + 1);
					break;
				}
			//Technically unnecessary case because only one 
			//friend will ever be in each column.
			case DOWN:
				if (rowNumber == forest.getDimension() - 1)
					return false;
				else{
					nextSpace = forest.getSpace(rowNumber + 1, columnNumber);
					break;
				}
			case DOWNLEFT:
				if (rowNumber == forest.getDimension() - 1 || columnNumber == 0)
					return false;
				else {
					nextSpace = forest.getSpace(rowNumber + 1, columnNumber - 1);
					break;
				}
			case LEFT:
				if (columnNumber == 0)
					return false;
				else {
					nextSpace = forest.getSpace(rowNumber, columnNumber - 1);
					break;
				}
			}
			//Check if nextSpace has a friend, then a conflict is found
			if(nextSpace.hasFriend())
				return true;
			//Else recursively check nextSpace for conflicts
			else
				return nextSpace.conflictsInDirection(d);
			
		}
		
	}
	
	/**
	 * Compares coordinates of spaces
	 * @param s Space to compare with
	 * @return true if same coordinates
	 *         false otherwise
	 */
	public boolean equals(Space s){
		return s.rowNumber == this.rowNumber
				&& s.columnNumber == this.columnNumber;
	}
	
	/**
	 * Sets value of the space
	 * @param newVal New value of the space
	 * Value is not updated if newVal is not either ' ', 'T', or 'F'
	 */
	public void setValue(SpaceValue val){
		value = val;
	}
	
	//Returns value of the space
	public SpaceValue getValue(){
		return value;
	}
	
	//Returns row number of space
	public int getRowNumber(){
		return rowNumber;
	}
	
	//Returns column number of space
	public int getColumnNumber(){
		return columnNumber;
	}
	
	//Returns true if this space is empty
	public boolean isOpen(){
		return value == SpaceValue.OPEN;
	}
	
	//Returns true if this space has a tree
	public boolean hasTree(){
		return value == SpaceValue.TREE;
	}
	
	//Returns true if this space has a friend
	public boolean hasFriend(){
		return value == SpaceValue.FRIEND;
	}
	
	public char getPrintValue(){
		if(value == SpaceValue.TREE)
			return 'T';
		else if(value == SpaceValue.OPEN)
			return ' ';
		else
			return 'F';
	}
	
}
