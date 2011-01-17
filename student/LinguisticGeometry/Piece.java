import java.awt.Graphics;

// Chris Roeder 2/5/04
// translated to java 2/14/04
// fixed bugs 2/19/04
// factored out ReachableBoard class 2/19/04


// Base class for Piece hierarchy.
// - manages position and reachability polymorphically
// - has some static functions for dealing with 8x8 boards
//   (soon to be re-factored to a board class)

// Represents a piece at a point and maybe somewhat poorly named as a result FIX

public abstract class Piece implements Cloneable {
	
	boolean white=true;
	public boolean onWhiteSide() {
		return white;
	}
	public boolean opposite(Piece p) {
		return  (white && !p.white) || (!white && p.white);
	}
	/**
	 * tells if a piece at from postion can reach to position in one move.
	 * @param from
	 * @param to
	 * @return true if reachable
	 */
	abstract public boolean reachable(Point from, Point to);

	/**
	 * tells if a piece can reach a position in one move, given obstacles.
	 * @param from
	 * @param to
	 * @param obstacles
	 * @return true if reachable
	 */
	abstract public boolean reachable(
		Point from,
		Point to,
		Piece board[][],
		Piece obstacles[][]);

	/**
	 * returns textual name of symbol. Ex. "King"
	 * @return string name of piece.
	 */
	public abstract String getName();

	/**
	 * returns a character used to represent the piece in an ascii display
	 * @return
	 */
	public abstract char getSymbol();
	public Object clone() throws CloneNotSupportedException {
		Piece p = (Piece) super.clone();

		// make it deep
		p.location = (Point) location.clone();

		p.r = (int[][]) r.clone();

		return (Object) p;
	}

	protected Point location;
	protected int r[][] = new int[15][15];

	public Piece() {
		location = new Point(0, 0);
		r = new int[15][15];
		for (int col = 0; col < 15; col++) {
			for (int row = 0; row < 15; row++) {
				r[row][col] = 0;
			}
		}
		calc15x15();
	}

	public Piece(Point l) {
		this();
		try { location = (Point) l.clone(); }
		catch (Exception x) { System.err.println("clone error in Piece ctor");}
	}
	
	public Piece(Point l, boolean white) {
		this(l);
		this.white = white;
	}

	/**
	 * prints out a piece's 15x15 reachability matrix.
	 *
	 */
	public void print15x15() {
		System.out.println("");
		System.out.println("15x15 for " + this.getName());
		for (int row = 14; row >= 0; row--) {
			for (int col = 0; col < 15; col++) {
				if (row == 7 && col == 7)
					// format
					System.out.print(Utilities.makeWide("+", 4, 'r'));
				else {
					System.out.print(
						Utilities.makeWide("" + r[row][col], 4, 'r'));
				}
			}
			System.out.println();
		}
		System.out.println("");
	}


	public String toString() {
		String team;
		if (onWhiteSide())
			team = "White";
		else
			team = "Black";
		return team + " " + getName() + location;
	}
	
	public String chessToString() {
		String team;
		if (onWhiteSide())
			team = "White";
		else
			team = "Black";
		return team + " " + getName() + location.chessToString();
	}
	
	/**
	 * calculates reachability for an 8x8 board considering obstacles.
	 * @param obstacles
	 * @return
	 */
	public ReachableBoard getReachableBoard(Piece board[][], Piece obstacles[][]) {
        	
		// an obstacle is anything but a 0 in the obstacle array
		ReachableBoard rb;
		if (obstacles != null){
			rb = new ReachableBoard(this, board,  obstacles);
		}
		else {
			rb = new ReachableBoard(this);
		}

		// Eight passes for each pair of points.
		// 100 means its an obstacle
		// 0 means we're not there and you can't start there
		// p > 0 means theres a route there (and you shouldn't re-calculate it)
		// p = -1 means its the start point
		//
		// The first pass is a little interesting in that since the whole board is
		// zeros (uncalculated) you have to start somewhere. The starting point, the
		// location of the piece is marked with -1. Since -1 +1 is 0, there's a
		// special case for making sure the first round of distances from the
		// starting point are 1, not 0 (ouch).

		Point from = new Point();
		Point to = new Point();
		// 8*64*64
		//System.out.println("--------------------------");
		//rb.printBoard();
		for (int pass = 0; pass < 8; pass++) {
			for (from.col = 0; from.col < 8; from.col++) {
				for (from.row = 0; from.row < 8; from.row++) {
					for (to.col = 0; to.col < 8; to.col++) {
						for (to.row = 0; to.row < 8; to.row++) {
							
							////////////// Pawn Hell //////////////////
							/*Point f = new Point(5, 2); 
							Point t = new Point(6,3);
							if (f.equals(from) && t.equals(to) &&
							    rb.r[to.row][to.col] == 0  // not reached before
								&& ( ( rb.r[from.row][from.col] > 0 // reached before
									   && rb.r[from.row][from.col] < 100
									  ) // obstacles
									|| rb.r[from.row][from.col] == -1
									)
								) {
								// pawn test case
								//System.out.println(rb.r[from.row][from.col]);
								//System.out.println(rb.r[to.row][to.col]);
								
							}*/
							/// end Pawn Hell ////////////////////////////
							
							// Blessed mother of short-circuit evalutation be with us now.
							if (rb.r[to.row][to.col] == 0  // not reached before
								&& ((rb.r[from.row][from.col] > 0 // reached before
									&& rb.r[from.row][from.col] < 100) // obstacles
									|| rb.r[from.row][from.col] == -1) // starting point
								&& from.notEqual(to) // this isn't redundant
								&& reachable(
									from,
									to, board,
									obstacles) // piece works that way
							) {

								if (to.row == from.row && to.col == from.col) {
									System.out.println("WTF"); // FIX
								}
								if (rb.r[from.row][from.col] == -1)
									rb.r[to.row][to.col] = 1;
								else
									rb.r[to.row][to.col] =
										rb.r[from.row][from.col] + 1;
							}
						}
					}
				}
			}
			//System.out.println("pass: " + pass);
			//rb.printBoard();
		}
		return rb;
	}
	// TO DO **************************************
	/**
	 * move a piece to a new point, recalculate its reachability considering obstacles.
	 *
	 */
	public void move(Point to) {
		try { location = (Point) to.clone(); }
		catch (Exception x) { System.err.println("clone error in move");
		}
		calc15x15();
	}

