package main;

import java.io.FileNotFoundException;

public class HideAndSeekRunner {
	/**
	 * 
	 * @param args [0] = Filename of input file 
	 * @throws FileNotFoundException
	 */
	public static void main(String args[]) throws FileNotFoundException{
		Forest forest = new Forest(args[0]);
	}
}
