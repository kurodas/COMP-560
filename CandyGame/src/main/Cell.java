package main;

public class Cell {
	enum colors {GREEN, BLUE, BLANK};
	public int value;
	public colors color;
	
	public Cell(int v, colors c){
		value = v;
		color = c;
	}
}
