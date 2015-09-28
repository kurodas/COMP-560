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
	private int rowNumber, columnNumber, conflictCount;
	char value;
	Forest forest;
	public enum Direction{upLeft, up, upRight, right, downRight, down, downLeft, left}
	
	public Space(int row, int column, char val, Forest f){
		rowNumber = row;
		columnNumber = column;
		if(val == ' ' || val == 'T' || val == 'F')
			value = val;
		forest = f;
	}
	
	public int getConflictCount(){
		//Reset conflictCount
		conflictCount = 0;
		//Check each direction
		for(Direction d: Direction.values()){
			//If a conflict is found, increment conflictCount
			if(conflictsInDirection(d))
				conflictCount++;
		}
		return conflictCount;
	}
	
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
			case upLeft:
				if (rowNumber == 0 || columnNumber == 0)
					return false;
				else{
					nextSpace = forest.getSpace(rowNumber - 1, columnNumber - 1);
					break;
				}
			case up:
				if (rowNumber == 0)
					return false;
				else{
					nextSpace = forest.getSpace(rowNumber - 1, columnNumber);
					break;
				}
			case upRight:
				if (rowNumber == 0 || columnNumber == forest.getDimension() - 1)
					return false;
				else{
					nextSpace = forest.getSpace(rowNumber - 1, columnNumber + 1);
					break;
				}
			case right:
				if (columnNumber == forest.getDimension() - 1)
					return false;
				else{
					nextSpace = forest.getSpace(rowNumber, columnNumber + 1);
					break;
				}
			case downRight:
				if (rowNumber == forest.getDimension() - 1
						|| columnNumber == forest.getDimension() - 1)
					return false;
				else{
					nextSpace = forest.getSpace(rowNumber + 1, columnNumber + 1);
					break;
				}
			case down:
				if (rowNumber == forest.getDimension() - 1)
					return false;
				else{
					nextSpace = forest.getSpace(rowNumber + 1, columnNumber);
					break;
				}
			case downLeft:
				if (rowNumber == forest.getDimension() - 1 || columnNumber == 0)
					return false;
				else {
					nextSpace = forest.getSpace(rowNumber + 1, columnNumber - 1);
					break;
				}
			case left:
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
	public void setValue(char val){
		if(val == ' ' || val == 'T' || val == 'F')
			value = val;
	}
	
	//Returns value of the space
	public char getValue(){
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
		return value == ' ';
	}
	
	//Returns true if this space has a tree
	public boolean hasTree(){
		return value == 'T';
	}
	
	//Returns true if this space has a friend
	public boolean hasFriend(){
		return value == 'F';
	}
	
}
