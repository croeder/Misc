/*
 * Created on Mar 16, 2004
 *
 */

/**
 * @author Chris Roederj
 * @version 20040409
 */

import java.util.Collection;
import java.util.ArrayList;

import java.util.Iterator;
import java.awt.Graphics;
import java.util.List;

public class Zone implements Cloneable {

	private Collection trajectories = new ArrayList();
	//private ArrayList trajLevels = null;
	private Trajectory primaryTrajectory;
	private int snoozeTime = 0;
			
	/**
	 * findAttacker finds the next piece on the board, starting ata
	 * particular location and returns its location. An 8x8 board is assumed.
	 * @param start
	 * @param board
	 * @param obs
	 * @return Point containing coordinates of found piece. If coordinates are -1,-1
	 *  then nothing was found.
	 */
	private static  boolean findAttacker(Point start, Piece board[][]) {
		boolean findAnything=false;
		while (start.col < 8 && start.row < 8 &&
		       board[start.row][start.col] == null) {
			start.next();
		}
		if (start.col < 8 && start.row < 8) {
			findAnything=true;
		}
		return findAnything;
	}
	
	private void addTrajectory(Trajectory t) {
		trajectories.add(t);
	}
	
	/**
	 * builds the starting point of a zone for use with bundle functions below.
	 * @param t
	 */
	private Zone(Trajectory t) {
		primaryTrajectory = t;
		primaryTrajectory.initTimes();
	}
	
	public Object clone() throws CloneNotSupportedException {
			Zone z = (Zone) super.clone();

			// make it deep
			z.trajectories = new ArrayList();
			Iterator i = trajectories.iterator();
			while (i.hasNext()) {
				Trajectory t = (Trajectory) ((Trajectory) i.next()).clone();
				z.trajectories.add(t);
			}
			z.primaryTrajectory = (Trajectory) primaryTrajectory.clone();

			return (Object) z;
		}
	
//	/**
//	 * creates a zone from a primary trajectory, searching for negation trajectories. 
//	 * When  more than one trajectory is found, (a bundle) use just the first (limitation). 
//	 * @param primaryTraj
//	 * @param board
//	 * @param obs
//	 * @param horizon
//	 */	
//	public Zone(Trajectory primaryTraj, Piece board[][], Piece obs[][], int horizon) {
//		// ** Q: need to limit ourselves to a horizon limit!!
//		// calculate times for each position on the trajectory
//// loop:
//		// pick a negation attacker (not on form and not on to)
//		// id his trajectory to some previously discovered trajectory
//		// calculate times
//		// if within time limits of previous traj, include it
//		// **calculate times for the traj.
//
//					
//		primaryTrajectory = primaryTraj;
//		primaryTrajectory.initTimes();
//
//		// build  array of levels		
//		int level=0;
//		trajLevels = new ArrayList();
//		trajLevels.add(new ArrayList());
//		((ArrayList) trajLevels.get(level)).add(primaryTrajectory);
//		boolean more = true;
//
//	
//		while (more && level < 20) {
//			level++;
//			more = false;
//			Iterator iter = ((ArrayList) trajLevels.get(level-1)).iterator();
//			while (iter.hasNext()){
//				Trajectory targetTraj = (Trajectory) iter.next();
//				System.out.println("\n\ninvestigating " + targetTraj);
//				ArrayList negationTrajs = findNegationTrajectories(targetTraj, board, obs, horizon);
//				if (negationTrajs != null && negationTrajs.size() > 0) {
//					ArrayList newLevel=null;
//					more = true;
//					try {
//						newLevel = (ArrayList) trajLevels.get(level);
//					}
//					catch (IndexOutOfBoundsException x) {
//						trajLevels.add(newLevel = new ArrayList());
//					} 
//					newLevel.addAll(negationTrajs);
//					
//					//((ArrayList)trajLevels.get(level)).addAll(negationTrajs);
//				}
//			}
//		}
//
//		// flatten
//		for (int i=0; i<level; i++) {
//			ArrayList trajlevel = (ArrayList) 
//			trajLevels.get(i);
//			trajectories.addAll(trajlevel);
//		} 
//		
//	}

