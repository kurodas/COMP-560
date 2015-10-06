package main;

import java.io.FileNotFoundException;

import main.Cell.colors;

public class CandyGameRunner {

	public static void main(String[] args) throws FileNotFoundException {
		Board gameBoard = new Board(args[0]);
		Strategy player1, player2;
		int player1MaxDepth, player2MaxDepth;
		if(args[1].equalsIgnoreCase("MM"))
			player1 = new Minimax();
		else //if(args[1].equalsIgnoreCase("AB"))
			player1 = new AlphaBeta();
		
		if(args[1].equalsIgnoreCase("MM"))
			player2 = new Minimax();
		else //if(args[1].equalsIgnoreCase("AB"))
			player2 = new AlphaBeta();
		
		while(!gameBoard.isGameOver()){
			player1.move(gameBoard, player1MaxDepth, colors.BLUE);
			if(!gameBoard.isGameOver())
				player2.move(gameBoard, player2MaxDepth, colors.GREEN);
		}
	}

}
