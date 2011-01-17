import java.awt.Graphics;
import java.awt.Color;
import java.util.Set;
import java.util.TreeSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.font.TextAttribute;
import java.text.*;
import java.awt.Graphics2D;


// Definitely time to factor a Trajectory class out of all
// the static junk. FIX

public class ReachableBoard {
	static int _scale=150; // size of board in pixels
	static int _cellSize = _scale/8;	
	static final int _offset=30;
	Piece _obstacles[][];
	Piece _board[][];
	/** 
	 * a weak connection back to the original piece.
	 * This object is not responsible for the memory of the piece.
	 */
	Piece _thePiece; 
	int[][] r = new int[8][8];
	Color reachColors[];
	Color sumColors[];
	
	public static void setScale(int s) { 
		_scale = s;
		_cellSize = _scale/8;
	}
	
    public ReachableBoard(Piece p, Piece board[][], Piece obstacles[][]) {
    	// FIX: these constructors should set up r[][]
    	_thePiece = p;
    	_obstacles = obstacles;
    	_board = board;
    	initReachColors();
    	initSumColors();
    	
		// start by setting the reachable board with 100 at each obstacle.
		for (int col = 0; col < 8; col++)
			for (int row = 0; row < 8; row++)
				if (obstacles[row][col] != null)
					r[row][col] = 100;
		// mark the starting point
		// row and column are y and x, not x and y
		r[p.location.row][p.location.col] = -1;
    }
    
    public ReachableBoard(Piece p){
    	_thePiece = p;
    	_obstacles = null;
    	initReachColors();
    	r[p.location.row][p.location.col]=-1;
    }
    
    /**
     * initialize reachability color array
     * creates a series of gradually chaning, hopefully visually 
     * appealing, colors used to display increasing distance in
     * a display of reachability.
     */
    protected void initReachColors() {
    	reachColors = new Color[12];
    	for (int i=0; i<12; i+=1) {
    		reachColors[i] = new Color(240- i*20,i*20,240- i*20);
    	}
    }

	protected void initSumColors() {
		sumColors = new Color[12];
		for (int i=0; i<12; i+=1) {
			sumColors[i] = new Color(255-i*20,255-i*20,15+i*20);
		}
	}

  
	/**
	 * paints a black 8x8 grid
	 * uses cellSize and passed-in offset to position and size.
	 * @param g
	 * @param off
	 */
    public static void paintGrid(Graphics g, Point off) {
    	g.setColor(Color.black);
    	int cellSize = _scale/8;
    	for (int row=0; row<8; row++) {
    		for (int col=0; col<8; col++) {
    			g.fillRect(_offset + cellSize * col + off.col, 
    			_offset + cellSize * row + off.row, 
    			cellSize-1, cellSize-1);
    		}
    	}
    }	
    
    
    /**
     * Paint a number in a cell.
     * Needs tweaking for size of font.
     * @param g Graphics
     * @param row
     * @param col
     * @param offset grouped as a point
     */
    protected void numberCell(Graphics g, int row, int col, Point off) {
		g.setColor(Color.black);
		g.drawString("" + r[row][col],
		 (int)(_offset + _cellSize * (col + 0.5)) + off.col, 
		 (int)(_offset + _cellSize * ((7-row)+0.5)) + off.row);
    }
    
	protected static void drawStringInCell(Graphics g, Point p, Point off, String s) {
		g.setColor(Color.white);
		AttributedString as = new AttributedString(s);
		Graphics2D g2d = (Graphics2D) g;
		as.addAttribute(TextAttribute.WEIGHT, TextAttribute.WEIGHT_HEAVY);
		g2d.drawString(as.getIterator(),
		 (int)(_offset + _cellSize * (p.col + 0.5)) + off.col, 
		 (int)(_offset + _cellSize * ((7-p.row)+0.5)) + off.row + _cellSize/4) ;
	}
	
   
   final static int UPPER_RIGHT = 0;
   final static int LOWER_RIGHT = 1;
   final static int LOWER_LEFT = 2;
   final static int UPPER_LEFT = 3;
   final static int CENTER = 4;
   /**
    * getSpotInCell
    * Cenerates a centerpoint for a spot to mark a cell.
    * Can be the center, or any one of four corners.
    * 0 is the upper right, 1 the lower right, 2 the lower left, 3 the upper left
    * 4 is the center.
    * @param p the row/column on the chess board
    * @param offset the offset to the corner of the chess board
    * @param corner the position within the cell 
    */
	protected static Point getSpotInCell(Point p, Point offset, int corner){
			int corner_x=0;
			int corner_y=0;
			switch(corner) {
				case UPPER_RIGHT: 
						corner_x = _cellSize/2;
						corner_y = 0;
						break;
				case LOWER_RIGHT: 
						corner_x = _cellSize/2;
						corner_y = _cellSize/2; 
						break;
				case LOWER_LEFT: 
						corner_x = 0; 
						corner_y = _cellSize/2; 
						break;
				case UPPER_LEFT: 
						corner_x = 0; 
						corner_y = 0;
						break;
				case CENTER: 
						corner_x = _cellSize/4;
						corner_y = _cellSize/4;
						break;
			}
			return new Point(
			   _offset + (_cellSize * (7-p.row))  + offset.row + corner_y ,
               _offset + (_cellSize * p.col)      + offset.col + corner_x); 
		  
			
		} 
		
