// Chris Roeder 2/5/04
// translated to java 2/14/04

class Rook extends Piece {
    public Rook() { 
        calc15x15(); 
    }
    public Rook(Point pt) {
        super(pt);  
        calc15x15(); 
    }
    public Rook(Point p, boolean w) {
    	super(p, w);
    	calc15x15();
    }
    public boolean reachable(Point from, Point to, Piece board[][], Piece  obstacles[][]) {
        return reachable(from, to) && !blocked(from, to, obstacles);
    }
    public boolean reachable(Point from, Point to) {
        int delta_col = from.col - to.col;
        int delta_row = from.row - to.row;
        return (delta_col==0 || delta_row==0);
    }
    public String getName() {
        return "Rook";
    } 
    public char getSymbol() {
        return 'R';
    }
}


