package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import main.Cell.colors;

public class Board {

	public Cell[][] board;
	
	public Board(String fileLoc) throws FileNotFoundException{
		board = new Cell[6][6];
		generateBoard(fileLoc);
	}
	
	public Board(){
		board = new Cell[6][6];
	}

	private void generateBoard(String fileLoc) throws FileNotFoundException{
		Scanner input = new Scanner(new File(fileLoc));
		for(int row = 0; row < 6; row++){
			for(int column = 0; column < 6; column++){
				board[row][column] = new Cell(input.nextInt(), colors.BLANK);
			}
		}
	}
	
	public Cell getCell(int row, int column){
		return board[row][column];
	}
	
	public Board clone(){
		Board clone = new Board();
		for(int row = 0; row < 6; row++){
			for(int column = 0; column < 6; column++){
				clone.board[row][column] = new Cell(board[row][column].value, board[row][column].color);
			}
		}
		return clone;
	}
}