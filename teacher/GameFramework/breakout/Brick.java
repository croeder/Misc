/*
 * Created on Mar 2, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package breakout;

import gameframework.Game;

import java.awt.Color;
import java.awt.Graphics;
import pong.ScoreLine;
import gameframework.Ball;

/**
 * @author User
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Brick extends ScoreLine {
    final Color colors[] = { Color.red, Color.blue, Color.yellow, Color.magenta,
    	                      Color.cyan };
    final int numColors = 5;
	boolean dead = false;
	int colorNum=0;
	private void nextColor() {
		if (!dead) {
			colorNum++;
			if (colorNum >= numColors) {
				// brick dies
				dead = true;
			}
			else {
				setColor(colors[colorNum]);
			}
		}
	}
	public void draw(Graphics g){
		if (!dead)
		super.draw(g);
	}
	public Brick(int x, int y, int w, int h, String name, Color c, Game p) {
		super(x, y, w, h, name, c, p);
	}
	public void bounce(Ball b) {
		if (!dead) {
			if ((b.getY() + b.getHeight()/2) >= y && (b.getY() + b.getHeight()/2) <= y + h) {
				  if ((b.getX() + b.getWidth()/2) < (x + w) && Math.abs((b.getX() + b.getWidth()/2) - (x + w)) <= Math.abs(b.getVX())) {
					b.reactToWall(b.VERTICAL);
					nextColor();
					theGame.score();
				  }
				  if ((b.getX() + b.getWidth()/2) > x && Math.abs((b.getX() + b.getWidth()/2) - x) <= Math.abs(b.getVX())) {
					b.reactToWall(b.VERTICAL);
					nextColor();
					theGame.score();
				  }
				}
				if ((b.getX() + b.getWidth()/2) >= x && (b.getX() + b.getWidth()/2) <= x + w) {
				  if ((b.getY() + b.getHeight()/2) < (y + h) && Math.abs((b.getY() + b.getHeight()/2) - (y + h)) <= Math.abs(b.getVY())) {
					b.reactToWall(b.HORIZONTAL);
					nextColor();
					theGame.score();
				  }
				  if ((b.getY() + b.getHeight()/2) > y && Math.abs((b.getY() + b.getHeight()/2) - y) <= Math.abs(b.getVY())) {
					b.reactToWall(b.HORIZONTAL);
					nextColor();
					theGame.score();
				  }
				}
			  }
	}
}
