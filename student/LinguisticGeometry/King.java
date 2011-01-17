// Chris Roeder 2/5/04
// translated to Java 2/14/04

public class King extends Piece {
    public King() { 
        calc15x15(); 
    }
    public King(Point pt) {
       super(pt);
       calc15x15(); 
    }
    public King(Point p, boolean w) {
    	super(p, w);
    	calc15x15();
    }
    public boolean reachable(Point from, Point to) {
        int delta_row = Math.abs(from.row - to.row);
        int delta_col = Math.abs(from.col - to.col);
        return delta_row < 2 && delta_col < 2;
    }
    public boolean reachable(Point from, Point to, Piece board[][], Piece  obstacles[][]) {
        return reachable(from, to) && !blocked(from, to, obstacles);
    }
    public String getName() {
        return "King";
    }
    public char getSymbol() {
        return 'K';
    }
}



