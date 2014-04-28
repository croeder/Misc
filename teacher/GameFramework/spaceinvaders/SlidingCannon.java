/*
 * Created on Mar 4, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package spaceinvaders;

import gameframework.Cannon;

import java.awt.Color;

/**
 * @author User
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SlidingCannon extends Cannon {

	/**
	 * @param x
	 * @param y
	 * @param l
	 * @param c
	 */

	public SlidingCannon(int x, int y, int l,  Color c) {
	   super(x,y,l,c);
	   angle = 3.1415926/2.0;
	}
	public void incAngle() {
	   if (x < 300){
		 x -= 5;
	   }
	 }

	 /**
	  * decAngle
	  * decrement the Angle (move it more to the left)
	  */
	 public void decAngle() {
	   if (x> 10) {
		 x += 5;
	   }
	 }

}
