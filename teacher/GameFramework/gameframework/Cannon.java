package gameframework;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author Chris Roeder
 * @version 1.0
 */
import java.awt.Color;
import java.awt.Graphics;


/**
 * Cannon
 * a stationary cannon with adjustable angle. It shoots a Bullet
 * at the same angle as it faces.
 * @author Chris Roeder
 * @version 1.0
 */
public class Cannon  implements Thing{
  // limited to one bullet at a time
  // bullet still exists and eats CPU after it hits a wall
  protected int x;
  int y;
  int length;
  Color color;
  protected double angle;
  int velocity=5;
  int height=20;
  int width=20;

  public Cannon(int x, int y, int l,  Color c) {
    this.x = x;
    this.y = y;
    length = l;
    color = c;
  }

  public void setAngle(double a) {
    angle = a;
  }

  /**
   * incAngle
   * increase the angle of the cannon (move it more to the right)
   */
  public void incAngle() {
    if (angle < 3.0){
      angle += 0.3;
    }
  }

  /**
   * decAngle
   * decrement the Angle (move it more to the left)
   */
  public void decAngle() {
    if (angle > 0.3) {
      angle -= 0.3;
    }
  }

  public void draw(Graphics g) {
    Color temp = g.getColor();
    g.setColor(color);
    int x2 = (int) (length * Math.cos(angle));
    int y2 = (int) (length * Math.sin(angle));
    System.out.println("" + length + " " + x2 + " " + y2);
    g.drawLine(x,y,x2+x,y-y2);
    g.fillRect(x-width/2,y,height,width);
    g.setColor(temp);
  }

  /**
   * shoot
   * Creates a bullet travelling along the cannon. The bullet starts
   * at the base of the cannon and travels at the same angle.
   * @return
   */
  public Bullet shoot() {
    int x2 = (int) (velocity * Math.cos(angle));
    int y2 = (int) (velocity * Math.sin(angle));
    System.out.println("velocity " + x2 + " " +y2);
    return new Bullet(x,y,x2,-y2,4,4, Color.red);
  }
  
  // implementation of Thing interface
   public int getX() { return x; }
   public int getY() { return y; }
   public int getHeight() { return height; }
   public int getWidth() {return width; }
   public void die() {
	  // don't dissappear, just sleep for a bit
	  try { Thread.sleep(200); }
	  catch (Exception x) { System.err.println(x); }
	  x = 20; 
	  
   }
}