package gameframework;

import javax.swing.JApplet;
import java.awt.HeadlessException;
import java.awt.Graphics;


public class One extends JApplet {
  public One() throws HeadlessException {
  }
  int x=0;
  int y=0;
  int vx=1;
  int vy=1;
  public void paint(Graphics g) {
    super.paint(g);
    g.fillOval(x, y, 50,50);
    x+=vx;
    if (x > this.getWidth()-50 || x < 0)
      vx = vx * -1;
    y+=vy;
    if (y > this.getHeight()-50 || y < 0)
      vy = vy * -1;

      try { Thread.sleep(20); }
      catch(Exception x) { System.err.println(x);}
    repaint();
  }

}