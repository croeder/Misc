/*
 * Created on Apr 8, 2004
 *
 */
package library;

/**
 * Used when there's a problem checking-in.
 * Typically this is when there is a hold on the book from another user,
 * or the book is not available.
 * 
 * @author Chris Roeder
 */
public class CheckInException extends LibraryException {

	public CheckInException(String s) {
		super(s);
	}
}
