/**
 * Chris's New Piece
 * can move like a king, but only in the dimensions of a rook.
 * A remotely realistic application would be a car with a fixed
 * speed on a city street grid.
 * @author Chris Roeder
 *
 */
public class NewPiece extends Piece {

	public NewPiece() {
		calc15x15();
	}
	public NewPiece(Point pt) {
		super(pt);
		calc15x15();
	}
	public boolean reachable(Point from, Point to) {
		int delta_row = Math.abs(from.row - to.row);
		int delta_col = Math.abs(from.col - to.col);
		return (delta_row == 1 && delta_col == 0) ||
			   (delta_row == 0 && delta_col == 1);
	}
	public boolean reachable(Point from, Point to, Piece board[][], Piece obstacles[][]) {
		return reachable(from, to) && !blocked(from, to, obstacles);
	}
	public String getName() {
		return "NewPiece";
	}
	public char getSymbol() {
		return 'C'; // 'N' and 'P' have been used...
	}
	protected boolean blocked(Point from, Point to, Piece obstacles[][]) {
		// Knight can jump so its only blocked if the to point
		// has an obstacle.
		return (obstacles[to.row][to.col] != null);
	}

}
