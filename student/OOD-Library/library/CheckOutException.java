/*
 * Created on Apr 8, 2004
 *
 */
package library;

/**
 * Used when there's a problem checking code out.
 * 
 * @author Chris Roeder
 */
public class CheckOutException extends LibraryException {

	CheckOutException(){
		super();
	}
	CheckOutException(String s) {
		super(s);
	}
}
