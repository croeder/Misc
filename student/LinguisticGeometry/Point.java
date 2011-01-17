// Chris Roeder 2/5/04
// translated to java 2/14/04
// added toString 2/19/04

public class Point implements Cloneable, Comparable  {
	public int col;
    public int row;
 
    public Point(int r, int c) {
        row=r;
        col=c;
    }
    
    public Point(String s) {
    	col = s.charAt(0) - 'a' + 1;
    	row = s.charAt(1) - '0';
    }
    
    public Point() {
        row=0;
        col=0;
    }
    
    public boolean next() {
		if (col < 8 && row < 8) {
			col++;
			int temp = col;
			col = temp % 8;
			row = row + temp / 8;
			return true;
		}
		else {
			return false;
		}
    }
    
    public boolean notEqual(Point p)  {
        return !equals(p);
    }
    
    public String toString() {
    	String out = "(" +row + ", " + col + ")";
    	return out;
    }
    
    public String chessToString() {
    	int r = row + 1;
    	char letters[] = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h' };
    	char c = letters[col];
    	return "(" + c + r + ")";
    }
    
    public Object clone()  throws CloneNotSupportedException {
    	return super.clone();
    }
    
    public boolean equals(Object o){
    	if (getClass() != o.getClass())
    		return false;
        Point p = (Point) o;
    	return p.row == row && p.col == col;
    }
    
    public int compareTo(Object o) {
    	Point p = (Point) o;
    	if (p.equals(this)) 	{
    		return 0;
    	}
    	else {
    		return (row*8+col) -(p.row*8+p.col);
    	}
    }
}



