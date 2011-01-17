import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;


import java.awt.Color;

/*
 * Created on Mar 2, 2004
 */

/**
 * Trajectory
 * Represents a Trajectory of a piece from one point to another.
 * @author Chris Roeder
 * 
 * need to fix that the constructors are sort of dumb and that
 * a trajectory isn't fully initialized after "construction".
 * Its the other functions that search for and build a trajectory. FIX
 * 
 * 041704 added horizons
 *
 */
public class Trajectory implements Cloneable {

	private Piece from;
	private List wayPoints; // starts with the from piece and goes somewhere...
	private List times; 
	
	public Iterator iterator() {
		return wayPoints.iterator();
	}
	
	public Point getFromPoint() {
		return from.location;
	}
	
	public Piece getFromPiece() {
		return from;
	}
	
	public Trajectory(Piece f)	{
		from = f;
	}
	
	public Trajectory(Piece f, List points) {
		from = f;
		wayPoints = points;
		initTimes();
	}
	
	/**
	 * returns time for nth step of trajectory.
	 * zero-based, and zero returns a null;
	 * @param n
	 * @return an Integer, null if for the first 
	 */
	public int getTime(int n){
		return ((Integer)times.get(n)).intValue();
	}
	
	/** finds the time for the point in the trajectory matching the point
	 * passed in. 
	 * 
	 * @param p
	 * @return
	 */
	public int getTime(Point p) {
		Iterator iter = wayPoints.iterator();
		int n=0;
		while (iter.hasNext()) {
			Point searchPoint = (Point) iter.next();
			//time value for first point is null in primary trajectory
			if (times != null){
				if (searchPoint!=null && searchPoint.equals(p)) { // FIX n==0
					Integer i = (Integer) times.get(n);
					if (i!=null) {
						if (n==0) {
							return -999;
						}
						return ((Integer) times.get(n)).intValue();
					}
					else {
						return -999;
					}
				}	
				n++;
			}
			else {
				System.out.println("ERROR: times is null in getTime in Trajectory");
			}
		}
		return 0;
	}
	
	/**
	 * inits times for a Primary Trajectory.
	 *
	 */
	public void initTimes() {
		// time value is k+1, where k >0
		// and this is a zero-based list.
		times = new ArrayList();
		int k=0;
		Iterator iter = wayPoints.iterator();
		while (iter.hasNext()) {
			Point p = (Point) iter.next();
			if (k==0) {
				times.add(null);
			}
			else {
				times.add(new Integer(k+1));
			}
			k++;
		}
		
	}
	
	public void initTimesSecondary(int time_y) {
		// time value is time_y - l + 1
		int l = wayPoints.size() -1;
		times = new ArrayList();
		int k=0;
		Iterator iter = wayPoints.iterator();
		while (iter.hasNext()) {
			iter.next();
			times.add(new Integer(time_y -l + 1));
			k++;
		}
		
	}
	
	public int getLength() {
		if (wayPoints != null)
			return wayPoints.size();
		else
			return 0;
	}
	
	/**
	 * tells if passed-in trajectory is a subset of this one.
	 * That means they end at the same place and have some
	 * common positions before that, perhaps all. The requirement
	 * is that they share at least one more point besides the last.
	 * (Is it formally discussed anywhere? FIX)
	 * @param potentialSubset
	 * @return
	 */
	public boolean subset(Trajectory potentialSubset) {
		List potPoints = potentialSubset.wayPoints;
	
		int numWayPoints = wayPoints.size();
		int numPotPoints = potPoints.size();
		
		if (numWayPoints >=2 && numPotPoints >=2){
			return
			       // check end points 
			    (  wayPoints.get(numWayPoints-1).equals(potPoints.get(numPotPoints-1)) &&
				   wayPoints.get(numWayPoints-2).equals(potPoints.get(numPotPoints-2)) ) /*   ||
				   // check start points
				(  wayPoints.get(0).equals(potPoints.get(0)) && 
				   wayPoints.get(1).equals(potPoints.get(1)) ) */
				;
		}
		else {
			return false;
		}
			
	}
	
