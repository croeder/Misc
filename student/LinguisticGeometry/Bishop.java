// Chris Roeder 2/5/04
// translated to java 2/14/04

public class Bishop extends  Piece {
    public Bishop() { 
        calc15x15(); 
    }
    public Bishop(Point pt) {
        super(pt);
        calc15x15(); 
    }
    public Bishop(Point p, boolean w) {
    	super(p, w);
    	calc15x15();
    }
    public boolean reachable(Point from, Point to) {
        int delta_row = Math.abs(from.row - to.row);
        int delta_col = Math.abs(from.col - to.col);
        return delta_row==delta_col;
    }
    public boolean reachable(Point from, Point to, Piece board[][], Piece  obstacles[][]) {
        if (reachable(from, to))  {
			if (!blocked(from, to, obstacles)) {
        		return true;
			}
			else {
				return false;
			}
        }
        else {
       
        	return false;
        }
    } 
    public String getName() {
        return "Bishop";
    }
    public char getSymbol() {
        return 'B';
    }
}

