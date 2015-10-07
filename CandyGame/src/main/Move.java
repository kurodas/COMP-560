package main;

import main.Cell.Color;

public class Move {
	int row;
	int column;
	Color moveColor;
	
	public Move(int row, int column, Color mColor){
		this.row = row;
		this.column = column;
		moveColor = mColor;
	}
	
}
