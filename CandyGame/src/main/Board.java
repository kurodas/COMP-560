package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import main.Cell.Color;

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
				board[row][column] = new Cell(input.nextInt(), Color.BLANK);
			}
		}
		input.close();
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
	
	public void play(Move move){
		if(move.x<0 || move.y < 0){
			System.out.println("HateLife");
		}
		if(board[move.x][move.y].color == Cell.Color.BLANK){
			board[move.x][move.y].color = move.moveColor;
			if(nearbyCellOfSameColor(move.x, move.y, move.moveColor)){
				recolorNearbyCells(move.x, move.y, move.moveColor);
			}
		}
	}
	/**
	 * Checks the spaces above, below, left, and right 
	 * of the space to see if any of them are the player's color
	 * @param x
	 * @param y
	 * @param playerColor
	 * @return true if any vertically or horizontally adjacent spaces are playerColor
	 * 			false otherwise
	 */
	private boolean nearbyCellOfSameColor(int x, int y, Cell.Color playerColor){
		if(x > 0 && board[x - 1][y].color == playerColor)
			return true;
		else if(x < 5 && board[x + 1][y].color == playerColor)
			return true;
		else if(y > 0 && board[x][y - 1].color == playerColor)
			return true;
		else if (y < 5 && board[x][y + 1].color == playerColor)
			return true;
		else
			return false;
	}
	/**
	 * Recolors spacess that are above, below, left, and right
	 * of the space at this coordinate to playerColor if 
	 * they are the opponent's color 
	 * @param x
	 * @param y
	 * @param playerColor
	 */
	private void recolorNearbyCells(int x, int y, Cell.Color playerColor){
		Cell.Color oppColor = playerColor == Cell.Color.BLUE ? Cell.Color.GREEN : Cell.Color.BLUE;
		if(x > 0 && board[x-1][y].color == oppColor)
			board[x-1][y].color = playerColor;
		if(x < 5 &&board[x+1][y].color == oppColor)
			board[x+1][y].color = playerColor;
		if(y > 0 && board[x][y-1].color == oppColor)
			board[x][y-1].color = playerColor;
		if(y < 5 && board[x][y+1].color == oppColor)
			board[x][y+1].color = playerColor;
	}
	/**
	 * Checks to see if the game is over by looking for empty spaces 
	 * @return true if all spaces are colored
	 * 			false if any spaces are still uncolored
	 */
	public boolean isGameOver(){
		for(int x = 0; x < 6; x++){
			for(int y = 0; y < 6; y++){
				if(board[x][y].color == Cell.Color.BLANK){
					return false;
				}
			}
		}
		return true;
	}
	/**
	 * Returns the score that the player of player color 
	 * gets from this space
	 * @param x
	 * @param y
	 * @param playerColor
	 * @return 0 if the space is uncolored
	 * 			the value of the space if it is the player's color
	 * 			the negative value of the space if it is the opponent's color
	 */
	public int getScoreForSpace(int x, int y, Cell.Color playerColor){
		if(board[x][y].color == Cell.Color.BLANK){
			return 0;
		}
		else if(board[x][y].color == playerColor){
			return board[x][y].value;
		}
		else{
			return -board[x][y].value;
		}
	}
	
	public void printBoardState(){
		for(int row = 0; row < 6; row++){
			for(int column = 0; column < 6; column++){
				//Print out value of the space
				if(board[row][column].value < 10)
					System.out.print(" " + board[row][column].value);
				else
					System.out.print(board[row][column].value);
				
				//Print color of this space
				if(board[row][column].color == Color.GREEN)
					System.out.print("G ");
				else if(board[row][column].color == Color.BLUE)
					System.out.print("B ");
				else
					System.out.print("  ");
			}
			System.out.println();
		}
		System.out.println();
	}
	
	
}