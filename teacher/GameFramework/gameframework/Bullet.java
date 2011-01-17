package gameframework;
import java.awt.Color;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author Chris Roeder
 * @version 1.0
 */

/**
 * Bullet
 * a derivation of Ball that dies when it hits a wall instead of bouncing.
 * This is accomplished by over-riding the reactToWall function.
 * @author Chris Roeder
 * @version 1.0
 */
public class Bullet extends Ball {
  // Bugs?
  // collision restricted to balls, should be an interface
  // so that other things (asteroids or ships) can collide
  //(and blow up) too.
  public Bullet(int x, int y, int vx, int vy) {
    this(x,y,vx,vy,5,5,Color.black);
  }
  public Bullet(int x, int y, int vx, int vy, int w, int h, Color c) {
    super(x,y,vx,vy,w,h,c);
  }

  /**
   * collide
   * Checks to see if the bullet is colliding with the Thing.
   * @see Thing
   * @param t The thing the bullet might be hitting.
   */
  public boolean collide(Thing t) {
    if (Math.abs(x - t.getX()) <= t.getWidth()/2 &&
        Math.abs(y - t.getY()) <= t.getHeight()/2) {
      t.die();
      return true;
    }
    return false;
  }

  /**
   * reactToWall
   * die when it hits a wall
   * @see Wall
   * @see Ball
   * @param wallOrientation HORIZONTAL or VERTICAL constants
   */
  public void reactToWall(int wallOrientation) {
    // stop the bullet
    vy = 0; vx = 0;
    // move it off screen
    x = -width; y = -height;
  }

}