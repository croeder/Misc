/*
 * Created on Mar 4, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package spaceinvaders;


import gameframework.Bullet;
import gameframework.Wall;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.*;

import javax.swing.JFrame;

/**
 * @author User
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

import gameframework.Game;

public class SpaceInvaders extends JFrame implements Game {


	public static void main(String args[]) {
		SpaceInvaders t = new SpaceInvaders();
		t.setBounds(0, 0, 320, 270);
		t.setVisible(true);
	}
	int numLives = 3;
	int score = 0;
	boolean gameState=true;
	
	public void playerDied() {
		numLives--;
		if (numLives < 0) {
			gameState = false;
		} else {
			try {
				Thread.sleep(200);
			} catch (Exception x) {
				System.err.println(x);
			}
			
		}
	}
	public void score() {
		score++;
	}
	GroupBall b[] = new GroupBall[100];
	int numBalls = 0;
	SlidingCannon gun = new SlidingCannon(100, 230, 10, Color.blue);
	Wall walls[] = new Wall[5];
	Bullet theLonelyBullet;

	public SpaceInvaders() {
		walls[0] = new Wall(10, 40, 10, 200, "top"); // top wall
		walls[1] = new Wall(10, 40, 300, 10, "left"); // left wall
		walls[2] = new Wall(10, 240, 300, 10, "bottom"); // bottom wall
		walls[3] = new Wall(300, 40, 10, 200, "right"); // right wall

		walls[4] = new Wall(120, 180, 40, 40, "blob"); // blob in middle

		// there's alittle potential here for trouble. Each
		// ball has to be created with the same velocity
		// and y velocity of 0.
		b[numBalls++] =
			new GroupBall(35, 55, 2, 0, 20, 20, Color.cyan, this, gun);
		b[numBalls++] =
			new GroupBall(60, 55, 2, 0, 20, 20, Color.cyan, this, gun);
		b[numBalls++] =
			new GroupBall(85, 55, 2, 0, 20, 20, Color.cyan, this, gun);
		b[numBalls++] =
			new GroupBall(110, 55, 2, 0, 20, 20, Color.cyan, this, gun);
		b[numBalls++] =
			new GroupBall(135, 55, 2, 0, 20, 20, Color.cyan, this, gun);
		b[numBalls++] =
			new GroupBall(160, 55, 2, 0, 20, 20, Color.cyan, this, gun);
		/* Q: Why does it break when I add a second row?
		b[numBalls++] = new GroupBall(35,  60, 3, 0, 20,20, Color.cyan, this);
		b[numBalls++] = new GroupBall(60, 60, 3, 0, 20,20, Color.cyan, this);
		b[numBalls++] = new GroupBall(85, 60, 3, 0, 20,20, Color.cyan, this);
		b[numBalls++] = new GroupBall(110, 60, 3, 0, 20,20, Color.cyan, this);
		b[numBalls++] = new GroupBall(135, 60, 3, 0, 20,20, Color.cyan, this);
		b[numBalls++] = new GroupBall(160, 60, 3, 0, 20,20, Color.cyan, this);
		// A: because then you have two balls being the "first" to report a
		// bounce. The first one changes the direcgtion. The second one changes
		// it back.
		 */
		//b[numBalls++] = new Bullet(35,35,2,1, 4,4, Color.red); numBalls++;
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void bounceBallGroup(int direction) {
		for (int i = 0; i < numBalls; i++) {
			b[i].bounce(direction);
		}
	}
	/**
	 * paint
	 * The JApplet's paint method. Lots of stuff in here. Read the source.
	 * @param g
	 */
	public void paint(Graphics g) {
		super.paint(g);

		// draw the gun
		gun.draw(g);

		// draw the bullet if there is one
		if (theLonelyBullet != null) {
			theLonelyBullet.draw(g);
		}

		// draw each ball
		// and check to see if it hit any of the walls.
		for (int j = 0; j < numBalls; j++) {
			b[j].draw(g);
		}
		for (int j = 0; j < numBalls; j++) {
			for (int i = 0; i < 5; i++) {
				walls[i].bounce(b[j]);
			}
		}

		// Check to see if the bullet hit any of the walls
		for (int i = 0; i < 5; i++) {
			if (theLonelyBullet != null) {
				walls[i].bounce(theLonelyBullet);
			}
		}
		// Check to see if the bullet hit any of the balls
		for (int i = 0; i < numBalls; i++) {
			if (theLonelyBullet != null) {
				theLonelyBullet.collide(b[i]);
			}
		}

		// draw the walls
		for (int i = 0; i < 5; i++) {
			walls[i].draw(g);
		}

		// slow down the animation for a bit
		try {
			Thread.sleep(20);
		} catch (Exception x) {
			System.err.println(x);
		}
		repaint();
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
		if (e.getKeyChar() == ' ') {
			// fire
			theLonelyBullet = gun.shoot();
		} else if (e.getKeyChar() == 'k') {
			// angle left
			gun.incAngle();
		} else if (e.getKeyChar() == 'l') {
			// angle right
			gun.decAngle();
		}
	}

}

class Two_this_keyAdapter extends java.awt.event.KeyAdapter {
	SpaceInvaders adaptee;

	Two_this_keyAdapter(SpaceInvaders adaptee) {
		this.adaptee = adaptee;
	}
	public void keyPressed(KeyEvent e) {
		adaptee.this_keyPressed(e);
	}
}
