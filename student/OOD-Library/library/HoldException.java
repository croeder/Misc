/*
 * Created on Apr 8, 2004
 *
 */
package library;

/**
 * Used when there's a problem creating a hold on a book.
 * 
 * @author Chris Roeder
 */
public class HoldException extends LibraryException {
	private Hold hold;
	HoldException(String s, Hold h) {
		super(s);
		hold = h;
	}
}
