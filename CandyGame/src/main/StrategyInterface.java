package main;

import main.Cell.Color;

public interface StrategyInterface {
	public Move move(Board board, int maxDepth, Color playerColor);
}