	private ArrayList findNegationTrajectories(Trajectory primaryTraj, Piece board[][], Piece obs[][], int horizon) {
		ArrayList trajectories = new ArrayList();		
		// find a place on the traj. to attack
		Iterator primaryTrajIter = primaryTraj.iterator();
		

		while (primaryTrajIter.hasNext())
		{
			// pick point to attack
			Point target = (Point) primaryTrajIter.next();
			//System.out.println("targetting: " + target.chessToString());
			
			// find attacker
			Point attackerPoint = new Point(0,0); //x as in rule 3 of the grammar, the starting point of 
						  // a negation trajectory.		
			while (findAttacker(attackerPoint, board)) {
				// get the attacker piece
				Piece attackerPiece = (Piece) board[attackerPoint.row][attackerPoint.col];
				//System.out.print("  attacking with: " + attackerPiece.chessToString());
				
				// Make sure you're not targetting the beginning or end of
				// the trajectory.
				// Make sure you're not targetting yourself.
				if (!attackerPiece.equals(primaryTraj.getFromPiece())) {
					// find trajectory between the two.
					Trajectory negationTraj = new Trajectory(attackerPiece);
					try {
						//negationTraj.findFirstTrajectory(target, board, obs, horizon);
						Bundle bun = new Bundle(attackerPiece, target, board, obs, horizon);
						Iterator iter = bun.iterator();
						negationTraj = null;
						if (iter.hasNext()) {
  							negationTraj = (Trajectory) iter.next();
							int targetTime = primaryTraj.getTime(target);
							if (negationTraj == null){
								 //System.out.println("\nWTF? NullPointerEXception in Zone.findNegationTrajectories??");
							}
							else negationTraj.initTimesSecondary(targetTime);
						}

						// make sure this doesn't share a subset with the 
						// traj. we're attacking
						if ( negationTraj != null && negationTraj.getLength() > 0        &&
							 negationTraj.getLength() <= horizon && 
							 !primaryTraj.subset(negationTraj) )  { 	
							if (attackerPiece.opposite(primaryTraj.getFromPiece())) {
								if (negationTraj.getLength() -1 <= primaryTraj.getTime(target)) {
									//System.out.println(" negating trajectory accepted.");
									trajectories.add(negationTraj);
								}
								else {
									//System.out.println("  Opposing trajectory denied because of time: "
									//   + primaryTraj.getTime(target) + " length: " + negationTraj.getLength());
								}
							}
							else {
								if (negationTraj.getLength()==1) {
									//System.out.println(" negating trajectory accepted.");
									trajectories.add(negationTraj);
								}
								else {
									//System.out.println("  Supporting trajectory denied because of time: "
									// +  negationTraj.getLength());
								}
							}
						}
						else if (negationTraj != null){
							//System.out.println("  trajectory denied because its a subset or the length was off" 
							//+ (negationTraj.getLength()-1));
						}
						else {
							//System.out.println("  trajectory denied because it didn't exist.");
						}
					}

				/*	catch(TrajectoryException x) {
						System.out.println("  not reachable.");
					}
*/					catch (Exception x) {
						//System.out.println(" Exception in Zones..." + x + "\n" );
						x.printStackTrace();
					}
				}
				else {
					//System.out.println("  that's the same piece!\n   ");
				}
				// start searching for the next one, one point over
				attackerPoint.next();
			}
		}	
		return trajectories;
	}
	
/////////////////////////////////////////////
	
