package main;

import main.Cell.Color;

public interface Strategy {
	public Move move(Board board, int maxDepth, Color playerColor);
	
	public int winLoseCheck(Board b);
}
