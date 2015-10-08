package main;

import main.Cell.Color;

public class AlphaBeta extends AbstractStrategy{
	
	public Move move(Board board, int maxDepth, Color playerColor){
		this.playerColor = playerColor;
		this.opponentColor = playerColor == Color.BLUE ? Color.GREEN : Color.BLUE;
		this.maxDepth = maxDepth;
		int max = Integer.MIN_VALUE;
		int bestRow = -1;
		int bestColumn = -1;
		int alpha = Integer.MIN_VALUE;
		for (int row = 0; row < 6; row++){
			for (int column = 0; column < 6; column++){
				if (board.getCell(row, column).color == Color.BLANK){
					Board bClone = board.clone();
					bClone.play(new Move(row, column, playerColor));
					int min = minimaxMin(bClone,1,alpha,Integer.MAX_VALUE);
					if(min > max){
						max = min;
						bestRow = row;
						bestColumn = column;
					}
					alpha = Math.max(alpha, max);
				}
			}
		}
		Move m = new Move(bestRow,bestColumn, playerColor);
		printMove(m);
		//System.out.println("Nodes Expanded: " + leafNodesExpanded);
		return m;
	}

	//Performs a maximum step on the MiniMax, if our depth is >= maxDepth provided during the 
	//initialization, simply returns the heuristic evaluation of the board
	//Implements pseudocode for alpha-beta from the textbook
	private int minimaxMax(Board board, int depth, int alpha, int beta){
		if(board.isGameOver()){
			return winLoseCheck(board);
		}
		
		if (depth >= maxDepth){
			return heuristicEval(board);
		}
		
		int v = Integer.MIN_VALUE;
		for (int row = 0; row < 6; row++){
			for (int column = 0; column < 6; column++){
				if (board.getCell(row, column).color == Color.BLANK){
					Board bClone = board.clone();
					bClone.play(new Move(row, column, playerColor));
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
	//initialization, simply returns the heuristic evaluation of the board.
	//Implements pseudocode for alpha-beta from the textbook
	private int minimaxMin(Board board, int depth, int alpha, int beta){
		if(board.isGameOver()){
			return winLoseCheck(board);
		}
		if(depth>=maxDepth){
			return heuristicEval(board);
		}
		int v = Integer.MAX_VALUE;
		for (int row = 0; row < 6; row++){
			for (int column = 0; column < 6; column++){
				if (board.getCell(row, column).color == Color.BLANK){
					Board bClone = board.clone();
					bClone.play(new Move(row, column, opponentColor));
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
	
}

