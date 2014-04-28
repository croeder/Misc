package gameframework;

/**
 * <p>Title: Wall</p>
 * <p>Description: an all-purpose wall that can bounce Balls. </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author Chris Roeder
 * @see Ball
 * @version 1.0
 * 
 * changed x, y, w and h to protected
 * added color, setColor()
 */
import java.awt.Graphics;
import java.awt.Color;

public class Wall {
  protected int x; 
  protected int y;
  protected int w;
  protected int h;
  String name;
  Color color;
  public void setColor(Color c){
  	color =c ;
  }
  public Wall(int x, int y, int w, int h, String name) {
    this.x = x;
    this.y = y;
    this.w = w;
    this.h = h;
    this.name = name;
    color = Color.black;
  }

  public void draw(Graphics g) {
  	g.setColor(color);
    g.fillRect(x, y, w, h);
  }
  public void bounce(Ball b) {
    if ((b.y + b.height/2) >= y && (b.y + b.height/2) <= y + h) {
      if ((b.x + b.width/2) < (x + w) && Math.abs((b.x + b.width/2) - (x + w)) <= Math.abs(b.vx)) {
        b.reactToWall(b.VERTICAL);// b.vx *= -1;
      }
      if ((b.x + b.width/2) > x && Math.abs((b.x + b.width/2) - x) <= Math.abs(b.vx)) {
        b.reactToWall(b.VERTICAL);//b.vx *= -1;
      }
    }
    if ((b.x + b.width/2) >= x && (b.x + b.width/2) <= x + w) {
      if ((b.y + b.height/2) < (y + h) && Math.abs((b.y + b.height/2) - (y + h)) <= Math.abs(b.vy)) {
        b.reactToWall(b.HORIZONTAL); //b.vy *= -1;
      }
      if ((b.y + b.height/2) > y && Math.abs((b.y + b.height/2) - y) <= Math.abs(b.vy)) {
        b.reactToWall(b.HORIZONTAL); //b.vy *= -1;
      }
    }
  }

}