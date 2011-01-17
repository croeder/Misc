/*
 * Created on Apr 6, 2004
 *
 */
package library;

/**
 * This class tracks books as a more specific type of LibraryItem.
 * The system tracks pages in addition to title and author. 
 *  
 * @author Chris Roeder
 * 
 */
public class Book extends LibraryItem {

	private int numPages;
	/**
	 * @param title
	 * @param author
	 */
	public Book(String title, String author, String branchName,  int n) {
		super(title, author, branchName);
		numPages = n;
	}
	
	public String toString() {
		return super.toString() + " with " + numPages + " pages.";
	}
}