	/**
	 * Check reachability from the 15x15 in terms of a point on the 8x8
	 * Answers the question, what's the reachability 
	 *
	 */
	public boolean reachableSimple(Point p) {
		return false;
	}

	/**
	 * calculate the piece's 15x15 reachability matrix with no obstacles.
	 *
	 */
	protected void calc15x15() {
		Point from = new Point(7, 7);
		doPass(from);
		for (int pass = 1; pass <= 8; pass++) {
			for (int col = 0; col < 15; col++) {
				for (int row = 0; row < 15; row++) {
					if (r[row][col] == pass) {
						Point testPoint = new Point(row, col);
						doPass(testPoint);
					}
				}
			}
		}
	}

	/**
	 * Check if piece is blocked on straight lines.
	 * @param from starting point
	 * @param to ending point
	 * @param obstacles array of pieces. Null means there's no piece there.
	 * @return true if blocked.
	 */
	protected boolean blocked(Point from, Point to, Piece obstacles[][]) {
		// verify its Diagonal or Horiz. or Vert.
		//   return NON-blocked if it isnt
		// UNLESS its a Knight
		int delta_col = Math.abs(from.col - to.col);
		int delta_row = Math.abs(from.row - to.row);
		if (!(  delta_row == delta_col
			|| (delta_col != 0 && delta_row == 0)
			|| (delta_col == 0 && delta_row != 0)
			|| (delta_col == 0 && delta_row == 0))) {
			return true;
		}

		int col_inc;
		if (from.col < to.col)
			col_inc = 1;
		else
			col_inc = -1;

		int row_inc;
		if (from.row < to.row)
			row_inc = 1;
		else
			row_inc = -1;

		if (delta_col > 0 && delta_row > 0) {
			// diagonal
			for (int col = from.col; col != to.col; col += col_inc) {
				for (int row = from.row; row != to.row; row += row_inc) {
					if (obstacles[row][col] != null) {
						return true;
					}
				}
			}
		} else if (delta_col == 0 && delta_row > 0) {
			// vertical
			for (int row = from.row; row != to.row; row += row_inc) {
				if (obstacles[row][from.col] != null) {
					return true;
				}
			}
		} else if (delta_row == 0 && delta_col > 0) {
			// horizontal
			for (int col = from.col; col != to.col; col += col_inc) {
				if (obstacles[from.row][col] != null) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * do one pass of checking the entire board for reachable places for the piece.
	 * @param from the starting point
	 */
	protected void doPass(Point from) {
		for (int col = 0; col < 15; col++) {
			for (int row = 0; row < 15; row++) {
				Point to = new Point(row, col);
				if (r[row][col] < 1
					&& reachable(from, to)
					&& !(col == from.col && row == from.row)) {
					r[row][col] = r[from.row][from.col] + 1;
				}
			}
		}
	}

	/**
	 * init an 8x8 array
	 * @param r the array to init.
	 * @param i the initial value for each cell.
	 */
	protected static void init(int r[][], int i) {
		for (int x = 0; x < 8; x++)
			for (int y = 0; y < 8; y++)
				r[x][y] = i;
	}

	/**
	 * Computes reachability for a piece to all places on the 8x8.
	 * @return 
	 */
	public ReachableBoard getReachableBoardNoObs() {
		ReachableBoard rb = new ReachableBoard(this);
		//  you supply the array, we fill it
		// no obstacles
		// take the 15x15, position it at p and copy
		Point origin = new Point(7 - location.row, 7 - location.col);
		for (int col = 0; col < 8; col++) {
			for (int row = 0; row < 8; row++) {
				if (row == (location.row) && col == (location.col)) {
					rb.r[row][col] = -1;
				} else {
					rb.r[row][col] = r[origin.row + row][origin.col + col];
				}
			}
		}
		return rb;
	}
	public void paint(Graphics g, Point off) {
		java.awt.Color color=null;
		if (onWhiteSide()) {
			color = java.awt.Color.gray;
		}
		else {
			color = java.awt.Color.pink;
		}
		//ReachableBoard.paintCell(g, color, location, off);
		String s = "" + this.getSymbol();
		if (this.onWhiteSide()) {
			s += "w";
		}
		else {
			s += "b";
		}
		ReachableBoard.drawStringInCell(g, location, off, s);
	}
}

