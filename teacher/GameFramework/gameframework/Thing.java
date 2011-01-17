package gameframework;

/**
 * Thing
 * A thing that a bullet might hit. You can get position and height
 * and width from it. Furthermore you can tell a Thing to die and dissapper
 * and go away...for after it has been hit.
 * @author Chris Roeder
 * @version 1.0
 */

public interface Thing {
  public int getX();
  public int getY();
  public int getWidth();
  public int getHeight();
  public void die();
}