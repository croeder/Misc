import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.List;



/**
 * @author Chris Roeder
 * @version 20040416
 * 
 * 03/17/04 created
 * 04/16/04 added horizon stuff 
 *
 */
public class Bundle {

	public Bundle(){
		trajSet = new ArrayList();
	}
	
	public void add(Trajectory t) {
		trajSet.add(t);
	}
	
	public void remove(Trajectory r) {
		trajSet.remove(r);
	}
	
	public void combine(Bundle b) {
		trajSet.addAll(b.trajSet);
	}
	
public ArrayList trajSet; // FIX
	
	public Trajectory getTrajectory(int i) {
		return (Trajectory) trajSet.get(i);
	}
	
	public Iterator iterator() {
		return trajSet.iterator();
	}
	
	public int getSize() {
		return trajSet.size();
	}
	
	/*
	 * findAdmissableTrajectories()
	 * Finds the admissable trajectories up to additionalLength longer than
	 * the shortest. In this case, just degree 2 are considered.
	 */
	public  Bundle(Piece from, Point to, Piece board[][],
	    Piece obs[][], int horizon,  int additionalLength)
	    // FIX horizon vs additionalLength
	{
		ReachableBoard fromBoard = from.getReachableBoard(board, obs);
	    Set dockPoints = fromBoard.getSum(to, additionalLength);
	    trajSet = new ArrayList();

	    Iterator iter = dockPoints.iterator();
	    while (iter.hasNext()) {
	    	Point p = (Point) iter.next();
	    	trajSet.addAll(Trajectory.findAdmissableTrajectory(from, to, p, board, obs, horizon).trajSet);
	    }
	}
	
	public Bundle(Trajectory t) {
		trajSet.add(t);
	}

	public Bundle(Piece fromPiece, Point to, Piece board[][], Piece obs[][], int horizon) {
		//get a collection of point collections. They represent trajectories, but
		// they aren't collection objects.
		Collection colCollections = findNextStep(fromPiece, fromPiece, to, 1, board, obs);
		trajSet = new ArrayList();

		// Create real Trajectory objects from these collections.
		Iterator iter = colCollections.iterator();
		while (iter.hasNext()) {
			List trajCol = (List) iter.next();
			Trajectory t = null;
			// restrict to trajectories within the horizon
			if (trajCol.size() <= horizon) {
				t = new Trajectory(fromPiece, trajCol);
			}
			trajSet.add(t);
		}
	}

	
	/**
	 * findNextStep
	 * for a start point, a piece and a reachability matrix including
	 * obstacles (all part of the current ReachableBoard object),
	 * a to point, and how many steps into the trajectory we are
	 * find the next step in the trajectory.
	 * 
	 * @return returns a Set of trajectories in the form of an ArrayList each.
	 *
	 * @param fromPiece  the original position
	 * @param wayPiece is the step
	 * @param to  the desired endpoint
	 * @param intersection  a set of points
	 * @param trajectorySet  a set of trajectories as ArrayLists
	 * @param trajectory  an ArrayList of Points
	 * */
	private static List findNextStep(Piece fromPiece, Piece wayPiece, Point to, 
	             int stepNumber, int additionalLength, Piece board[][], Piece _obstacles[][]) {
	             	
		    List trajectorySet = new ArrayList();
		    
		    // Pawn hack
			Piece saveHack = null;
				if (fromPiece.getClass() == Pawn.class){
					saveHack = board[to.row][to.col];
					board[to.row][to.col] = new Pawn(new Point(0,0),  !fromPiece.onWhiteSide());
				}
		    
		    
			Collection intersection=Trajectory.getIntersection(fromPiece, wayPiece, stepNumber, 
			    additionalLength, to, board, _obstacles);
			    
			// pawn hack
			if (saveHack != null) 
				board[to.row][to.col] = saveHack;
			    
			// find the trajectory/ies from here on closer to "to"   	
			if (intersection.size() > 0){
				Iterator iter = intersection.iterator();
				while (iter.hasNext()){
					Point pt = (Point) iter.next();
					if (!pt.equals(to)){
						Piece piece = null;
						try { piece = (Piece) fromPiece.clone(); }
						catch (Exception x) { System.err.println(x); }
						piece.move(pt);
						List subTrajectorySet =  findNextStep(fromPiece, piece, to ,
						         stepNumber +1, board,  _obstacles);
						trajectorySet.addAll(subTrajectorySet);	
					} else {
						// terminal case
						// The first point in the intersection matches the goal point
						// create an array list with it in it and return it in a Set.
						ArrayList traj = new ArrayList();
						traj.add(pt);
						trajectorySet.add(traj);
					}
				}
				//now stitch them onto the point we're at now (_thePiece.location)
				// and return a new set of partial trajectories
				Iterator iter2 = trajectorySet.iterator();
				while (iter2.hasNext()) {
					ArrayList traj = (ArrayList) iter2.next();
					traj.add(0, wayPiece.location);
				}
			} 
		java.lang.Class tSetClass = trajectorySet.getClass();
		return trajectorySet;
	}

	private static List findNextStep(Piece fromPiece, Piece wayPiece, Point to, 
					 int stepNumber, Piece board[][], Piece _obstacles[][]) {
		return Bundle.findNextStep(fromPiece, wayPiece, to, stepNumber, 0, board,  _obstacles);
	}					 	

	public void print() {
			Iterator trajIter = trajSet.iterator();
			while (trajIter.hasNext()){
				Trajectory trajectory = (Trajectory) trajIter.next();	
				trajectory.print();
			}
		}

		public void paint(Graphics g,  Point offset) {
			final Color colors[] = {Color.red,  Color.blue,    Color.green, 
							  Color.cyan, Color.magenta, Color.yellow,
							  Color.gray, Color.orange,  Color.pink };
			final int numColors = 9;
	 	
			Iterator trajIter = trajSet.iterator();
			int i=0;
			while (trajIter.hasNext()){
				Trajectory traj = (Trajectory) trajIter.next();
				//paintTrajectory	
				traj.paint(g,offset, colors[i]);
				i++;
				i = i % numColors;
				//Utilities.snooze(100);
			}
		}


}
