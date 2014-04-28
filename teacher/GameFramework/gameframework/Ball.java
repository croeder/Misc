package gameframework;

/**
 * <p>Title: Ball</p>
 * <p>Description: a moving object that "reacts" to walls. This version bounces
 * off of perpendicular (square) walls. </p>
 * <p>Copyright: Copyright (c) Chris Roeder 2004</p>
 * <p>Company: </p>
 * @author Chris Roeder
 * @version 1.0
 * 
 * added getVX(), getVY(), and launch()
 * made VERTICAL and HORIZONTAL public
 */
import java.awt.Graphics;
import java.awt.Color;

public class Ball implements Thing {
  // bugs
  // bounces work with balls of even height/width
  protected Color color;
  protected int x=0;
  protected int y=0;
  protected int vx=1;
  protected int vy=1;
  protected final int width;
  protected final int height;
  public final int VERTICAL=1;
  public final int HORIZONTAL=0;

  public int getVX() { return vx; }
  public int getVY() { return vy; }
  public void launch() { x=50; y=120; vx=1; vy=1; }
  
  
  public Ball(int x, int y, int vx, int vy) {
    this.x =x;
    this.y = y;
    this.vx = vx;
    this.vy = vy;
    width=25;
    height=25;
  }
  public Ball(int x, int y, int vx, int vy, int w, int h, Color c) {
    this.x =x;
    this.y = y;
    this.vx = vx;
    this.vy = vy;
    width = w;
    height = h;
    color = c;
  }
  /**
   * draw
   * draw the ball. Call from paint() method of JFrame/JApplet
   * @param g the graphics Context.
   */
  public void draw(Graphics g) {
    x+=vx;
    y+=vy;
    Color old = g.getColor();
    g.setColor(color);
    g.fillOval(x, y, height, width);
    g.setColor(Color.black);
    g.fillOval(x + width/2,y + height/2, 1,1); // DEBUG
    g.setColor(old);
  }

  /**
   * reactToWall
   * for the wall to call to let the Ball and its subclasses
   * polymorphically react to the wall.
   * @see Bullet class.
   * @param wallOrientation
   */
  public void reactToWall(int wallOrientation) {
    if (wallOrientation == HORIZONTAL){
      vy *= -1;
    }
    if (wallOrientation == VERTICAL){
      vx *= -1;
    }
  }

  // implementation of Thing interface
  public int getX() { return x; }
  public int getY() { return y; }
  public int getHeight() { return height; }
  public int getWidth() {return width; }
  public void die() {
    vx=0; vy=0; x=-width; y=-height;
  }
}