	/**
	 * find bundles of negation trajectories and return them for a particular trajectory.
	 * Practically a cut-n-paste of findNegationTrajectory, but returns the entire bundle
	 * instead of just the first trajectory. Debugging println's taken out here because this
	 * line of code (doing bundles) will experience quite a combinatoral explosion.
	 * 
	 * @param primaryTraj the trajectory to attack
	 * @param board the state of the board
	 * @param obs the obstacles on the board (getting old and unused...)
	 * @param horizon the maximum length of trajectories
	 * 
	 * @return an ArrayList of bundles, not just a flat ArrayList of trajectories
	 */
	private static  ArrayList findNegationBundles(Trajectory primaryTraj, Piece board[][], Piece obs[][], int horizon) {
		ArrayList bundles = new ArrayList();		
		// find a place on the traj. to attack
		Iterator primaryTrajIter = primaryTraj.iterator();
		
		while (primaryTrajIter.hasNext())
		{
			// pick point to attack
			Point target = (Point) primaryTrajIter.next();
			//System.out.println("targetting: " + target.chessToString());
			
			// find attacker
			Point attackerPoint = new Point(0,0); //x as in rule 3 of the grammar, the starting point of 
			              // a negation trajectory.		
			while (findAttacker(attackerPoint, board)) {
				// get the attacker piece
				Piece attackerPiece = (Piece) board[attackerPoint.row][attackerPoint.col];
				//System.out.print("  attacking with: " + attackerPiece.chessToString());
				
				// Make sure you're not targetting the beginning or end of
				// the trajectory.
				// Make sure you're not targetting yourself.
				if (!attackerPiece.equals(primaryTraj.getFromPiece())) {
					// find trajectory between the two.
					Trajectory negationTraj = new Trajectory(attackerPiece);
					try {
						//negationTraj.findFirstTrajectory(target, board, obs, horizon);
						Bundle bun = new Bundle(attackerPiece, target, board, obs, horizon);
						Iterator iter = bun.iterator();
						
						// consider each trajectory in the bundle, remove from bundle if not worthy
						while (iter.hasNext()) {
							negationTraj = (Trajectory) iter.next();
							int targetTime = primaryTraj.getTime(target);
							negationTraj.initTimesSecondary(targetTime);
	
							// check all sorts of conditions..
							if ( negationTraj.getLength() > 0        &&
							     negationTraj.getLength() <= horizon && 
							     !primaryTraj.subset(negationTraj) )  { 	
								if (attackerPiece.opposite(primaryTraj.getFromPiece())) {
									if (negationTraj.getLength() -1 <= primaryTraj.getTime(target)) {
										// Here's the add:
										//trajectories.add(negationTraj);
									}
									else {
										//System.out.println("  Opposing trajectory denied because of time: "
										  // + primaryTraj.getTime(target) + " length: " + negationTraj.getLength());
										  bun.remove(negationTraj);
									}
								}
								else {
									if (negationTraj.getLength()==1) {
										//System.out.println(" negating trajectory accepted.");
										//trajectories.add(negationTraj);
									}
									else {
										//System.out.println("  Supporting trajectory denied because of time: "
										 //+  negationTraj.getLength());
										 bun.remove(negationTraj);
									}
								}
							}
							else {
								//System.out.println("  trajectory denied because its a subset or the length was off" 
								//+ (negationTraj.getLength()-1));
								bun.remove(negationTraj);
							}
						} // end-while
						// ok bundle is clean, add it to the things to return
						if (bun.getSize() > 0) {
							bundles.add(bun);
						} 
					}

				/*	catch(TrajectoryException x) {
						System.out.println("  not reachable.");
					}
*/					catch (Exception x) {
						//System.out.println(" Exception in Zones..." + x);
					}
				}
				else {
					//System.out.println("  that's the same piece!\n   ");
				}
				// start searching for the next one, one point over
				attackerPoint.next();
			}
		}	
		return bundles;
	}
	
