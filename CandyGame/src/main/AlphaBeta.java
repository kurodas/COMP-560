package main;

import main.Cell.Color;

public class AlphaBeta implements Strategy{

	private Cell.Color playerColor;
	private int maxDepth;
	private Cell.Color opponentColor;
	
	public Move move(Board board, int maxDepth, Cell.Color playerColor){
		this.playerColor = playerColor;
		this.opponentColor = playerColor == Cell.Color.BLUE ? Cell.Color.GREEN : Cell.Color.BLUE;
		this.maxDepth = maxDepth;
		int max = Integer.MIN_VALUE;
		int bestX = -1;
		int bestY = -1;
		int alpha = Integer.MIN_VALUE;
		for (int x = 0; x < 6; x++){
			for (int y = 0; y < 6; y++){
				if (board.getCell(x, y).color == Cell.Color.BLANK){
					Board bClone = board.clone();
					bClone.play(new Move(x, y, playerColor));
					int min = minimaxMin(bClone,2,alpha,Integer.MAX_VALUE);
					if(min > max){
						max = min;
						bestX = x;
						bestY = y;
					}
					alpha = Math.max(alpha, max);
				}
			}
		}
		Move m = new Move(bestX,bestY, playerColor);
		return m;
	}

	//Performs a maximum step on the MiniMax, if our depth is >= maxDepth provided during the 
	//initialization, simply returns the heuristic evaluation of the board
	private int minimaxMax(Board board, int depth, int alpha, int beta){
		if(board.isGameOver()){
			return winLoseCheck(board);
		}
		
		if (depth >= maxDepth){
			return heuristicEval(board);
		}
		
		int v = Integer.MIN_VALUE;
		for (int x = 0; x < 6; x++){
			for (int y = 0; y < 6; y++){
				if (board.getCell(x, y).color == Cell.Color.BLANK){
					Board bClone = board.clone();
					bClone.play(new Move(x, y, playerColor));
					int min = minimaxMin(bClone,depth + 1,alpha,beta);
					v = Math.max(v, min);
					if(beta <= v){
						return Integer.MAX_VALUE-1;//prune
					}
					alpha = Math.max(alpha, v);
				}
			}
		}
		
		return v;
	}
	
	//Performs a minimum step on the MiniMax, if our depth is >= maxDepth provided during the 
	//initialization, simply returns the heuristic evaluation of the board
	private int minimaxMin(Board board, int depth, int alpha, int beta){
		if(board.isGameOver()){
			return winLoseCheck(board);
		}
		if(depth>=maxDepth){
			return heuristicEval(board);
		}
		int v = Integer.MAX_VALUE;
		for (int x = 0; x < 6; x++){
			for (int y = 0; y < 6; y++){
				if (board.getCell(x, y).color == Cell.Color.BLANK){
					Board bClone = board.clone();
					bClone.play(new Move(x, y, opponentColor));
					int max = minimaxMax(bClone, depth + 1,alpha,beta);
					v = Math.min(v, max);
					if(alpha>= max){
						return Integer.MIN_VALUE+1;//prune
					}
					beta = Math.min(beta, v);
				}
			}
		}
		
		return v;
	}
	
	//Performs an evaluation on the current board with the heuristic described in the document
	//Assumes the player color given during the initialization is the positive direction
	private int heuristicEval(Board board) {
		int score = 0;
		//board.printBoardState();
		score += board.getScoreForSpace(0, 0, playerColor) * 2;
		score += board.getScoreForSpace(0, 5, playerColor) * 2;
		score += board.getScoreForSpace(5, 0, playerColor) * 2;
		score += board.getScoreForSpace(5, 5, playerColor) * 2;
		for(int i = 1; i<5; i++){
			score += Math.round(board.getScoreForSpace(i, 0, playerColor) * 1.5);
			score += Math.round(board.getScoreForSpace(i, 5, playerColor) * 1.5);
			score += Math.round(board.getScoreForSpace(0, i, playerColor) * 1.5);
			score += Math.round(board.getScoreForSpace(5, i, playerColor) * 1.5);
		}
		for(int x = 1; x<5; x++){
			for(int y = 1; y<5; y++){
				score += board.getScoreForSpace(x, y, playerColor);
			}
		}
		
		return score;
	}
	
	private int heuristicEval2(Board board) {
		int score = 0;
		score += board.getScoreForSpace(0, 0, playerColor) * 2;
		score += board.getScoreForSpace(0, 5, playerColor) * 2;
		score += board.getScoreForSpace(5, 0, playerColor) * 2;
		score += board.getScoreForSpace(5, 5, playerColor) * 2;
		for(int i = 1; i<5; i++){
			score += Math.round(board.getScoreForSpace(i, 0, playerColor) * 5 / 3);
			score += Math.round(board.getScoreForSpace(i, 5, playerColor) * 5 / 3);
			score += Math.round(board.getScoreForSpace(0, i, playerColor) * 5 / 3);
			score += Math.round(board.getScoreForSpace(5, i, playerColor) * 5 / 3);
		}
		for(int i = 2; i<4; i++){
			score += Math.round(board.getScoreForSpace(i, 1, playerColor) * 4 / 3);
			score += Math.round(board.getScoreForSpace(i, 4, playerColor) * 4 / 3);
			score += Math.round(board.getScoreForSpace(1, i, playerColor) * 4 / 3);
			score += Math.round(board.getScoreForSpace(4, i, playerColor) * 4 / 3);
		}
		
		score += board.getScoreForSpace(2, 2, playerColor);
		score += board.getScoreForSpace(2, 3, playerColor);
		score += board.getScoreForSpace(3, 2, playerColor);
		score += board.getScoreForSpace(3, 3, playerColor);
		return score;
		
	}
	
	public int winLoseCheck(Board b){
		int sum = 0;
		for(int x = 0; x < 6; x++){
			for(int y = 0; y < 6; y++){
				Cell c = b.getCell(x, y);
				if(c.color == playerColor){
					sum += c.value;
				}
				else{
					sum -= c.value;
				}
			}
		}
		if(sum > 0){
			return Integer.MAX_VALUE - 1;//maximum possible utility, victory
		}
		else if(sum == 0){
			return 0;//draw
		}
		else{
			return Integer.MIN_VALUE + 1;//minimum possible utility, failure
		}
		
	}


	
}

