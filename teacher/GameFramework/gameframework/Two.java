package gameframework;

/**
 * <p>Title: Two </p>
 * <p>Description: Second test driver for Animation</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author Chris Roeder
 * @version 1.0
 */

import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.*;


public class Two extends javax.swing.JFrame {
  // Bugs
  // bullets are still in the list when they stop
  
  public static void main(String args[]) {
  	Two t = new Two();
  	t.setBounds(0,0, 300,300);
  	t.setVisible(true);
  }
  
  Ball b[] = new Ball[100];
  int numBalls=0;
  Cannon gun = new Cannon(100, 175, 10, Color.blue);
  Bullet theLonelyBullet=null;
  Wall walls[] = new Wall[5];
  public Two() {
    walls[0] = new Wall(10, 10, 10, 200, "top"); // top wall
    walls[1] = new Wall(10, 10, 300, 10, "left"); // left wall
    walls[2] = new Wall(10, 200, 300, 10, "bottom" );// bottom wall
    walls[3] = new Wall(300, 10, 10, 200, "right");// right wall

    walls[4] = new Wall(120, 80, 40,40, "blob"); // blob in middle

    b[0] = new Ball(35, 35, 1, 1, 20,20, Color.cyan); numBalls++;
    b[1] = new Bullet(35,35,2,1, 4,4, Color.red); numBalls++;
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * paint
   * The JApplet's paint method. Lots of stuff in here. Read the source.
   * @param g
   */
 public void paint(Graphics g) {
   super.paint(g);

   // draw the gun
   gun.draw(g);

   // draw the bullet if there is one
   if (theLonelyBullet != null) {
     theLonelyBullet.draw(g);
   }

   // draw each ball
   // and check to see if it hit any of the walls.
   for (int j=0; j<numBalls; j++) {
     b[j].draw(g);
     for (int i = 0; i < 5; i++) {
       walls[i].bounce(b[j]);
     }
   }

   // Check to see if the bullet hit any of the walls
   for (int i = 0; i < 5; i++) {
     if (theLonelyBullet != null) {
       walls[i].bounce(theLonelyBullet);
     }
   }
   // Check to see if the bullet hit any of the balls
   for (int i = 0; i < numBalls; i++) {
     if (theLonelyBullet != null) {
       theLonelyBullet.collide(b[i]);
     }
   }

   // draw the walls
   for (int i = 0; i < 5; i++) {
       walls[i].draw(g);
   }

  // slow down the animation for a bit
  try { Thread.sleep(20); }
   catch(Exception x) { System.err.println(x);}
   repaint();
 }


  private void jbInit() throws Exception {
    this.addKeyListener(new Two_this_keyAdapter(this));
  }

  /**
   * this_keyPressed()
   * for cannon control.
   * @param e
   */
  void this_keyPressed(KeyEvent e) {
    if (e.getKeyChar() == ' ') {
      // fire
      theLonelyBullet = gun.shoot();
    }
    else if (e.getKeyChar() == 'k') {
      // angle left
      gun.incAngle();
    }
    else if (e.getKeyChar() == 'l'){
      // angle right
      gun.decAngle();
    }
  }

}

class Two_this_keyAdapter extends java.awt.event.KeyAdapter {
  Two adaptee;

  Two_this_keyAdapter(Two adaptee) {
    this.adaptee = adaptee;
  }
  public void keyPressed(KeyEvent e) {
    adaptee.this_keyPressed(e);
  }
}
