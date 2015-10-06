package main;

import main.Cell.colors;


public class Minimax {
	
	private Cell.colors playerColor;
	private int maxDepth;
	private Cell.colors opponentColor;
	
	public Move MiniMax(Board board, int maxDepth, Cell.colors playerColor){
		this.playerColor = playerColor;
		this.opponentColor = playerColor == Cell.colors.BLUE ? Cell.colors.GREEN : Cell.colors.BLUE;
		this.maxDepth = maxDepth;
		int max = Integer.MIN_VALUE;
		int bestX = -1;
		int bestY = -1;
		for (int x = 0; x < 6; x++){
			for (int y = 0; y < 6; y++){
				if (board.getCell(x, y).color == Cell.colors.BLANK){
					Board bClone = board.clone();
					bClone.play(x, y, playerColor);
					int min = minimaxMin(bClone,2);
					if(min > max){
						max = min;
						bestX = x;
						bestY = y;
					}
				}
			}
		}
		Move m = new Move(bestX,bestY);
		return m;
	}

	//Performs a maximum step on the MiniMax, if our depth is >= maxDepth provided during the 
	//initialization, simply returns the heuristic evaluation of the board
	private int minimaxMax(Board board, int depth){
		if(board.isGameOver()){
			return winLoseCheck(board);
		}
		
		if (depth >= maxDepth){
			return heuristicEval(board);
		}
		
		int max = Integer.MIN_VALUE;
		for (int x = 0; x < 6; x++){
			for (int y = 0; y < 6; y++){
				if (board.getCell(x, y).color == Cell.colors.BLANK){
					Board bClone = board.clone();
					bClone.play(x, y, playerColor);
					int min = minimaxMin(bClone,depth + 1);
					if(min > max){
						max = min;
					}
				}
			}
		}
		
		return max;
	}
	
	//Performs a minimum step on the MiniMax, if our depth is >= maxDepth provided during the 
	//initialization, simply returns the heuristic evaluation of the board
	public int minimaxMin(Board board, int depth){
		if(board.isGameOver()){
			return winLoseCheck(board);
		}
		if(depth>=maxDepth){
			return heuristicEval(board);
		}
		int min = Integer.MAX_VALUE;
		for (int x = 0; x < 6; x++){
			for (int y = 0; y < 6; y++){
				if (board.getCell(x, y).color == Cell.colors.BLANK){
					Board bClone = board.clone();
					bClone.play(x, y, opponentColor);
					int max = minimaxMax(bClone, depth + 1);
					if(max < min){
						min = max;
					}
				}
			}
		}
		
		return min;
	}
	
	//Performs an evaluation on the current board with the heuristic described in the document
	//Assumes the player color given during the initialization is the positive direction
	private int heuristicEval(Board board) {
		int score = 0;
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
	
	private int winLoseCheck(Board b){
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
			return Integer.MAX_VALUE;//maximum possible utility, victory
		}
		else if(sum == 0){
			return 0;//draw
		}
		else{
			return Integer.MIN_VALUE;//minimum possible utility, failure
		}
		
	}

	
}
