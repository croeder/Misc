// Chris Roeder 2/5/04
// translated to Java 2/14/04

class Knight extends Piece {
    public Knight() { 
        calc15x15(); 
    }
    public Knight(Point pt) {
        super(pt); 
        calc15x15(); 
    }
    public Knight(Point p, boolean w) {
    	super(p,w);
    	calc15x15();
    }
    public boolean reachable(Point from, Point to) {
        int delta_row = Math.abs(from.row - to.row);
        int delta_col = Math.abs(from.col - to.col);
        return (delta_row==1 && delta_col==2 ) ||
           (delta_row==2 && delta_col==1);
    }
    public boolean reachable(Point from, Point to, Piece board[][], Piece  obstacles[][]) {
        return reachable(from, to) && !blocked(from, to, obstacles);
    }
    public String getName() {
        return "Knight";
    }
    public char getSymbol() {
        return 'N';
    }
    protected boolean blocked(Point from, Point to, Piece  obstacles[][]) {
        // Knight can jump so its only blocked if the to point
        // has an obstacle.
        return (obstacles[to.row][to.col]!=null);
    }
}

    


    
