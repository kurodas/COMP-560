package main;

import main.Cell.Color;
/**
 * Abstract class extended by Minimax and AlphaBeta 
 * 
 */
public abstract class AbstractStrategy implements StrategyInterface{
	protected Color playerColor;
	protected int maxDepth;
	protected Color opponentColor;
	protected long leafNodesExpanded = 0;
	
	
	//Performs an evaluation on the current board with the heuristic described in the document
	//Assumes the player color given during the initialization is the positive direction
	//Prioritizes corner spaces and spaces along the edge of the board over other spaces
	protected int heuristicEval(Board board) {
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
		for(int row = 1; row<5; row++){
			for(int column = 1; column<5; column++){
				score += board.getScoreForSpace(row, column, playerColor);
			}
		}
		//Leaf nodes expanded counter is incremented whenever the 
		//evaluation function is called at a leaf node 
		leafNodesExpanded++;

		return score;
	}
	
	//Prioritizes spaces farther from the center over spaces near the center of the board
	protected int heuristicEval2(Board board) {
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
		
		//Leaf nodes expanded counter is incremented whenever the 
		//evaluation function is called at a leaf node 
		leafNodesExpanded++;

		return score;
		
	}
	
	public int winLoseCheck(Board b){
		int sum = 0;
		for(int row = 0; row < 6; row++){
			for(int column = 0; column < 6; column++){
				Cell c = b.getCell(row, column);
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
			return Integer.MIN_VALUE + 1;//minimum possible utility (less than minimum though), failure
		}
		
	}
	
	public int getScore(Board b){
		int score = 0;
		for (int row = 0; row < 6; row++){
			for (int column = 0; column < 6; column++){
				if(b.getCell(row, column).color == playerColor)
					score += b.getCell(row, column).value;
			}
		}
		return score;
	}
	
	public long getNodesExpanded(){
		return leafNodesExpanded;
	}
	
	public void printMove(Move m){
		String column;
		if(m.column == 0){
			column = "A";
		}
		else if(m.column == 1){
			column = "B";
		}
		else if(m.column == 2){
			column = "C";
		}
		else if(m.column == 3){
			column = "D";
		}
		else if(m.column == 4){
			column = "E";
		}
		else {
			column = "F";
		}
		System.out.println(m.moveColor + ": drop " + column + (m.row + 1));
	}
}
