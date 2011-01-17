/*
 * Created on Apr 6, 2004
 *
 */
package library;

/**
 * represents a "hold" on a book. Includes references to the LibraryItem,
 * the cardholder, the branch and a notification style.
 * 
 * @author Chris Roeder
 *
 */
public class Hold {

	private LibraryItem item;
	private CardHolder user;
	private String branch; // branc to transfer to
	private String notify; // "phone" or "email"
	
	public Hold(LibraryItem item, CardHolder user, String branch, String notify){
		this.item = item;
		this.user = user;
		this.branch = branch;
		this.notify = notify;
	}
	public String toString(){
		return item + 
		  "\n  held by:     " + user +
		  "\n  transfer to: " + branch +
		  "\n  notify via:  " + notify;
	}
	public LibraryItem getItem() { return item; }
	public CardHolder getUser() { return user; }
	
}
