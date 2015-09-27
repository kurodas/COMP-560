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
	int rowNumber, columnNumber;
	char value;
	
	public Space(int row, int column, char val){
		rowNumber = row;
		columnNumber = column;
		if(val == ' ' || val == 'T' || val == 'F')
			value = val;
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
