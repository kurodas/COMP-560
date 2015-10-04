package minimax;

import minimax.Cell.colors;

public class Minimax {
	
	public int minimaxMax(Cell[][] board, int depth){
		int evalValue = gameState(board);
		
		boolean isGameOver = (evalValue == WIN || evalValue == LOSE || evalValue == DRAW);
		if (depth == 0 || isGameOver){
			return evalValue;
		}
		
		int bestValue = -1000;
		for (int y = 0; y < 6; y++){
			for (int x = 0; x < 6; x++){
				if (board[x][y].color == null){
					board[x][y].color = colors.BLUE;
					bestValue = Math.max(bestValue, minimaxMin(board, depth-1));
					board[x][y].color = null;
				}
			}
		}
		return evalValue;
	}
	
	public int minimaxMin(Cell[][] board, int depth){
		int evalValue = gameState(board);
		
		boolean isGameOver = (evalValue == WIN || evalValue == LOSE || evalValue == DRAW);
		if (depth == 0 || isGameOver){
			return evalValue;
		}
		
		int bestValue = 1000;
		for (int y = 0; y < 6; y++){
			for (int x = 0; x < 6; x++){
				if (board[x][y].color == null){
					board[x][y].color = colors.GREEN;
					bestValue = Math.max(bestValue, minimaxMax(board, depth-1));
					board[x][y].color = null;
				}
			}
		}
		return evalValue;
	}
}
