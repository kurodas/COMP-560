package main;

import java.io.FileNotFoundException;

import main.Cell.Color;

public class CandyGameRunner {

	public static void main(String[] args) throws FileNotFoundException {
		Board gameBoard = new Board(args[0]);
		Strategy player1, player2;
		int player1MaxDepth, player2MaxDepth;
		if(args.length > 1 && args[1].equalsIgnoreCase("MM")){
			player1 = new Minimax();
			player1MaxDepth = 3;
			System.out.println("Player 1: Minimax");
		}
		else //if(args[1].equalsIgnoreCase("AB"))
		{
			player1 = new AlphaBeta();
			player1MaxDepth = 6;
			System.out.println("Player 1: Alpha-Beta");
		}
		
		if(args.length > 2 && args[2].equalsIgnoreCase("MM")){
			player2 = new Minimax();
			player2MaxDepth = 3;
			System.out.println("Player 2: Minimax");
		}
		else //if(args[2].equalsIgnoreCase("AB"))
		{
			player2 = new AlphaBeta();
			player2MaxDepth = 6;
			System.out.println("Player 2: Alpha-Beta");
		}
		
		while(!gameBoard.isGameOver()){
			gameBoard.play(player1.move(gameBoard, player1MaxDepth, Color.BLUE));
			gameBoard.printBoardState();
			if(!gameBoard.isGameOver()){
				gameBoard.play(player2.move(gameBoard, player2MaxDepth, Color.GREEN));
				gameBoard.printBoardState();
			}
		}
		gameBoard.printBoardState();
		System.out.println(player1.winLoseCheck(gameBoard));
	}

}