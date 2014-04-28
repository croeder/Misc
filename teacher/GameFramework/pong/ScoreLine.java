/*
 * Created on Mar 2, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package pong;

import gameframework.Wall;
import java.awt.Color;
import java.awt.Graphics;
import gameframework.Ball;
import gameframework.Game;
/**
 * @author User
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 * 
 * Added interface Game so that this class can be used elsewhere besides Pong
 */
public class ScoreLine extends Wall {

	/**
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 * @param name
	 */
	protected Game theGame;
	public void draw(Graphics g) {
		super.draw(g);
	}
	public ScoreLine(int x, int y, int w, int h, String name, Color c, Game p) {
		super(x, y, w, h, name);
		super.setColor(c);
		theGame = p;
	}
	public void bounce(Ball b) {
		if ((b.getY() + b.getHeight()/2) >= y && (b.getY() + b.getHeight()/2) <= y + h) {
			  if ((b.getX() + b.getWidth()/2) < (x + w) && Math.abs((b.getX() + b.getWidth()/2) - (x + w)) <= Math.abs(b.getVX())) {
				b.reactToWall(b.VERTICAL);
				theGame.score();
			  }
			  if ((b.getX() + b.getWidth()/2) > x && Math.abs((b.getX() + b.getWidth()/2) - x) <= Math.abs(b.getVX())) {
				b.reactToWall(b.VERTICAL);
				theGame.score();
			  }
			}
			if ((b.getX() + b.getWidth()/2) >= x && (b.getX() + b.getWidth()/2) <= x + w) {
			  if ((b.getY() + b.getHeight()/2) < (y + h) && Math.abs((b.getY() + b.getHeight()/2) - (y + h)) <= Math.abs(b.getVY())) {
				b.reactToWall(b.HORIZONTAL);
				theGame.score();
			  }
			  if ((b.getY() + b.getHeight()/2) > y && Math.abs((b.getY() + b.getHeight()/2) - y) <= Math.abs(b.getVY())) {
				b.reactToWall(b.HORIZONTAL);
				theGame.score();
			  }
			}
		  }

}
