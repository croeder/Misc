/*
 * Created on Mar 2, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package pong;

import gameframework.Wall;
import gameframework.Ball;

/**
 * @author Chris Roeder
 *
 * This is a wall that keeps score. Balls don't bounce when
 * they hit it, they die.
 * 
 * This presents an interesting dilema in the framework as its
 * not super easy to support. I copied the bounce code over here
 * from the base class. Since its so complicated it might be nice
 * to create a way to share that code and not have to copy it here.
 * Its even more difficult because ScoreLine is part of a different
 * package than Ball now...so it has to use accessors instead of the
 * members directly.
 */

import gameframework.Game;

public class DeathLine extends Wall {

	/**
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 * @param name
	 */
	Game theGame;
	public DeathLine(int x, int y, int w, int h, String name, Game p) {
		super(x, y, w, h, name);
		theGame = p;
	}
	public void bounce(Ball b) {
		if ((b.getY() + b.getHeight()/2) >= y && (b.getY() + b.getHeight()/2) <= y + h) {
		  if ((b.getX() + b.getWidth()/2) < (x + w) && Math.abs((b.getX() + b.getWidth()/2) - (x + w)) <= Math.abs(b.getVX())) {
			b.die();// b.vx *= -1;
			theGame.playerDied();
		  }
		  if ((b.getX() + b.getWidth()/2) > x && Math.abs((b.getX() + b.getWidth()/2) - x) <= Math.abs(b.getVX())) {
			b.die();//b.vx *= -1;
			theGame.playerDied();
		  }
		}
		if ((b.getX() + b.getWidth()/2) >= x && (b.getX() + b.getWidth()/2) <= x + w) {
		  if ((b.getY() + b.getHeight()/2) < (y + h) && Math.abs((b.getY() + b.getHeight()/2) - (y + h)) <= Math.abs(b.getVY())) {
			b.die(); //b.vy *= -1;
			theGame.playerDied();
		  }
		  if ((b.getY() + b.getHeight()/2) > y && Math.abs((b.getY() + b.getHeight()/2) - y) <= Math.abs(b.getVY())) {
			b.die(); //b.vy *= -1;
			theGame.playerDied();
		  }
		}
	  }
}
