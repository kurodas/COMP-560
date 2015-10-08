package main;

import java.io.FileNotFoundException;

import main.Cell.Color;

public class CandyGameRunner {
	
	static int DEFAULT_MINIMAX_MAX_DEPTH = 3;
	static int DEFAULT_ALPHABETA_MAX_DEPTH = 6;
	/**
	 * 
	 * @param   args[0] = Filename of board
	 * 			args[1] = Player 1 strategy:
	 * 						ab = AlphaBeta
	 * 						mm = Minimax
	 * 			args[2] = Player 2 strategy:
	 * 						ab = AlphaBeta
	 * 						mm = Minimax
	 * 			Optional parameters
	 * 			args[3] = Minimax maxDepth
	 * 			args[4] = AlphaBeta maxDepth
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException {
		AbstractStrategy player1, player2;
		int minimaxMaxDepth, alphaBetaMaxDepth;
		//Set max depths for search algorithms, either from default values or args
		if(args.length > 3){
			minimaxMaxDepth = Integer.parseInt(args[3]);
			if(args.length > 4)
				alphaBetaMaxDepth = Integer.parseInt(args[4]);
			else
				alphaBetaMaxDepth = DEFAULT_ALPHABETA_MAX_DEPTH;
		}
		else{
			minimaxMaxDepth = DEFAULT_MINIMAX_MAX_DEPTH;
			alphaBetaMaxDepth = DEFAULT_ALPHABETA_MAX_DEPTH;
		}
		int player1MaxDepth, player2MaxDepth;
		long startTime, endTime;
		long player1TotalTime = 0, player2TotalTime = 0;
		
		Board gameBoard = new Board(args[0]);		
		if(args.length > 1 && args[1].equalsIgnoreCase("MM")){
			player1 = new Minimax();
			player1MaxDepth = minimaxMaxDepth;
			System.out.println("Player 1: Minimax");
		}
		else //if(args[1].equalsIgnoreCase("AB"))
		{
			player1 = new AlphaBeta();
			player1MaxDepth = alphaBetaMaxDepth;
			System.out.println("Player 1: Alpha-Beta");
		}
		
		if(args.length > 2 && args[2].equalsIgnoreCase("MM")){
			player2 = new Minimax();
			player2MaxDepth = minimaxMaxDepth;
			System.out.println("Player 2: Minimax");
		}
		else //if(args[2].equalsIgnoreCase("AB"))
		{
			player2 = new AlphaBeta();
			player2MaxDepth = alphaBetaMaxDepth;
			System.out.println("Player 2: Alpha-Beta");
		}
		
		while(!gameBoard.isGameOver()){
			//Player 1 takes turn
			startTime = System.currentTimeMillis();
			gameBoard.play(player1.move(gameBoard, player1MaxDepth, Color.BLUE));
			endTime = System.currentTimeMillis();
			player1TotalTime += (endTime - startTime);
			//gameBoard.printBoardState();
			
			//Player 2 takes turn
			if(!gameBoard.isGameOver()){
				startTime = System.currentTimeMillis();
				gameBoard.play(player2.move(gameBoard, player2MaxDepth, Color.GREEN));
				endTime = System.currentTimeMillis();
				player2TotalTime += (endTime - startTime);
				//gameBoard.printBoardState();
			}
		}
		gameBoard.printBoardState();
		System.out.println("Player 1 Score: " + player1.getScore(gameBoard));
		System.out.println("Player 2 Score: " + player2.getScore(gameBoard));
		System.out.println("Player 1 took an average of " + player1TotalTime/18 + " milliseconds per move");
		System.out.println("Player 2 took an average of " + player2TotalTime/18 + " milliseconds per move");
		System.out.println("Player 1 expanded an average of " + player1.leafNodesExpanded/18 + " nodes per move");
		System.out.println("Player 2 expanded an average of " + player2.leafNodesExpanded/18 + " nodes per move");
		System.out.println("Player 1 expanded " + player1.leafNodesExpanded + " nodes total");	
		System.out.println("Player 2 expanded " + player2.leafNodesExpanded + " nodes total");
	}
	
}