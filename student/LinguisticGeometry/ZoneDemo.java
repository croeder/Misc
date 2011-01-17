/*
 * Created on Mar 19, 2004
 *
 */

import javax.swing.JButton;
import javax.swing.JTextArea;
import java.awt.event.*;
import javax.swing.JScrollPane;
import java.awt.Graphics;
import java.util.Iterator;
import java.util.List;


/**
 * Displays work for project 2.
 * @author Chris Roeder
 *
 */
public class ZoneDemo extends javax.swing.JFrame {
	JButton zoneBtn = new JButton();
	JButton retiBtn = new JButton();
	JButton pawnBtn = new JButton();
	JButton zoneBtn2 = new JButton();
	JButton dummyBtn = new JButton();
	JTextArea jtext = new JTextArea();
	JScrollPane jPane = new JScrollPane(jtext);
	
	Point fromPoint = new Point(4, 1);
	//Piece fromPiece = new King(fromPoint);
	Piece fromPiece = new Queen(fromPoint);
	
	private int zoneChoice=2;
	
	public static void main(String args[]) {
		ZoneDemo d = new ZoneDemo();
		d.setSize(950, 500);
		d.setVisible(true);
	}

	public ZoneDemo() {
		this.getContentPane().setLayout(null);
		int btnWidth = 120;
		int btnHeight = 25;

		zoneBtn.setBounds(5, 10, btnWidth, btnHeight);
		zoneBtn.setText("1st Zone");
		this.getContentPane().add(zoneBtn);
		zoneBtn.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				zoneChoice=1;
				repaint();
			}
		});
		retiBtn.setBounds(100, 10, btnWidth, btnHeight);
		retiBtn.setText("Reti Zone");
		this.getContentPane().add(retiBtn);
		retiBtn.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					zoneChoice=2;
					repaint();
				}
			});
			
		pawnBtn.setBounds(200, 10, btnWidth, btnHeight);
				pawnBtn.setText("Pawn Test");
				this.getContentPane().add(pawnBtn);
				pawnBtn.addMouseListener(new MouseAdapter() {
						public void mouseClicked(MouseEvent e) {
							zoneChoice=3;
							repaint();
						}
					});
		zoneBtn2.setBounds(300, 10, btnWidth, btnHeight);
		zoneBtn2.setText("new Zones");
		this.getContentPane().add(zoneBtn2);
		zoneBtn2.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				zoneChoice=4;
				repaint();
			}
		});
		
		dummyBtn.setBounds(400, 10, btnWidth, btnHeight);
		dummyBtn.setText("Dummy Test");
		this.getContentPane().add(dummyBtn);
		dummyBtn.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				zoneChoice=5;
				repaint();
			}
		});
		
		jPane.setBounds(400,50, 500, 350);
		this.getContentPane().add(jPane);
		
	}
	private void validateBoard(Piece board[][]) {
		for (int col=0; col<8; col++)
			for (int row=0; row<8; row++)
				if (board[row][col]!=null)
				if (board[row][col].location.col != col ||
					board[row][col].location.row != row) {
					System.out.print("board validation " + row + " " + col);
					System.out.println("     " + board[row][col].location);
				}
	}

		Piece z = null;
		
	
		
		Piece obsNone[][] = { 
			  { z, z, z, z, z, z, z, z }, {
				z, z, z, z, z, z, z, z }, {
				z, z, z, z, z, z, z, z }, {
				z, z, z, z, z, z, z, z }, {
				z, z, z, z, z, z, z, z }, { 
				z, z, z, z, z, z, z, z }, {
				z, z, z, z, z, z, z, z }, {
				z, z, z, z, z, z, z, z }
		};
	Point off9 = new Point(50, 10);
	public void paint(java.awt.Graphics g) {
		super.paint(g);
		// Zones

		ReachableBoard.setScale(350);
		switch (zoneChoice) {
			case 1: drawZone(g);
				break;
			case 2: drawRetiZone2(g);
			    break;
			case 3: drawPawnTest(g);
				break;
			case 4: drawZone2(g);
				break;
			case 5: drawDummyTest(g);
		}
	}
	
	public void drawZone(Graphics g) {
			//		"real" pieces
			 Piece Kb = new King(  new Point(6, 1), false);
			 Piece Pb = new Pawn(  new Point(4, 4), false);
			 Piece Bw = new Bishop(new Point(1, 5), true);
			 Piece Kw = new King(  new Point(0, 1), true);
			 Piece Kw2 = new King( new Point(4, 5), true);
			 Piece Nb = new Knight(new Point(6, 6), false);
			 Piece Pw = new Pawn(  new Point(4, 3), true);
			 Piece Pw2 = new Pawn( new Point(2, 2), true);
		
			 Piece board[][] = {
				 {z,Kw, z, z, z, z, z, z},
				 {z, z, z, z, z,Bw, z, z},
				 {z, z,Pw2,z, z, z, z, z},
				 {z, z, z, z, z, z, z, z},
				 {z, z, z,Pw,Pb, z, z, z},
				 {z, z, z, z, z, z, z, z},
				 {z,Kb, z, z, z, z,Nb, z},
				 {z, z, z, z, z, z, z, z}
			 };
		Trajectory tr = new Trajectory(Bw);
//			try {
//				tr.findFirstTrajectory(Pb.location, board, obsNone, 5); 
//				tr.paint(g, off9, java.awt.Color.yellow);
//				Zone testZone = new Zone(tr, board, obsNone, 5);
//				testZone.paint(g, off9, board);
//				//testZone.print();
//				g.setColor(java.awt.Color.black);
//				jtext.setText(testZone.toString());
//			}
//			catch (Exception x) {
//				System.out.println("int ZoneDemo.drawZone() caught: " + x);
//				x.printStackTrace();
//			}
	}
