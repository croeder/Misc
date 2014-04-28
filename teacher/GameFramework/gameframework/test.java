package gameframework;


import java.awt.Graphics;

public class test extends javax.swing.JApplet {
  Ball b[] = new Ball[5];
  Wall walls[] = new Wall[5];
  public test() {
    b[0] = new Ball(0, 110, 10, 0);
    b[1] = new Ball(0, 130, 10, 0);
    b[2] = new Ball(0, 150, 10, 0);
    b[3] = new Ball(0, 170, 10, 0);
    b[4] = new Ball(250, 110, -10, 0);
    b[5] = new Ball(250, 130, -10, 0);
    b[6] = new Ball(250, 150, -10, 0);
    b[7] = new Ball(250, 170, -10, 0);
    walls[0] = new Wall(120, 80, 40,40, "blob"); // blob in middle
  }

 public void paint(Graphics g) {
   super.paint(g);

   for (int i = 0; i < 8; i++) {
     b[i].draw(g);
     for (int j = 0; j < 5; j++) {
       walls[j].bounce(b[i]);
     }
   }
   for (int i = 0; i < 5; i++) {
       walls[i].draw(g);
   }

  try { Thread.sleep(20); }
   catch(Exception x) { System.err.println(x);}
   repaint();
 }

}
