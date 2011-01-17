/*
 * Created on Apr 18, 2004
 *
 */

/**
 * @author Chris Roeder
 *
 * Dummy piece for pawn promotion. Has no reachability so it can't move.
 */
public class TargetZone extends Piece {

	/**
	 * 
	 */
	public TargetZone() {
	}

	/**
	 * @param l
	 */
	public TargetZone(Point l) {
		super(l);
	}

	/**
	 * @param l
	 * @param white
	 */
	public TargetZone(Point l, boolean white) {
		super(l, white);
	}

	/* (non-Javadoc)
	 * @see Piece#reachable(Point, Point)
	 */
	public boolean reachable(Point from, Point to) {
		return false;
	}

	/* (non-Javadoc)
	 * @see Piece#reachable(Point, Point, Piece[][], Piece[][])
	 */
	public boolean reachable(
		Point from,
		Point to,
		Piece[][] board,
		Piece[][] obstacles) {
		return false;
	}

	/* (non-Javadoc)
	 * @see Piece#getName()
	 */
	public String getName() {
		return "Dummy Target";
	}

	/* (non-Javadoc)
	 * @see Piece#getSymbol()
	 */
	public char getSymbol() {
		return 'T';
	}

}
