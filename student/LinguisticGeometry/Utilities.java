/*
 * Created on Feb 19, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

/**
 * @author User
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

import java.text.DecimalFormat;
public class Utilities {

	/**
	 * Sleep for specified number of milliseconds
	 * This is just a Thread.sleep() call with the try/catch block
	 * around it.
	 */
	public static void snooze(int i){
		try { Thread.sleep(i);}
		catch (Exception x) {
			System.out.println(x);
		}
	}

	/** 
	 * Format a string for a field width.
	 * @param in input String
	 * @param width desired width
	 * @param direction one of ('l','r','c') indicating left, right, center
	 */
	static String makeWide(String in, int width, char direction) {
		String out = "";
		String blanks = "                                           ";
		if (width > in.length()) {
			// pad out on left, right or center to make-up
			switch (direction) {
				case 'l' :
					out = in + blanks.substring(0, width - in.length());
					break;
				case 'r' :
					out = blanks.substring(0, width - in.length()) + in;
					break;
				case 'c' :
					String pad = blanks.substring(0, (width - in.length())/2);

					out = pad + in + pad;
					int leftover = (width - in.length())%2;
					if (leftover > 0)
						out = out + " ";
					break;
			}
		} else {
			// cut down to fit
			int tooMuch = in.length() - width;
			switch (direction) {

				case 'l' :
					out = in.substring(tooMuch, in.length());
					break;
				case 'r' :
					out = in.substring(0, width);
					break;
				case 'c' :
					out = in.substring(tooMuch/2 +1, width);
			}
		}

		return out;
	}
	static void testMakeWide() {
		System.out.println("\"" + makeWide("xxx", 5, 'l') + "\"");
		System.out.println("\"" + makeWide("xxx", 5, 'c') + "\"");
		System.out.println("\"" + makeWide("xxx", 5, 'r') + "\"");
		System.out.println("\"" + makeWide("xyz", 2, 'l') + "\"");
		System.out.println("\"" + makeWide("xyz", 2, 'c') + "\"");
		System.out.println("\"" + makeWide("xyz", 2, 'r') + "\"");
	}
	static void testFormat() {
		DecimalFormat dc = new DecimalFormat("0");
		System.out.println("\"" + dc.format(2) + "\"");
	}
	public static void main(String args[]) {
		testMakeWide();
		testFormat();
	}
}
