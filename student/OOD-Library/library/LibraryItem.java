/*
 * Created on Apr 6, 2004
 *
 */
package library;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

//import commandParser.CommandProcessor;

/**
 * 
 * Represents an object in the library.
 * 
 * 
 * @author Chris Roeder
 *
 * Base class for stuff a library might check-out.
 */
public class LibraryItem {

	protected String title;
	protected String author;
	protected CardHolder checkedOutBy=null;
	protected Date due=null;
	protected String branchName=null;
	
	public LibraryItem(String title, String author, String branchName) {
			this.title = title;
			this.author = author;
			this.branchName = branchName;
	}
	
	public boolean isCheckedOut() {
		return checkedOutBy != null;
	}
	
	public void checkOut(CardHolder customer, CardHolder librarian, Date due) 
	    throws HoldException, CheckOutException {		
		// check for holds
		Collection holds = Catalog.getHoldList();
		Hold h=null;
		Iterator iter = holds.iterator();
		while (iter.hasNext()) {
			 h = (Hold) iter.next();
			 if (this == h.getItem()) {
				 if (h.getUser() != customer){
					throw new HoldException("book has a hold on it for "
                       + "a different customer, checkout denied.", h);
				 }
			 }
		}
		
		// check if checked out
		if (checkedOutBy != null){
			throw new CheckOutException("book already checked out by: " + checkedOutBy); 
		}
		
		// check to see librarian's branch matches book's branch
		if (!librarian.getBranch().equals(branchName)) {
			throw new CheckOutException("book not available at this branch: " + librarian.getBranch()
			   + "\n  it is located at " + branchName);
		}
		
		// clear hold
		if (h!=null && h.getItem().equals(this) && !isCheckedOut()) {
			holds.remove(h);
		}
		
		// check out
		checkedOutBy = customer;
		this.due = due;
	}
	
	public void checkIn(CardHolder customer, CardHolder librarian) throws CheckInException {
		if (checkedOutBy == null) {
			throw new CheckInException(toString() + " is not checked out");
		}
		if (!checkedOutBy.equals(customer)){
			throw new CheckInException("book already checked out by: " + checkedOutBy);
		}
		
		// check for implicit transfer. If the user returns it to a different
		// branch than where it was checked out from.
		branchName = librarian.getBranch();
		
		// check in
		checkedOutBy=null;
		due = null;
	}
	
	public String toString() {
		if (checkedOutBy == null) {
			return "* " + title + "\n        by " + author;	
		}
		else {
			return "  " + title + "\n        by " + author;
		}
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getAuthor() {
		return author;
	}
	
	/**
	 * Transfers a book from the "from" branch to the "to" branch.
	 * Verifies that the book is actually located at the "from" branch.
	 * Doesn't verify the new branch against any list of valid branches (FIX).
	 * 
	 * @param from the name of the branch transferring from
	 * @param to the name of the branch transferring to
	 * @throws TransferException
	 */
	public void transfer(String from, String to) throws TransferException {
		if (!branchName.equals(from)) {
			throw new TransferException("book not available to transfer from " 
			+ from);
		}
		else if (checkedOutBy != null) {
			throw new TransferException("book currently checked out, can't transfer");
		}
		else {
			branchName = to;
		}
	}
}
