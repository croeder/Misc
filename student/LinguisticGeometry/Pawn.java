// Chris Roeder 2/5/04
// translated to Java 2/14/04

class Pawn extends Piece {
    private boolean increasing; // tells direction pawn is going
    public Pawn(boolean i) {
        increasing = i;
        calc15x15();
    }
    
    public Pawn(Point pt, boolean w) {
    	super(pt, w);
    	increasing = w;
    	calc15x15();
    }
    
    public  void setIncreasing(boolean b) {
    	increasing = b;
    }
    public boolean reachable(Point from, Point to) {
        int delta_row = from.row - to.row;
        if (increasing) {
            return from.col == to.col && delta_row == -1;	
        }
        else {
           return from.col == to.col && delta_row == 1;	
        }
    }

    public boolean reachable(Point from, Point to, Piece board[][], Piece  obstacles[][]) {

		// for pawn test
		Point f = new Point(5, 2); 
		Point t = new Point(6,3);
		
		if (reachable(from, to) && !blocked(from, to, obstacles)) {
//			if (from.equals(f) && to.equals(t))
//				System.out.println("--got one easy");
			return true;
		}

    	// might still be reachable if we consider taking a piece on the diagonal
    	int delta_row = from.row - to.row;
    	int delta_col = from.col - to.col;
    	if (increasing) {
    		if (delta_row == -1 && Math.abs(delta_col) == 1  &&
    		    board[to.row][to.col] != null 
    		  &&  board[to.row][to.col].opposite(this)   ) {	
//					if (from.equals(f) && to.equals(t))
//						System.out.println("--got one increasing. " + from.chessToString() + " " + to.chessToString() );	
    			return true;
    		}
    	}
    	else {
			if (delta_row == 1 && Math.abs(delta_col) == 1 &&
			    obstacles[to.row][to.col] != null && 
				obstacles[to.row][to.col].opposite(this) ) {
//					if (from.equals(f) && to.equals(t))
//										System.out.println("got one decreasing");	
				return true;
			} 
    	}
//		if (from.equals(f) && to.equals(t))
//					System.out.println("loser");
        return false;
    }
    
    
    public String getName() {
        return "Pawn";
    }
    public char getSymbol() {
        return 'P';
    }
}


