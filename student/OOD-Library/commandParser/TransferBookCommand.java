/*
 * Created on Apr 6, 2004
 *
 */
package commandParser;

import library.LibraryItem;
import library.TransferException;

/**
 * Used by librarians only to transfer a book from one branch to another.
 * No requirement to be logged in at either branch to execute this command. FIX?!
 * 
 * @author Chris Roeder
 */
public class TransferBookCommand extends Command {

	private int itemNumber=0;
	private String fromBranch=null;
	private String toBranch=null;
	
	public TransferBookCommand(String n, String c) {
		super(n,c);
		int errors=0;
		String numberString=null;
		String prefix="TransferBookCommand: Error: ";
		
		if ((numberString = (String) paramHash.get("number")) != null) {
			try {
				itemNumber = Integer.parseInt(numberString);
			}
			catch (NumberFormatException x) {
				System.out.println(prefix + " right side of \"number=\" must be a number." + numberString); 
			}
		}
		else {
			System.out.println(prefix + " needs \"number\" parameter.");
			errors++;
		}
		if ((fromBranch = (String) paramHash.get("from")) == null) {
			System.out.println(prefix + " needs \"from\" parameter.");
			errors++;
		}
		if ((toBranch = (String) paramHash.get("to")) == null) {
			System.out.println(prefix + " needs \"to\" parameter.");
			errors++;
		}
		if (errors > 0) {
			usage();
		}
	}
	
	public void go() {
		LibraryItem item = (LibraryItem) CommandProcessor.getProcessor().getResults().get(itemNumber);
		String prefix="TransferBookCommand: Error: ";
		try { 
			item.transfer(fromBranch, toBranch);
			System.out.println("Item: " + item + "\n  successfully transferred "
			+ "\n  to: " + toBranch);
	 	}
		catch (TransferException x) {
			System.out.println(prefix + x);
		}
	}
	
	public boolean librariansOnly() {
		return true;
	}
	
	public String usage() {
		return "usage: transfer number=<book number> from=<from branch name>\n"
               + "  to=<to branch name>";
	}
}