	public static void testSubset(){
		Piece q = new King(new Point(4,1));
		Trajectory a = new Trajectory(q);
		a.addPoint(new Point(2,2));
		a.addPoint(new Point(3,3));
		a.addPoint(new Point(4,3));
		
		Piece p = new King(new Point(1,1));
		Trajectory b = new Trajectory(p);
		b.addPoint(new Point(3,2));
		b.addPoint(new Point(3,3));
		b.addPoint(new Point(4,3));
	
		if (!(a.subset(b))) {
			System.err.println("subset test 1.1 failed.");
			System.err.println(a + "\n" + b);
		}

		if (!b.subset(a)) {
			System.err.println("subset test 1.2 failed.");
			System.err.println(a + "\n" + b);
		}
			
		Piece r = new King(new Point(4,4));
		Trajectory c =  new Trajectory(r);
		c.addPoint(new Point(7,7));
		
		if (a.subset(c))
			System.err.println("subset test 2.1 failed.");
		if (c.subset(a))
			System.err.println("subset test 2.2 failed.");	
		if (b.subset(c))
			System.err.println("subset test 2.3 failed.");
		if (c.subset(b))
			System.err.println("subset test 2.4 failed");
			
		Piece s = new King(new Point(4,1));
		Trajectory d = new Trajectory(s);
		d.addPoint(new Point(2,3));
		d.addPoint(new Point(3,4));
		d.addPoint(new Point(4,3));
		if (a.subset(d)) 
			System.err.println("subset test 3.1 failed");
		if (d.subset(a))
			System.err.println("subset test 3.2 failed");
		
		Piece t = new King(new Point(4,1));
		Trajectory e = new Trajectory(t);
		e.addPoint(new Point(2,3));
		e.addPoint(new Point(9,8));
		e.addPoint(new Point (11,12));
		if (e.subset(d)) 
			System.err.println("subset test 4.1 failed");
		if (d.subset(e))
			System.err.println("subset test 4.2 failed");
	}
	
	public Object clone() throws CloneNotSupportedException {
		Trajectory t = (Trajectory) super.clone();

		// make it deep
		t.from = (Piece) from.clone();
		// copy the list, but refer to the points
		t.wayPoints = new ArrayList();
		t.wayPoints.addAll(wayPoints);
		return (Object) t;
	}
	
	/**
	 * Add a point to the end of this trajectory's point list.
	 * @param p
	 */
	public void addPoint(Point p) {
		if (wayPoints == null)
			wayPoints = new ArrayList();
		wayPoints.add(p);
		initTimes();
	}
	
	/**
	 * Adds points of passed-in trajectory to this trajectory.
	 * The passed-in points come after this one's points.
	 * @param t
	 */
	public void combine(Trajectory t) {
		wayPoints.addAll(t.wayPoints);
		initTimes();
	}

	/**
	 * create a collection of trajectories that are created by finding every
	 * possible combination of trajectoreis between from and via, and trajectories
	 * between via and to.
	 * @param from
	 * @param to
	 * @param via
	 * @param obs
	 * @return
	 */
	public static Bundle findAdmissableTrajectory(Piece from, Point to, Point via, Piece board[][],
		Piece obs[][], int horizon ) {


		int additionalLength=0;
		Piece viaPiece;
		try {
			viaPiece = (Piece) from.clone();
			viaPiece.move(via);
		} catch (Exception x) {
			x.printStackTrace();
			System.err.println("clone problem in Trajectory.findAdmissableTrajectory()" + x);
			return null;
		}
		// FIX? should the horizon be split between the two below?
		Bundle firstBundle = new Bundle(from, via, board, obs, horizon);
		Bundle secondBundle = new Bundle(viaPiece, to, board, obs, horizon);
		Bundle combinedBundle = new Bundle();
		Iterator firstIter = firstBundle.iterator();
		while (firstIter.hasNext()) {
			Trajectory firstPartTraj = (Trajectory) firstIter.next();
			Iterator secondIter= secondBundle.iterator();
			while (secondIter.hasNext()) {
				Trajectory secondPartTraj = (Trajectory) secondIter.next();
				Trajectory combinedTraj=null;
				try { combinedTraj = (Trajectory) firstPartTraj.clone();}
				catch (Exception x) { System.err.println(x); }
				
				//combinedTraj.combine(firstPartTraj);
				combinedTraj.combine(secondPartTraj);
				combinedBundle.add(combinedTraj);
			}
		}
		return combinedBundle;
	}
	
