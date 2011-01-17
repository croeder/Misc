/*
 * Created on Mar 4, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package spaceinvaders;

import gameframework.Ball;
import gameframework.Game;

import java.awt.Color;
import java.awt.Graphics;

/**
 * @author User
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
import gameframework.Bullet;

public class GroupBall extends Ball {
	SpaceInvaders si;
	Bullet myBullet;
	SlidingCannon sc;
	/**
	 * @param x
	 * @param y
	 * @param vx
	 * @param vy
	 * @param w
	 * @param h
	 * @param c
	 */
	public GroupBall(
		int x,
		int y,
		int vx,
		int vy,
		int w,
		int h,
		Color c,
		SpaceInvaders si,
		SlidingCannon sc) {
		super(x, y, vx, vy, w, h, c);
		this.si = si;
		this.sc = sc;
	}

	public void reactToWall(int wallOrientation) {
		if (wallOrientation == HORIZONTAL) {
			si.bounceBallGroup(HORIZONTAL);
		}
		if (wallOrientation == VERTICAL) {
			si.bounceBallGroup(VERTICAL);
		}
	}
	public void bounce(int wallOrientation) {
		if (wallOrientation == HORIZONTAL) {
			vy *= -1;
		}
		if (wallOrientation == VERTICAL) {
			vx *= -1;
		}
	}

	public void draw(Graphics g) {
		x += vx;
		y += vy;
		Color old = g.getColor();
		g.setColor(color);
		g.fillOval(x, y, height, width);
		g.setColor(Color.black);
		g.fillOval(x + width / 2, y + height / 2, 1, 1); // DEBUG
		g.setColor(old);
		dropBomb();
		if (myBullet != null) {
			myBullet.draw(g);

			if (myBullet.collide(sc)) {
				myBullet = null;
				((Game) si).playerDied();
			}
		}
	}

	protected void dropBomb() {
		if (((int) (Math.random() * 70)) == 3) {
			myBullet = new Bullet(x, y, 0, 5, 2, 4, Color.black);
		}
	}
}
