package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class Board {

	public Object[][] boardArray;
	
	protected ArrayList<String> stringList;
	
	public Board(String fileLoc){
		generateStringList(fileLoc);
		if(!stringList.isEmpty())
			generateBoardArray();
	}
	
	protected ArrayList<String> generateStringList(String fileLoc){
		BufferedReader br;
		stringList = new ArrayList<String>(); 
		try {
			br = new BufferedReader (new FileReader(fileLoc) );
			//Read in text file line by line
			for(String input = br.readLine(); input != null; input = br.readLine()){
				stringList.add(input);
			}
			br.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return stringList;
	}

	protected void generateBoardArray(){
		boardArray = new Cell[6][6];
		for(int y = 0; y < 6; y++){
			String currentLine = stringList.get(y);
			for(int x = 0; x < 6; x++){
				
			}
		}
	}	
}