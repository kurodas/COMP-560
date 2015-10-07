package main;

public class Cell {
	public enum Color {GREEN, BLUE, BLANK};
	public int value;
	public Color color;
	
	public Cell(int v, Color c){
		value = v;
		color = c;
	}
}
