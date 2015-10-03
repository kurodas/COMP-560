package main;

import java.io.PrintWriter;
/**
 * Generates random input files
 * @author skuroda
 *
 */
public class InputGenerator {
	
	public static void main(String[] args){
		int dimension = (int)(4 + 20 * Math.random());//Min size with a solution is 4*4
		int trees = (int)((dimension * dimension - dimension) * Math.random());
		System.out.println(dimension + " " + trees);
		InputGeneratorSpace[] treeSpaces = new InputGeneratorSpace[trees];
		InputGenerator gen = new InputGenerator();
		for(int i = 0; i < trees; i++){
			boolean newTreeSpaceGenerated = false;
			while (!newTreeSpaceGenerated) {
				int nextRowNum = (int) (dimension * Math.random());
				int nextColumnNum = (int) (dimension * Math.random());
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
			writer.println(dimension + " " + trees);
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
		
		InputGeneratorSpace(int row, int column){
			rowNumber = row + 1;
			columnNumber = column + 1;
		}
		
		boolean equals(InputGeneratorSpace space){
			return (this.columnNumber == space.columnNumber && this.rowNumber == space.rowNumber);
		}
	}
}
