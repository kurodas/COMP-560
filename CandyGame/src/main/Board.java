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
	
	public void play(int x, int y, Cell.colors playerColor){
		Cell.colors oppColor = playerColor == Cell.colors.BLUE ? Cell.colors.GREEN : Cell.colors.BLUE;
		if(board[x][y].color == Cell.colors.BLANK){
			board[x][y].color = playerColor;
			for(int xloop = x-1; xloop<=x+1; x++){
				for(int yloop = y-1; yloop<=y+1; y++){
					if(xloop >= 0 && xloop <6 && yloop >= 0 && yloop < 6){
						if(board[xloop][yloop].color == oppColor){
							board[xloop][yloop].color = playerColor;
						}
					}
				}
			}
		}
	}
	
	public boolean isGameOver(){
		for(int x = 0; x < 6; x++){
			for(int y = 0; y < 6; y++){
				if(board[x][y].color == Cell.colors.BLANK){
					return false;
				}
			}
		}
		return true;
	}
	
	public int getScoreForSpace(int x, int y, Cell.colors playerColor){
		if(board[x][y].color == Cell.colors.BLANK){
			return 0;
		}
		else if(board[x][y].color == playerColor){
			return board[x][y].value;
		}
		else{
			return -board[x][y].value;
		}
	}
	
	
}