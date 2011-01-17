/*
 * Created on Mar 2, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package pong;

import gameframework.Wall;

/**
 * @author User
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Paddle extends Wall {
	private int minX;
	private int maxX;
	private final int MOVE_INCREMENT=10;
	public Paddle(int x, int y, int width, int height, String name,
	             int minX, int maxX) {
		super(x, y, width, height, name);
		this.minX = minX;
		this.maxX = maxX;
	}
	public void moveLeft(){
		if (x > minX)
			x-=MOVE_INCREMENT;
	}
	public void moveRight() {
		if (x < maxX)
			x+=MOVE_INCREMENT;
	}
}
