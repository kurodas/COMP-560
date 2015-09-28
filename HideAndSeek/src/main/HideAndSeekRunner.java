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
		System.out.print("For friend in column " 
				+ forest.getFriendLocations().get(0).getColumnNumber());
		System.out.print(" row " + forest.getFriendLocations().get(0).getRowNumber());
		System.out.print(", there are " + forest.getFriendLocations().get(0).getConflictCount());
		System.out.println(" conflicts.");
	}
}
