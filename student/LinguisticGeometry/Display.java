import javax.swing.JButton;
import java.awt.*;
import java.awt.event.*;


/**
 * Displays work for project 2.
 * @author User
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Display extends javax.swing.JFrame {
	JButton kingBtn = new JButton();
	JButton queenBtn = new JButton();
	JButton bishopBtn = new JButton();
	JButton knightBtn = new JButton();
	JButton rookBtn = new JButton();
	JButton pawnBtn = new JButton();
	JButton stepBtn = new JButton();
	JButton newBtn = new JButton();
	Point fromPoint = new Point(4, 1);
	//Piece fromPiece = new King(fromPoint);
	Piece fromPiece = new Queen(fromPoint);
	public static void main(String args[]) {
		Display d = new Display();
		d.setSize(800, 600);
		d.setVisible(true);
	}

	public Display() {
		this.getContentPane().setLayout(null);
		int btnWidth = 80;
		int btnHeight = 25;

		kingBtn.setBounds(5, 20, btnWidth, btnHeight);
		kingBtn.setText("King");
		this.getContentPane().add(kingBtn);
		kingBtn.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				fromPiece = new King(fromPoint);
				repaint();
			}
		});

		queenBtn.setBounds(5, 50, btnWidth, btnHeight);
		queenBtn.setText("Queen");
		this.getContentPane().add(queenBtn);
		queenBtn.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				fromPiece = new Queen(fromPoint);
				repaint();
			}
		});
		bishopBtn.setBounds(5, 80, btnWidth, btnHeight);
		this.getContentPane().add(bishopBtn);
		bishopBtn.setText("Bishop");
		bishopBtn.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				fromPiece = new Bishop(fromPoint);
				repaint();
			}
		});
		knightBtn.setBounds(5, 110, btnWidth, btnHeight);
		this.getContentPane().add(knightBtn);
		knightBtn.setText("Knight");
		knightBtn.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				System.out.println("Knight");
				fromPiece = new Knight(fromPoint);
				repaint();
			}
		});
		rookBtn.setBounds(5, 140, btnWidth, btnHeight);
		this.getContentPane().add(rookBtn);
		rookBtn.setText("Rook");
		rookBtn.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				fromPiece = new Rook(fromPoint);
				repaint();
			}
		});
		pawnBtn.setBounds(5, 170, btnWidth, btnHeight);
		this.getContentPane().add(pawnBtn);
		pawnBtn.setText("Pawn");
		pawnBtn.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				fromPiece = new Pawn(fromPoint, true);
				repaint();
			}
		});
		newBtn.setBounds(5, 200, btnWidth, btnHeight);
		this.getContentPane().add(newBtn);
		newBtn.setText("New Piece");
		newBtn.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				fromPiece = new NewPiece(fromPoint);
				repaint();
			}
		});
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
	public void paint(java.awt.Graphics g) {
		super.paint(g);
		g.drawString("Piece: " + fromPiece.getName(), 10, 270);
		g.drawString("st1", 10, 290);
		g.drawString("stk", 10, 310);
		g.drawString("sum", 10, 330);
		g.drawString("intersection", 10, 350);
		g.drawString("start/end", 10, 370);
		g.drawString("Way Point", 10, 390);

		g.setColor(Color.gray);
		g.fillOval(100, 280, 10, 10);
		g.setColor(Color.blue);
		g.fillOval(100, 300, 10, 10);
		g.setColor(Color.cyan);
		g.fillOval(100, 320, 10, 10);
		g.setColor(Color.magenta);
		g.fillOval(100, 340, 10, 10);
		g.setColor(Color.yellow);
		g.fillOval(100, 360, 10, 10);
		g.setColor(Color.red);
		g.fillOval(100, 380, 10, 10);

		Point from = new Point(4, 0);
		fromPiece.move(from);

		// remember, this obstacle array is upside down form how its displayed!
		// positionless pieces used now for display only
		Piece z = null;
		Piece P = new Pawn(true);
		Piece B = new Bishop();
		Piece N = new Knight();
		Piece K = new King();
		Piece R = new Rook();
		Piece Q = new Queen();
		
		// "real" pieces
		Piece Kb = new King(  new Point(6, 1),  false);
		Piece Pb = new Pawn(  new Point(4, 4),  false);
		Piece Bw = new Bishop(new Point(1, 5), true);
		Piece Kw = new King(  new Point(0, 1),  true);
		Piece Kw2 = new King( new Point(4, 5), true);
		Piece Nb = new Knight(new Point(6, 6), false);
		Piece Pw = new Pawn(  new Point(4, 3), true);
		Piece Pw2 = new Pawn( new Point(2, 2), true);
		
		Piece board[][] = {
			{z,Kw, z, z, z, z, z, z,},
			{z, z, z, z, z,Bw, z, z,},
			{z, z,Pw2,z, z, z, z, z,},
			{z, z, z, z, z, z, z, z,},
			{z, z, z,Pw,Pb, z, z, z,},
			{z, z, z, z, z, z, z, z,},
			{z,Kb, z, z, z, z,Nb, z,},
			{z, z, z, z, z, z, z, z,}
		};
		
		validateBoard(board);
		
		Piece obs2[][] = { { z, B, z, z, z, z, z, z }, {
				z, N, z, z, z, z, z, z }, {
				z, K, z, z, z, z, z, z }, {
				z, B, z, z, z, z, z, P }, {
				z, R, z, z, z, z, P, z }, {
				z, Q, z, z, z, P, z, z }, {
				P, P, P, P, P, P, P, P }, {
				z, K, z, z, z, z, z, z }
		};

		Piece obs[][] = { { z, z, z, z, z, z, z, z }, {
				z, z, z, z, z, z, z, z }, {
				z, z, z, z, z, z, z, z }, {
				z, z, z, z, P, z, z, z }, {
				z, z, z, z, P, P, z, z }, {
				z, z, z, z, P, P, z, z }, {
				z, z, z, z, z, z, z, z }, {
				z, z, z, z, z, z, z, z }
		};
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
/*
		// ---fromRB off
		// reachable w/out obstacles
		ReachableBoard rb;
		Point off5 = new Point(20, 500);
		rb = fromPiece.getReachableBoardNoObs();
		;
		g.setColor(Color.black);
		g.drawString("Reachability No Obstacles", off5.col + 30, off5.row + 27);
		rb.paintBoard(g, off5);
		// "to" reachable with obstacles
		ReachableBoard fromRB;
		Point off = new Point(20, 100);
		fromRB = fromPiece.getReachableBoard(obs);
		// ---- from rb off
		fromRB.paintBoard(g, off);
		g.setColor(Color.black);
		g.drawString("Reachability With Obstacles", off.col + 30, off.row + 27);

		// "from" reachable with obstacles
		Point off2 = new Point(20, 300);
		Point to = new Point(4, 7);
		Piece toPiece = null;
		//toPiece = new King(to);		
		try {
			toPiece = (Piece) fromPiece.clone();
		} catch (Exception x) {
			System.err.println("clone excpetion" + x);
			x.printStackTrace();
		}
		toPiece.move(to);
		ReachableBoard toRB = toPiece.getReachableBoard(obs);
		toRB.paintBoard(g, off2);
		g.setColor(Color.black);
		g.drawString(
			"Reachability With Obstacles",
			off2.col + 30,
			off2.row + 27);

		// Sets: ST1, STK, SUM
		Point off3 = new Point(200, 100);
		fromRB.paintSets(g, to, 2, off3);
		g.setColor(Color.black);
		g.drawString("ST1, STk, SUM", off3.col + 30, off3.row + 27);

		// Animated Trajectory Discovery
		Point off4 = new Point(200, 300);
		fromRB.paintGrid(g, off4);
		fromRB.paintObs(g, off4, obs);
		fromRB.paintTrajectoryDiscovery(g, to, off4);
		g.setColor(Color.black);
		g.drawString("Searching for Trajectory", off4.col + 30, off4.row + 27);
		Trajectory t = new Trajectory(fromPiece);
		t.findFirstTrajectory( to, obs);
		t.paintTrajectory(g, off4, Color.cyan) ;

		// new way of doing trajectories (calculating and painting separately)
		Point off6 = new Point(200, 500);
		g.setColor(Color.black);
		g.drawString("Shortest Trajectories", off6.col + 30, off6.row + 27);
		Utilities.snooze(500);
		Bundle bun = new Bundle(fromPiece, to, obs);
		fromRB.paintGrid(g, off6); 
		fromRB.paintObs(g, off6, obs);
		bun.paint(g, off6);
		if (bun.trajSet == null)
		System.out.println("wtf");
		else
		System.out.println("ok");
		bun.print();
		
		// Admissable trajectory
		Point viaPoint = new Point(7,2);
		Point off7 =  new Point(400,100);
		g.setColor(Color.black);
		g.drawString("Admissable Trajectories through c8", off7.col + 30, off7.row + 27);
		Bundle bun2 = Trajectory.findAdmissableTrajectory(fromPiece, to, viaPoint, obs);
		fromRB.paintGrid(g, off7); 
		fromRB.paintObs(g, off7, obs);
		bun2.paint(g, off7);
		bun2.print();
		
		// Admissable trajectories
		Point off8 =  new Point(400,300);
		g.setColor(Color.black);
		Bundle bun3;
		if (fromPiece.getName().equals("Knight") ||
		    fromPiece.getName().equals("NewPiece")) {
		    	// rook and newpiece don't find anything at just a +1
		    	//  they need +2 to make the extra corner in this case
			g.drawString("Admissable Trajectories with length = min + 2", off8.col + 30, off8.row + 27);
			bun3 = new Bundle(fromPiece, to, obs, 2);
		}
		else {
			g.drawString("Admissable Trajectories with length = min + 1", off8.col + 30, off8.row + 27);
			bun3 = new Bundle(fromPiece, to, obs, 1);
		}
		fromRB.paintGrid(g, off8); 
		fromRB.paintObs(g, off8, obs);
		bun3.paint(g, off8);
		//Trajectory.printTrajectories(trajset3);
*/
		// Zones
		Point off9 = new Point(400, 500);
		System.out.println("From: " + Bw.location + "  To: " + Pb.location);
		Trajectory tr = new Trajectory(Bw);
		try {
			tr.findFirstTrajectory(Pb.location, obsNone,  obsNone, 10);
			tr.initTimes();
			tr.paint(g, off9, java.awt.Color.yellow);
			Zone testZone = null;// new Zone(tr, board, obsNone, 5);
			testZone.paint(g, off9, board);
			testZone.print();
		}
		catch (Exception x) {
			System.out.println("in Display.paint() " + x);
		}
		
	}

}