    /**
     * Paint a cell corner a particular color.
     * corners are numbered: top right, bottom right, bot 
     * @param g Graphics
     * @param c the color
     * @param p the point
     */
	protected static void paintCell(Graphics g, Color c, Point p, Point offset, int corner){
		Color saveColor = g.getColor();
		g.setColor(c);
		Point spotLocation = getSpotInCell(p,offset,corner);
		g.fillOval(spotLocation.col, spotLocation.row, 
			(_cellSize-1)/4 + 3, (_cellSize-1)/4 + 3);
		g.setColor(saveColor);
	}
	protected void testPaintCellWithCorner(Graphics g, Point offset) {
		paintGrid(g, offset);
		paintCell(g, Color.red, new Point(1,1), offset, 0);
		paintCell(g, Color.red, new Point(1,1), offset, 1);
		paintCell(g, Color.red, new Point(1,1), offset, 2);
		paintCell(g, Color.red, new Point(1,1), offset, 3);
	}
    public static void paintCell(Graphics g, Color c, Point p, Point offset){
    	paintCell(g, c, p.row, p.col, offset.col, offset.row);
    }
	static  protected void paintCell(Graphics g, Color c, int row, int col, Point off) {
    	paintCell(g, c, row, col, off.col, off.row);
    }
    static protected void paintCell(Graphics g, Color c, int row, int col, int offx, int offy) {
    	g.setColor(c);
		g.fillOval(_offset + (_cellSize * col) + offx, 
		_offset + _cellSize * (7-row) + offy,
		 _cellSize-1, _cellSize-1);
    }
    static protected void paintLine(Graphics g, Point from, Point to, Point offset, int position) {
		Point from_p = ReachableBoard.getSpotInCell(from, offset, position);
		Point to_p   = ReachableBoard.getSpotInCell(to, offset, position);
		// adjust from and to for the fact that getSpotInCell returns positions
		// apropriate for the upper left corner of a circle, not the center point
		int adjust = (_cellSize/4) +1;
		from_p.col += adjust;
		from_p.row += adjust;
		to_p.col += adjust;
		to_p.row += adjust;
		g.drawLine(from_p.col, from_p.row, to_p.col, to_p.row);
    }
    public void paintObs(Graphics g, Point off, Piece obs[][]){
		paintGrid(g, off);
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				if (obs[col][row]!=null){
					paintCell(g, Color.red, col, row, off );
				}
			}
		}	
    }
    
    /**
     * paint the reachability of the 8x8 board for the piece this
     * reachabilityBoard was created from. Shows increasing distance
     * with darker colors and increasing numbers.
     * @param g
     */
    public void paintBoard(Graphics g, Point off) {
	    	
		// (0,0) is in the lower left of printout
		paintGrid(g, off);
		for (int row = 7; row > -2; row--) {
			for (int col = 0; col < 8; col++) {
				if (row == -1) {
					// print row lables as letters
					//System.out.print("   " + (char) ('a' + col));
				} else {
					if (r[row][col] > 0 && r[row][col] < 100) {
						// reachable in n steps 
						paintCell(g, reachColors[r[row][col]], row, col, off); 
						numberCell(g, row, col, off);
					} else if (r[row][col] == 100) {
						// obstacle
						//System.out.print("   *");
						paintCell(g, Color.red, row, col, off);
					} else if (r[row][col] == -1) {
						// starting point
						paintCell(g, Color.yellow, row, col, off);
					} else if (r[row][col] == 0) {
						// unreachable
					} else {
						// default case prints as character to expose potential bugs
					}
				}
			}
		}
	}
	
	/**
	 * prints an ascii representation of the 8x8 board and
	 * the reachability for the piece this board was created from.
	 */
	public void printBoard() {
		System.out.println(_thePiece.getName() + " at " + _thePiece.location);
		// (0,0) is in the lower left of printout
		for (int row = 7; row >= -1; row--) {
			for (int col = 0; col < 8; col++) {
				if (row == -1) {
					// print row lables as letters
					System.out.print("   " + (char) ('a' + col));
				} else {
					if (r[row][col] > 0 && r[row][col] < 100) {
						// reachable in n steps 
						System.out.print(Utilities.makeWide(""+r[row][col], 4, 'r'));
					} else if (r[row][col] == 100) {
						// obstacle
						System.out.print("   *");
					} else if (r[row][col] == -1) {
						// starting point
						System.out.print("   +");
					} else if (r[row][col] == 0) {
						// unreachable
						System.out.print(Utilities.makeWide(""+0, 4, 'r'));
					} else {
						// default case prints as character to expose potential bugs
						System.out.print((char) r[row][col]);
					}
				}
			}
			// print row number
			System.out.println(" " + (row + 1));
			//System.out.println("");
		}
	}
	
	/**
	 * prints an ascii representation of the 8x8 board and
	 * the reachability for the piece this board was created from.
	 * Considers obstacles.
	 */
	public void printBoard(Piece obstacles[][]) {
			System.out.println(_thePiece.getName() + " with obstacles at " + _thePiece.location);
			// (0,0) is in the lower left of printout
			for (int row = 7; row > -2; row--) {
				for (int col = 0; col < 8; col++) {
					if (row == -1) {
						// print row lables as letter
						System.out.print("   " + (char) ('a' + col));
					} else {
						if (r[row][col] > 0 && r[row][col] < 100) {
							// reachable in n steps 
							System.out.print(Utilities.makeWide("" + r[row][col], 4, 'r'));
						} else if (r[row][col] == 100) {
							// obstacle
							//System.out.print(Utilities.makeWide("" + obstacles[row][col].getSymbol(), 4, 'r'));
							System.out.print(Utilities.makeWide("x", 4, 'r'));
						} else if (r[row][col] == -1) {
							// starting point
							System.out.print("   +");
						} else if (r[row][col] == 0) {
							// unreachable
							System.out.print(Utilities.makeWide(""+0, 4,'r'));
						} else {
							// default case prints as character to expose potential bug
							System.out.println(Utilities.makeWide("" + ((char) r[row][col]), 4, 'r'));
						}
					}
				}
				// print the row number
				System.out.println(" " + (row + 1));
				//System.out.println("");
			}
		}
		
	public Set getSum(Point to)
	{
		return getSum(to, 0);
	}
	
	/**
	 * Computes the value of SUM for a piece to point p.
	 * @param p the "to" point
	 * @param additionalLength length over the minimum, 0 for shortest
	 * @return a ReachableBoard of SUM values
	 * Must be called from a reachable board created from a
	 * Piece, not a ST1, STK or SUM. (FIX?)
	 */
	public Set getSum(Point to, int additionalLength){
		Piece destination;
		try {
			destination = (Piece) _thePiece.clone();
			destination.move(to);
			// PAWN HACK
			if (destination.getClass() == Pawn.class) {
				Pawn d = (Pawn) destination;
				d.setIncreasing(!d.onWhiteSide());
			}
		} catch (Exception x) {
			x.printStackTrace();
			System.err.println("clone problem in Reachable.getsum()" + x);
			return null;
		}
		
		// get a board for the "to" values
		ReachableBoard toRB;
		toRB = destination.getReachableBoard(_board, _obstacles);

		// figure out what the value of sum should be
		int sum = r[to.row][to.col] + additionalLength;
		
//		Point f = new Point(5, 2); 
//		Point t = new Point(6,3);
//System.out.println("SUM : " + sum);
//System.out.println(r[f.row][f.col] + " " + r[t.row][t.col]);
//System.out.println(toRB.r[f.row][f.col] + " " + toRB.r[t.row][t.col]);
//			
		// build the collection of points in the SUM set
		Set al = new TreeSet();
		if (sum == 0) {
			return al;
		}
		for (int row=0; row<8; row++) {
			for (int col=0; col<8; col++) {
				if (r[row][col] + toRB.r[row][col]==sum) {
					Point pt = new Point(row, col); 
					al.add(pt);
				}
				if (r[row][col] == -1) { // start points are marked wierd
					Point pt = new Point(row, col); 
					if (toRB.r[row][col] == sum) {
						al.add(pt);
					}
				}
				if (toRB.r[row][col] == -1) { // start points are marked wierd
					Point pt = new Point(row, col); 
					if (r[row][col] == sum) {
						al.add(pt);
					}
				}
			}
		}
		return al;
	}

	 
	/**
	 * Computes the value of STk for a piece.
	 * @param the number of steps away from the piece's current location to search.
	 * @return a ReachableBoard of STK values.
	 */
	public Set getSTk(int k) {
		Set al = new TreeSet();
		for (int row=0; row<8; row++) {
			for (int col=0; col<8; col++) {
				if (r[row][col]==k) {
					Point pt = new Point(row, col); 
					al.add(pt);
				}
			}
		}
		return al;
	}
	
	/**
	 * paints a set of points a particular color.
	 * @param g
	 * @param c
	 * @param color
	 * @param off
	 */
	public void paintSet(Graphics g, Set c, Color color, Point offset){
		paintSet(g,c,color, offset, 0);
	}
	public void paintSet(Graphics g, Set c, Color color, Point offset, int corner){
		java.util.Iterator iter = c.iterator();
		if (!iter.hasNext()){
			System.out.println("EmPTY SET");
		}
		while (iter.hasNext()){
			Point p = (Point) iter.next();
			paintCell(g, color, p, offset, corner);
		}	
	}

	
	/**
	 * animates trajectory growth
	 * paints each of st1, stk and sum as the trajectory grows 
	 * towards the target.
	 * @param g
	 * @param to
	 * @param level
	 * @param off
	 */
	public void paintTrajectoryDiscovery(Graphics g, Point to, Point offset)
	{
		Utilities.snooze(2000);
		Piece wayPiece=null;
		try {wayPiece = (Piece) this._thePiece.clone();}
		catch(Exception e) { System.err.println("clone exception" + e);}
		int n=1;
		Set intersection=null;
		ArrayList wayPoints = new ArrayList();
		wayPoints.add(this._thePiece.location);
		while (!wayPiece.location.equals(to) && 
			(intersection==null || !intersection.isEmpty())){
			paintCell(g, Color.yellow, _thePiece.location, offset);
			paintCell(g, Color.yellow, to, offset);
			{
				Iterator iter = wayPoints.iterator();
				while (iter.hasNext())
				{
					Point p = (Point) iter.next();
					paintCell(g, Color.RED, p, offset, 4 );
				}
			}
			ReachableBoard wayBoard = wayPiece.getReachableBoard(_board, _obstacles);
			Set st1 = wayBoard.getSTk(1);
			paintSet(g, st1, Color.lightGray, offset, 0);
			Utilities.snooze(500);
			Set stk = getSTk(n);
			paintSet(g, stk, Color.blue, offset, 1);
			Utilities.snooze(500);
			Set sum = getSum(to,0); 
			paintSet(g, sum, Color.cyan, offset, 2);
			Utilities.snooze(500);
		
			intersection = new TreeSet();
			intersection.addAll(st1);
			intersection.addAll(sum);
			intersection.addAll(stk);
			intersection.retainAll(stk);
			intersection.retainAll(sum);
			intersection.retainAll(st1);
			paintSet(g, intersection, Color.magenta, offset, 3);
			Utilities.snooze(1000);
			paintSet(g, st1, Color.black, offset, 0);
			paintSet(g, stk, Color.black, offset, 1);
			java.util.Iterator iter = intersection.iterator();
			if (!iter.hasNext()) {
				System.out.println("No Path!");
				break;
			}
			Point next = (Point) iter.next();
			wayPoints.add(next);
			wayPiece.move(next);
			n++;
		}
		if (wayPiece.location.equals(to)) {
			paintCell(g, Color.RED, to, offset, 4 );
		}
	}
	
	
	/**
	 * paints each of st1, stk, and SUM
	 * @param g
	 * @param to
	 * @param level
	 * @param off
	 */
	public void paintSets(Graphics g, Point to, int level, Point offset){
		paintGrid(g, offset);
		System.out.println("FROM: " + _thePiece);
		paintCell(g, Color.yellow, _thePiece.location, offset);
		System.out.println("TO: " + to);
		paintCell(g, Color.yellow, to, offset);
		Utilities.snooze(500);
		Point p = new Point(4,3);
		Piece wayPiece = null;
		try {wayPiece = (Piece) _thePiece.clone();}
		catch (Exception x) { System.err.println(x); }
		wayPiece.move(p);
		ReachableBoard wayBoard = wayPiece.getReachableBoardNoObs();
		Set st1 = wayBoard.getSTk(1);
		System.out.println("ST1\n" + st1);
		paintSet(g, st1, Color.gray, offset, 0);
		int i=4;
		Set stk = getSTk(i);
		System.out.println("STK\n" + stk);
		paintSet(g, stk, Color.blue, offset, 1);
		Utilities.snooze(250);
		Set sum = getSum(to);
		System.out.println("SUM\n" + sum);
		paintSet(g, sum, Color.cyan, offset, 2);
	}
}