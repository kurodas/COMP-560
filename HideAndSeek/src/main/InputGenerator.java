package main;

import java.io.PrintWriter;
/**
 * Generates input files with specified dimensions and tree count
 * or with randomized dimensions and tree count.
 * Tree locations are always randomized.
 * @author skuroda
 * @param args [0] = dimension/friend count 
 * 			args[1] = tree count 
 */
public class InputGenerator {
	
	public static void main(String[] args){
		int friendCount, treeCount;
		if(args.length < 1){
			friendCount = (int)(4 + 20 * Math.random());//Min size with a solution is 4*4
		}
		else
			friendCount = Integer.parseInt(args[0]);
		if(args.length < 2){
			treeCount = (int)((friendCount * friendCount - friendCount) * Math.random());
		}
		else
			treeCount = Integer.parseInt(args[1]);
		
//		System.out.println(friendCount + " " + treeCount);
		InputGeneratorSpace[] treeSpaces = new InputGeneratorSpace[treeCount];
		InputGenerator gen = new InputGenerator();
		for(int i = 0; i < treeCount; i++){
			boolean newTreeSpaceGenerated = false;
			while (!newTreeSpaceGenerated) {
				int nextRowNum = (int) (friendCount * Math.random());
				int nextColumnNum = (int) (friendCount * Math.random());
				InputGeneratorSpace nextSpace = gen.new InputGeneratorSpace(nextRowNum, nextColumnNum);
				boolean treeIsNew = true;
				for (int j = 0; j < treeSpaces.length; j++) {
					if (treeSpaces[j] != null && treeSpaces[j].equals(nextSpace))
						treeIsNew = false;
				}
				if (treeIsNew){
					treeSpaces[i] = nextSpace;
					newTreeSpaceGenerated = true;
				}
			}
		}
		try {
			PrintWriter writer = new PrintWriter("Input.txt", "UTF-8");
			writer.println(friendCount + " " + treeCount);
			for(InputGeneratorSpace s:treeSpaces){
				writer.println(s.rowNumber + " " + s.columnNumber);
			}
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}		
		
	}
	
	private class InputGeneratorSpace{
		int rowNumber, columnNumber;
		
		//+1 because input-handling code expects 1-indexed coordinates.
		InputGeneratorSpace(int row, int column){
			rowNumber = row + 1;
			columnNumber = column + 1;
		}
		
		boolean equals(InputGeneratorSpace space){
			return (this.columnNumber == space.columnNumber && this.rowNumber == space.rowNumber);
		}
	}
}
