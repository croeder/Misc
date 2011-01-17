/*
 * Created on Apr 8, 2004
 *
 */
package library;

/**
 * Base class on Library exceptions.
 * 
 * @author Chris Roeder
 */
public class LibraryException extends Exception {

	public LibraryException(){
		super();
	}
	
	public LibraryException(String s){
		super(s);
	}
}
