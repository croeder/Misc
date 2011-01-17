package backprop;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class Point {
    double x;
    double y;
    public static  final double dimension = 2.506;
    static java.util.Random r = new java.util.Random();

    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    Point() {
            // The area of a circle with radius 1 is 3.14.
            // The sides of a square with area 6.28 is 2.506 (sqrt(6.28)).
            // This produces a 50/50 ratio of inside the circle to outside.
        x = r.nextDouble() * dimension - dimension/2.0;
        y = r.nextDouble() * dimension - dimension/2.0;
    }

    boolean insideCircle() {
       return Math.sqrt(x * x + y * y) < 1.0;
   }
}