	/**
	 * finds first trajectory from our point to the given point. Populates
	 * the wayPoints collection with it.
	 * @param to
	 * @param obs
	 * @return
	 */
	public void findFirstTrajectory(Point to, Piece board[][], Piece obs[][], int horizon)
	    throws TrajectoryException {
	
		Piece movingPiece=null;
		wayPoints = new ArrayList();
		try {movingPiece = (Piece) from.clone();}
		catch(Exception e) { System.err.println("clone exception" + e);}
		int n=1;
		wayPoints.add(from.location);
		Set intersection=null;

		while (!movingPiece.location.equals(to) && n <= horizon &&
			   (intersection==null || !intersection.isEmpty())){  
//System.out.println(" LOOKING from: " + movingPiece + " to: " + to + " n= " + n );
			// HACK WARNING
			// only if we're dealing with a pawn,put a pawn from the other team on the "to" spot
			// the reachabiltiy part of pawn that deals with taking on the diagonal gets faked-out properly.
			// Typically reachability doesn't care what's in the board but pawns in this situation do (you can't move diagonally if there's
			// nothing there)
			// FIX FIX FIX there's code in a few places that needs this fix as well:
			// ...bundles as well as Admissable Trajectories.
			Piece saveHack = null;
			if (from.getClass() == Pawn.class){
				saveHack = board[to.row][to.col];
				board[to.row][to.col] = new Pawn(new Point(0,0),  !from.onWhiteSide());
			}
			intersection=Trajectory.getIntersection(from, movingPiece, n, to, board, obs);  	
			if (from.getClass() == Pawn.class) {
				// HACK WARNING put the board back the way it was
				board[to.row][to.col] = saveHack;
			}
			
			java.util.Iterator iter = intersection.iterator();
			if (!iter.hasNext()) {
				System.out.println("No Path! in Trajectory...findFirstTrajectory --> empty intersection"); 
				wayPoints=null;
				throw new TrajectoryException();
			}
			
			// THE pawns problem is right here. It takes the first reachable point and tries
			// that. In the case of the pawn test that's straight ahead. It never sees the
			// point to the right because it comes second and this code is too dumb to 
			// keep looking and find the reachable point we hacked so hard to find.
			Point next = (Point) iter.next();
			wayPoints.add(next);
			movingPiece.move(next);
			n++;
		}
		this.initTimes();

	}

	static Set getIntersection(Piece fromPiece, Piece wayPiece, int n, Point to, Piece board[][], Piece _obstacles[][]){
		return getIntersection(fromPiece, wayPiece, n, 0, to, board, _obstacles);
	}

	// creating RB's everywhere ain't cheap FIX
	static Set getIntersection(Piece fromPiece, Piece wayPiece, int n, 
	         int additionalLength, Point to, Piece board[][], Piece _obstacles[][]){
	         	
		ReachableBoard wayBoard = wayPiece.getReachableBoard(board, _obstacles);
		Set st1 = wayBoard.getSTk(1); // must be from piece at waypoint
		ReachableBoard fromBoard = fromPiece.getReachableBoard(board, _obstacles);
		Set stk = fromBoard.getSTk(n); // must be from piece in original "from" position
		Set sum = fromBoard.getSum(to, additionalLength); // must be from piece in original "from" position
		Set intersection = new TreeSet();
		
/*FIX*/	
		//System.out.println("    from: " + fromPiece + " way: " + wayPiece + " to: " + to);
		//System.out.println("    SETS: " + st1 + "\n" + stk + "\n" + sum);
		intersection.addAll(st1);
		intersection.addAll(sum);
		intersection.addAll(stk);
		intersection.retainAll(stk);
		intersection.retainAll(sum);
		intersection.retainAll(st1);
		
//		if (intersection.size() == 0)
//			System.out.println("    INTERSECTION IS ZERO " + intersection);
//		else 
//			System.out.println("    non-zero intersection" + intersection);
		return intersection;
	}

	public void paint(Graphics g, Point offset) {
		if (from.onWhiteSide()) {
			paint(g, offset, java.awt.Color.white, ReachableBoard.UPPER_LEFT);
		}
		else {
			paint(g, offset, java.awt.Color.red, ReachableBoard.UPPER_RIGHT);
		}
	}
	
	public void paint(Graphics g, Point offset, Color c) {
		paint(g, offset, c, ReachableBoard.CENTER);
	}
	
	public void paint(Graphics g, Point offset, Color c, int position) {
		Color saveColor = g.getColor();
		g.setColor(c);
		Point last=null;
		if (wayPoints == null)
			return;
		Iterator iter = wayPoints.iterator();
		while (iter.hasNext()){
			Point p = (Point) iter.next();
			if (last != null){
		
				// blink to point
				ReachableBoard.paintCell(g, Color.black, last, offset, position);
				Utilities.snooze(10);
				ReachableBoard.paintCell(g, c, last, offset, position);

				// blink from point
				ReachableBoard.paintCell(g, Color.black, p, offset, position);
				Utilities.snooze(10);
				ReachableBoard.paintCell(g, c, p, offset, position);  
				
				ReachableBoard.paintLine(g, p, last, offset, position);
			}
			last =  p;
		}
		g.setColor(saveColor);
	}

	public void print() {
		System.out.println(toString());
	}
	
	public String toString() {
		if (wayPoints == null) {
			return "(no points in trajectory)";
		}
		String temp = from.chessToString() + ": ";
		Iterator iter = wayPoints.iterator();
		int n=0;
		while (iter.hasNext()){
			Point p = (Point) iter.next();
			if (p != null){
				temp += "a" + p.chessToString() + "t-";
				try {
					temp = temp + times.get(n);
				}
				catch (ArrayIndexOutOfBoundsException x) {
				
				}
			}
			n++;
		}
		return temp;
	}

}
