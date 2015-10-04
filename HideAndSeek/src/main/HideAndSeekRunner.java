package main;

import java.io.FileNotFoundException;

public class HideAndSeekRunner {
	/**
	 * 
	 * @param args [0] = Filename of input file 
	 * 			args[1] = Assignment order: 
	 * 						O = left-to-right
	 * 						M = most-conflicted
	 * 						R = random 
	 * @throws FileNotFoundException
	 */
	public static void main(String args[]) throws FileNotFoundException{
		//InputGenerator.main(null);
		Forest forest = new Forest(args[0]);
//		for (int i = 0; i < forest.getDimension(); i++) {
//			System.out.print("For friend in column "
//					+ forest.getFriendLocations().get(i).getColumnNumber());
//			System.out.print(" row "
//					+ forest.getFriendLocations().get(i).getRowNumber());
//			System.out.print(", there are "
//					+ forest.getFriendLocations().get(i).getConflictCount(false));
//			System.out.println(" conflicts.");
//		}
		System.out.println();
		LocalSearch search = new LocalSearch(forest, args[1]);
		forest.printForestGrid();
//		for (int i = 0; i < forest.getDimension(); i++) {
//			System.out.print("For friend in column "
//					+ forest.getFriendLocations().get(i).getColumnNumber());
//			System.out.print(" row "
//					+ forest.getFriendLocations().get(i).getRowNumber());
//			System.out.print(", there are "
//					+ forest.getFriendLocations().get(i).getConflictCount(false));
//			System.out.println(" conflicts.");
//		}
		for(Space f:forest.getFriendLocations()){
			System.out.println((f.getRowNumber() + 1) + " " + (f.getColumnNumber() + 1));
		}
	}
}
