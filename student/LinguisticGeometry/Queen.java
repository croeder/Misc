// Chris Roeder 2/5/04
// translated to java 2/14/04

class Queen extends Piece {
    public Queen() { 
        calc15x15(); 
    }
    public Queen(Point pt) {
        super(pt);
        calc15x15(); 
    }
    public Queen(Point pt, boolean w){
    	super(pt, w);
    	calc15x15();
    }
    public boolean reachable(Point from, Point to)  {
        int delta_col = Math.abs(from.col - to.col);
        int delta_row = Math.abs(from.row - to.row);
        return (delta_col == delta_row || delta_row == 0 || delta_col == 0) ;
    }
    public boolean reachable(Point from, Point to, Piece board[][], Piece  obstacles[][]) {
    	/*if (blocked(from, to, obstacles)) {
    		//System.out.println("" + from + " " + to);
    	}
    	*/
        return  reachable(from, to) && !blocked(from, to, obstacles);
    }
    public String getName() {
        return "Queen";
    }
    public char getSymbol() {
        return 'Q';
    }
}
