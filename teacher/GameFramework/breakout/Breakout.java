/*
 * Created on Mar 2, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package breakout;

import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import gameframework.Wall;
import gameframework.Ball;
import pong.Paddle;
import pong.DeathLine;
import gameframework.Game;

/**
 * @author User
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

	public class Breakout extends JFrame implements Game {

		public static void main(String args[]) {
			Breakout p = new Breakout();
			p.setBounds(0, 0, 350, 350);
			p.setVisible(true);
		}
		// Bugs
		// bullets are still in the list when they stop
	
		Ball b[] = new Ball[100];
		int numBalls = 0;
		Paddle paddle;
		Wall walls[] = new Wall[100];
		int numLives = 3;
		boolean gameState=true;
		int numWalls=0;
		int score = 0;
	
		public void score() {
			score ++;
		}
	
		public Breakout() {
			walls[0] = new Wall(10, 40, 10, 270, "left"); // left wall
			walls[1] = new Wall(10, 40, 300, 10, "top"); // top wall
			walls[2] = new DeathLine(10, 300, 300, 10, "bottom", this); // bottom wall
			walls[3] = new Wall(300, 40, 10, 270, "right"); // right wall
			paddle = new Paddle(120, 270, 60, 10, "blob", 10, 280);
			// blob in middle
			walls[4] = paddle;
			for (int row=0; row<3; row++)
				for (int col=0; col<10; col++)
					walls[5 + col + row * 10] = new 
					 Brick(22 + col * 25, 65 + row * 20, 50, 20, "score", Color.red, this);
			numWalls = 35;

			b[0] = new Ball(55, 70, 1, 1, 20, 20, Color.cyan);
			b[0].launch();
			numBalls++;
			try {
				jbInit();
			} catch (Exception x) {
				;
			}
		}

		public void playerDied() {
			numLives--;
			if (numLives < 0) {
				gameState = false;
			} else {
				try {
					Thread.sleep(20);
				} catch (Exception x) {
					System.err.println(x);
				}
				b[0].launch();
			}
		}
	
		public void paint(Graphics g) {
			super.paint(g);
			if (gameState) {
		
			// draw each ball
			// and check to see if it hit any of the walls.
			for (int j = 0; j < numBalls; j++) {
				b[j].draw(g);
				for (int i = 0; i < numWalls; i++) {
					walls[i].bounce(b[j]);
				}
			}

			// draw the walls
			for (int i = 0; i < numWalls; i++) {
				walls[i].draw(g);
			}

			// paint the number of Lives
			g.drawString("Lives " + numLives, 50, 330);
		
			// paint the score
			g.drawString("Score " + score, 250, 330);

			// slow down the animation for a bit
			try {
				Thread.sleep(5);
			} catch (Exception x) {
				System.err.println(x);
			}
				repaint();
			}
			else {
				g.drawString("Game Over", 100, 100);
			}
		}

		private void jbInit() throws Exception {
			this.addKeyListener(new Two_this_keyAdapter(this));
		}

		/**
		 * this_keyPressed()
		 * for cannon control.
		 * @param e
		 */
		void this_keyPressed(KeyEvent e) {
			if (e.getKeyChar() == 'k') {
				// angle left
				///gun.incAngle();
				paddle.moveLeft();
			} else if (e.getKeyChar() == 'l') {
				// angle right
				//gun.decAngle();
				paddle.moveRight();
			}
		}

	}

	class Two_this_keyAdapter extends java.awt.event.KeyAdapter {
		Breakout adaptee;

		Two_this_keyAdapter(Breakout adaptee) {
			this.adaptee = adaptee;
		}
		public void keyPressed(KeyEvent e) {
			adaptee.this_keyPressed(e);
		}
	}

