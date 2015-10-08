package main;

import java.io.FileNotFoundException;

public class HideAndSeekRunner {
	/**
	 * Runs hide and seek
	 * @param   args[0] = Filename of input file
	 * 			Optional Parameters 
	 * 			args[1] = Assignment order: 
	 * 						O = left-to-right
	 * 						M = most-conflicted
	 * 						R = random
	 * 			args[2] = Whether new input is generated
	 * 						G = generate new input
	 * 			args[3] = Dimensions of the board to be generated
	 * 			args[4] = Number of trees in the board to be generated
	 * @throws FileNotFoundException
	 */
	public static void main(String args[]) throws FileNotFoundException{
		//Pass parameters to inputGenerator if necessary
		if(args.length > 2 && args[2].equalsIgnoreCase("G")){
			if(args.length == 4){
			InputGenerator.main(new String[]{args[3]});
			}
			else if(args.length == 5){
				InputGenerator.main(new String[]{args[3], args[4]});
			}
			else
				InputGenerator.main(null);
		}
		Forest forest = new Forest(args[0]);
		//System.out.println();
		if(args.length > 1){
			LocalSearch search = new LocalSearch(forest, args[1]);
		}
		else{
			LocalSearch search = new LocalSearch(forest, null);
		}
		//forest.printForestGrid();
		for(Space f:forest.getFriendLocations()){
			System.out.println((f.getRowNumber() + 1) + " " + (f.getColumnNumber() + 1));
		}
	}
}
