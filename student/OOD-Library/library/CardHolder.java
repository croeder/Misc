/*
 * Created on Apr 6, 2004
 *
 */
package library;

/**
 * Represents any library card holder. Librarians and customers both
 * use this class. They are distinguished by use of the librarianPrivileges
 * function.
 * 
 * @author Chris Roeder
 */
public class CardHolder {

	String fname;
	String lname;
	String cardNumber;
	String branchName;
	
	public CardHolder(String fname, String lname, String cardNumber) {
		this.fname = fname;
		this.lname = lname;
		this.cardNumber = cardNumber;
	}
	
	public void setBranch(String b) {
		branchName = b;
	}
	
	public String getBranch() {
		return branchName;
	}
	
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		CardHolder ch = (CardHolder) o;
		return ch.cardNumber == cardNumber;
	}
	
	public String toString() {
		return fname + " " + lname + " card: " + cardNumber;
	}
	
	/**
	 * tells if the cardholder is a librarian or not. Librarian cards start 
	 * with an 'L'. Regular customers' cards start with a 'C'.
	 * @return
	 */
	public boolean librarianPrivileges() {
		return cardNumber.charAt(0) == 'L';
	}
}