	/**
	 * 
	 * @return an array list of levels, where a level is an array list of bundles of nth negation trajectories
	 */
	public static List getZones(Trajectory t, Piece board[][], Piece obs[][], int horizon) {	
		Zone z = new Zone(t);
		return getZonesR(z, t, board, obs, horizon, 1);
	}
	protected static List getZonesR(Zone z, Trajectory targetTrajectory, Piece board[][], Piece obs[][], int horizon, int recursiveDepth) {
		System.out.println("recursive depth " + recursiveDepth);
		// create array of zones
		int level=0;
		ArrayList zones = new ArrayList();
		boolean more = true;
		ArrayList newTrajectories = new ArrayList();
	
	
		ArrayList negationBundles = findNegationBundles(targetTrajectory, board, obs, horizon);
		System.out.println("FOUND " + negationBundles.size() + " negation bundles");
		// At this point I have  a zone with a list of negation bundles for *one* of its trajectories.
		// I need to go deep and across to finish this out.
		// Deep to get all the (n+1)th negations for *this* trajectory,
		// but also across for the other trajectories in the bundle.
		
		// just handling this level is crazy enough. If I have 4 bundles of 4,3, 5 and 2 trajectories
		// each, I need to find all the combinations: 4 * 3 * 5 *2 = 120 in this case.
		// I start to think about this like its a 4 digit number (though other examples will have 
		// varying amounts of digits), and then realize that each column has a different
		// number of possibilities so the number analogy gets weak. Anyway I count like its a weird number:
		// 0,0,0,0; 0,0,0,1; 0,0,1,0; 0,0,1,1; 0,0,2,0; 0,0,2,1; 0,0,3,0; 0,0,3,1; 0,0,4,0; 0,0,4,1; 0,0,5,0; 0,0,5,1
		// 0,1,0,0; 0,1,0,1; 0,1,1,0; 0,1,1,1; 0,1,2,0; 0,1,2,1; 0,1,3,0; 0,1,3,1; 0,1,4,0; 0,1,4,1; 0,1,5,0; 0,1,5,1
		// 0,2,0,0; etc.
		int numCombinations =1;
		int numDigits = negationBundles.size(); // same as numBundles would be
		int bundleCounts[] = new int [numDigits];
		int bundleMaxes[] = new int [numDigits]; 
		for (int i=0; i<negationBundles.size(); i++) {
			bundleCounts[i] = 0;
			numCombinations *= ((Bundle) negationBundles.get(i)).getSize();
			bundleMaxes[i] = ((Bundle) negationBundles.get(i)).getSize();
			//System.out.println("bundle " + i + " has " + bundleMaxes[i] + " trajectories.");
		}
		
		int numZones=0;
		System.out.println("num combinations is " + numCombinations);
		for (int i=0; i<numCombinations; i++) {
				int carry=1;
				// calculate the next number
				for (int col=numDigits-1; col>=0; col-- ){
					if (carry > 0) {
						bundleCounts[col] += carry;
						if (bundleCounts[col] == bundleMaxes[col]) {
							bundleCounts[col] = 0;
							carry=1;
						}
						else {
							carry=0;
						}
					}
				}
				
				// Use the number to build a new zone. The column number tells you which bundle to 
				// go for, the number for that column tells you which trajectory to take.
				Zone newZone=null;
				try {
					// FIX, this clone makes  a copy of every trajectory. Consider just making those properly
					// immutable and saving the time.
					newZone = (Zone) z.clone();
					//System.out.println("SUCCUESSFUL CLONE");
				}
				catch (CloneNotSupportedException x) {
					System.out.println(x);
				}
				System.out.print("building new zone from combinations: ");
				for (int col=0; col<numDigits; col++) {
					//System.out.println(" bundle " + col + " trajectory: " + bundleCounts[col]);
					Bundle bun = (Bundle) negationBundles.get(col);
					Trajectory traj = bun.getTrajectory(bundleCounts[col]);
					newZone.addTrajectory(traj);
					newTrajectories.add(traj);
				}
				
				// RECURSIVE CALL
				// Now call this function recursively to go deep on the newly created zone.
				// It needs to be called once for each *trajectory* added...one from each of the new bundles.
				// Somehow I need to keep track of the new trajectories in each new Zone.
				Iterator newTrajIter = newTrajectories.iterator();
				while (newTrajIter.hasNext()) {
					Trajectory t = (Trajectory) newTrajIter.next();
					List newZoneList=null;
					if (recursiveDepth < 2){
						newZoneList = getZonesR(newZone, t, board, obs, horizon, recursiveDepth + 1);
					}
					else {
						newZoneList = new ArrayList();
						newZoneList.add(newZone);;
					}
					if (newZoneList != null)
						zones.addAll(newZoneList);
				}
				// Since its the same zone in each call, just a different trajectory to focus on,
				// The zone gets built up properly. There are not many different zones to re-assemble.
			}
			System.out.println("returning " + zones.size());
			return zones;
	}

			
		
	public void print() {
		System.out.println(toString());
	}
	
	
	/**
	 * toString returns a string representation of the zone. (Incomplete) FIX
	 */
	public String toString() {
		String temp="Zone: primary trajectory is: \n  " +
		primaryTrajectory + "\n" + trajectories.size() + " Others:  \n  ";
		
		Iterator iter = trajectories.iterator();
		while (iter.hasNext()) {
			Trajectory t = (Trajectory) iter.next();
			temp += t.toString() + "\n";
		}
			
			
		return temp;
	}
	
	public void paint(Graphics g, Point off, Piece board[][]) {
		// paint board
		ReachableBoard.paintGrid(g, off);
		Point p = new Point(0,0);

		for (p.col=0; p.col<8; p.col++) {
			for (p.row=0; p.row<8; p.row++) {
				if (board[p.row][p.col]!=null) {
					board[p.row][p.col].paint(g, off);				
				}
			}
		}

		//if (b)
		// paint trajectories
		Iterator iter = trajectories.iterator();
		while (iter.hasNext()) {
			Trajectory t = (Trajectory) iter.next();
			t.paint(g, off);
			//Utilities.snooze(snoozeTime);
		}
	}
}
