package main;

import main.Cell.colors;

public interface Strategy {
	public Move move(Board board, int maxDepth, colors playerColor);
}