//	public void drawRetiZone(Graphics g) {
//			//		"real" pieces
//			 Piece Kb = new King(  new Point(5, 0),  false);
//			 Piece Pb = new Pawn(  new Point(4, 7),  false);
//			 Piece Kw = new King(  new Point(7, 7),  true);
//			 Piece Pw = new Pawn(  new Point(5, 2), true);
//		
//		Piece reti[][] = {
//				{ z, z, z, z, z, z, z, z},
//				{ z, z, z, z, z, z, z, z},
//				{ z, z, z, z, z, z, z, z},
//				{ z, z, z, z, z, z, z, z},
//				{ z, z, z, z, z, z, z,Pb},
//				{Kb, z,Pw, z, z, z, z, z},
//				{ z, z, z, z, z, z, z, z},
//				{ z, z, z, z, z, z, z,Kw}
//			};
//			validateBoard(reti);
////		Trajectory tr = new Trajectory(Kw);
////			try {
////				tr.findFirstTrajectory(Pb.location, reti, obsNone, 5); 
////				tr.paint(g, off9, java.awt.Color.yellow);
////				Zone testZone = new Zone(tr, reti, obsNone, 5);
////				testZone.paint(g, off9, reti);
////				//testZone.print();
////				g.setColor(java.awt.Color.black);
////				jtext.setText(testZone.toString());
////			}
////			catch (Exception x) {
////				System.out.println("in ZoneDemo.drawReti" + x);
////				x.printStackTrace();
////			}
//	}
	public void drawRetiZone2(Graphics g) {
		//		"real" pieces
		Piece Kb = new King(  new Point(5, 0),  false);
		Piece Pb = new Pawn(  new Point(4, 7),  false);
		Piece Kw = new King(  new Point(7, 7),  true);
		Piece Pw = new Pawn(  new Point(5, 2),  true);
		Piece Tw = new TargetZone( new Point(0, 7), true);
		Piece Tb = new TargetZone( new Point(7, 2), false);
	
		Piece reti[][] = {
				{ z, z, z, z, z, z, z,Tw},
				{ z, z, z, z, z, z, z, z},
				{ z, z, z, z, z, z, z, z},
				{ z, z, z, z, z, z, z, z},
				{ z, z, z, z, z, z, z,Pb},
				{Kb, z,Pw, z, z, z, z, z},
				{ z, z, z, z, z, z, z, z},
				{ z, z,Tb, z, z, z, z,Kw}
		};
		validateBoard(reti);
		System.out.println("Finding Reti Zones: Black King to White Pawn");
		Trajectory tr = new Trajectory(Kb);
		try {
			tr.findFirstTrajectory(Pw.location, reti, obsNone, 6); 
			List zones = Zone.getZones(tr, reti, obsNone, 6);
			System.out.println("FOUND 1: " + zones.size() + " zones in search.");
			Iterator ziter = zones.iterator();
			while (ziter.hasNext()) {
				Zone z = (Zone) ziter.next();
				z.paint(g, off9, reti);  
				g.setColor(java.awt.Color.black);
				jtext.setText(z.toString());
				z.print();
				System.out.println("");
			}
			tr.paint(g, off9, java.awt.Color.yellow);
		}
		catch (Exception x) {
								//System.out.println("in ZoneDemo.drawReti2 " + x );
								//x.printStackTrace();
		}
		System.out.println("White King to Black Pawn");
		tr = new Trajectory(Kw);
		try {
			tr.findFirstTrajectory(Pb.location, reti, obsNone, 6); 
			List zones = Zone.getZones(tr, reti, obsNone, 6);
			System.out.println("FOUND 1: " + zones.size() + " zones in search.");
			Iterator ziter = zones.iterator();
			while (ziter.hasNext()) {
				Zone z = (Zone) ziter.next();
				z.paint(g, off9, reti);  
				g.setColor(java.awt.Color.black);
				jtext.setText(z.toString());
				z.print();
				System.out.println("");
			}
			tr.paint(g, off9, java.awt.Color.yellow);
		}
		catch (Exception x) {
												//System.out.println("in ZoneDemo.drawReti2 " + x );
												//x.printStackTrace();
		}	
		
		System.out.println("Black Pawn to promotion");
	    tr = new Trajectory(Pb);
		try {
			tr.findFirstTrajectory(Tw.location, reti, obsNone, 6); 
			List zones = Zone.getZones(tr, reti, obsNone, 6);
			System.out.println("FOUND 1: " + zones.size() + " zones in search.");
			Iterator ziter = zones.iterator();
			while (ziter.hasNext()) {
				Zone z = (Zone) ziter.next();
				z.paint(g, off9, reti);  
				g.setColor(java.awt.Color.black);
				jtext.setText(z.toString());
				z.print();
				System.out.println("");
			}
			tr.paint(g, off9, java.awt.Color.yellow);
		}
		catch (Exception x) {
							//System.out.println("in ZoneDemo.drawReti2 " + x );
							//x.printStackTrace();
		}	
		try {
			System.out.println("White Pawn to promotion.");
	 		tr = new Trajectory(Pw);
		    tr.findFirstTrajectory(Tb.location, reti, obsNone, 6); 
		    List zones = Zone.getZones(tr, reti, obsNone, 6);
		    System.out.println("FOUND 2: " + zones.size() + " zones in search.");
		    Iterator ziter = zones.iterator();
		    while (ziter.hasNext()) {
			   Zone z = (Zone) ziter.next();
			   z.paint(g, off9, reti);  
			   g.setColor(java.awt.Color.black);
			   jtext.setText(z.toString());
			   z.print();
			   System.out.println("");
		    }
			tr.paint(g, off9, java.awt.Color.yellow);
		    
		}
		catch (Exception x) {
					System.out.println("in ZoneDemo.drawReti2 " + x );
					x.printStackTrace();
		}
	}
	public void drawPawnTest(Graphics g) {
		System.out.println("Draw Pawn Test");
				//		"real" pieces
				 Piece Kb = new King(   new Point(7, 4), false);
				 Piece Pw = new Pawn(   new Point(5, 2), true);
				 Piece Bw = new Bishop( new Point(5, 3), true);
		
			Piece reti[][] = {
					{ z, z, z, z, z, z, z, z},
					{ z, z, z, z, z, z, z, z},
					{ z, z, z, z, z, z, z, z},
					{ z, z, z, z, z, z, z, z},
					{ z, z, z, z, z, z, z, z},
					{ z, z,Pw,Bw, z, z, z, z},
					{ z, z, z, z, z, z, z, z},
					{ z, z, z, z,Kb, z, z, z}
				};
				validateBoard(reti);
				Trajectory tr = new Trajectory(Kb);
				try {
					tr.findFirstTrajectory(Bw.location, reti, obsNone, 5); 
					ReachableBoard.paintGrid(g, off9);
					Point p = new Point();
					for (p.col=0; p.col<8; p.col++) {
						for (p.row=0; p.row<8; p.row++) {
							if (reti[p.row][p.col]!=null) {
								reti[p.row][p.col].paint(g, off9);				
							}
						}
					}
					tr.paint(g, off9, java.awt.Color.yellow);
					List zones = Zone.getZones(tr, reti, obsNone, 5);
					System.out.println("FOUND: " + zones.size() + " zones in search.");
					Iterator ziter = zones.iterator();
					while (ziter.hasNext()) {
						Zone z = (Zone) ziter.next();
						z.paint(g,off9, reti);  
						g.setColor(java.awt.Color.black);
						jtext.setText(z.toString());
					}
					tr.paint(g, off9, java.awt.Color.yellow);
//					Zone testZone = new Zone(tr, reti, obsNone, 5);			
//					testZone.paint(g, off9, reti);
//					//testZone.print();
//					g.setColor(java.awt.Color.black);
//					jtext.setText(testZone.toString());
				}
				catch (Exception x) {
					System.out.println("in ZoneDemo.drawReti" + x);
					x.printStackTrace();
				}
		}
	public void drawDummyTest(Graphics g) {
		System.out.println("Draw Dummy Test");
					//		"real" pieces
					 Piece Pw = new Pawn(   new Point(5, 2), true);
					 Piece Tb = new TargetZone( new Point(7,2), false);
		
				Piece reti[][] = {
						{ z, z, z, z, z, z, z, z},
						{ z, z, z, z, z, z, z, z},
						{ z, z, z, z, z, z, z, z},
						{ z, z, z, z, z, z, z, z},
						{ z, z, z, z, z, z, z, z},
						{ z, z,Pw, z, z, z, z, z},
						{ z, z, z, z, z, z, z, z},
						{ z, z,Tb, z, z, z, z, z}
					};
					validateBoard(reti);
					Trajectory tr = new Trajectory(Pw);
					try {
						tr.findFirstTrajectory(Tb.location, reti, obsNone, 5); 
						ReachableBoard.paintGrid(g, off9);
						Point p = new Point();
						for (p.col=0; p.col<8; p.col++) {
							for (p.row=0; p.row<8; p.row++) {
								if (reti[p.row][p.col]!=null) {
									reti[p.row][p.col].paint(g, off9);				
								}
							}
						}
						tr.paint(g, off9, java.awt.Color.yellow);
						tr.print();
					}
					catch (Exception x) {
						System.out.println("in ZoneDemo.drawDummyTest" + x);
						x.printStackTrace();
					}
			}
		
	public void drawZone2(Graphics g) {
				//		"real" pieces
				 Piece Kb = new King(  new Point(6, 1), false);
				 Piece Pb = new Pawn(  new Point(4, 4), false);
				 Piece Bw = new Bishop(new Point(1, 5), true);
				 Piece Kw = new King(  new Point(0, 1), true);
				 Piece Kw2 = new King( new Point(4, 5), true);
				 Piece Nb = new Knight(new Point(6, 6), false);
				 Piece Pw = new Pawn(  new Point(4, 3), true);
				 Piece Pw2 = new Pawn( new Point(2, 2), true);
		
				 Piece board[][] = {
					 {z,Kw, z, z, z, z, z, z},
					 {z, z, z, z, z,Bw, z, z},
					 {z, z,Pw2,z, z, z, z, z},
					 {z, z, z, z, z, z, z, z},
					 {z, z, z,Pw,Pb, z, z, z},
					 {z, z, z, z, z, z, z, z},
					 {z,Kb, z, z, z, z,Nb, z},
					 {z, z, z, z, z, z, z, z}
				 };
			Trajectory tr = new Trajectory(Bw);
				try {
					tr.findFirstTrajectory(Pb.location, board, obsNone, 5); 
					tr.paint(g, off9, java.awt.Color.yellow);
					List zones = Zone.getZones(tr, board, obsNone, 5);
					System.out.println("FOUND: " + zones.size() + " zones in search.");
					Iterator ziter = zones.iterator();
					while (ziter.hasNext()) {
						Zone z = (Zone) ziter.next();
						z.paint(g, off9, board);  
						z.print();
						System.out.println("");
					}
					tr.paint(g, off9, java.awt.Color.yellow);
					g.setColor(java.awt.Color.black);
					jtext.setText(z.toString());					
				}
				catch (Exception x) {
					System.out.println("int ZoneDemo.drawZone() caught: " + x);
					x.printStackTrace();
				}
		}
}
