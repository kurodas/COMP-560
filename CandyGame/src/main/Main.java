package main;

public class Main {

	public static void main(String[] args) {
		int greenwin = 0;
		int bluewin = 0;
		while(true){
		Board b = new Board();
		for(int x = 0; x < 6; x++){
			for(int y = 0; y < 6; y++){
				Cell c = new Cell((int) Math.floor(Math.random() * 100), Cell.Color.BLANK);
				b.board[x][y] = c;
			}
		}
		boolean bluesTurn = true;
		Minimax mm  = new Minimax();
		Minimax2 mm2 = new Minimax2();
		AlphaBeta ab = new AlphaBeta();
		while(!b.isGameOver()){
			//System.out.println("Played");
			if(bluesTurn){
				bluesTurn = false;
				Move a = ab.move(b, 6, Cell.Color.BLUE);
				b.play(a);
			}
			else{
				bluesTurn = true;
				Move a = mm2.move(b, 3, Cell.Color.GREEN);
				b.play(a);
			}
		}
		mm.playerColor = Cell.Color.BLUE;
		int win = mm.winLoseCheck(b);
		if(win>0){
			bluewin++;
//			System.out.printf("Blue wins: %d Green wins: %d\n",bluewin,greenwin);
		}
		else if(win == 0){
			System.out.println("Draw");
		}
		else{
			greenwin++;
	//		System.out.println("Green wins");
		}
		System.out.printf("Blue wins: %d Green wins: %d\n",bluewin,greenwin);
	}
	}

}
