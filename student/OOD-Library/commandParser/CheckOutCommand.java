/*
 * Created on Apr 6, 2004
 *
 */
package commandParser;

import java.util.Date;

import library.CardHolder;
import library.CheckOutException;
import library.HoldException;
import library.LibraryItem;

/**
 * @author Chris Roeder
 */
public class CheckOutCommand extends Command {

	private int number=0;
	private String cardNumber=null;
	private String prefix="CheckOutCommand: Error:";
	public String usage() {
		return "checkout number=<book number> card=<card number>";
	}

	public CheckOutCommand(String n, String s) {
		super(n,s);
	
		if (paramHash.get("number") != null) {
			String nString = (String) paramHash.get("number");
			try {
				number = Integer.parseInt(nString);
			}
			catch (Exception x) {
				System.out.println(prefix + "number paramter in checkout command must be an integer."); 
			}
		}
		else {
			System.out.println(prefix + "checkout needs a number parameter."); 
		}
		
		if (paramHash.get("card") != null) {
			cardNumber = (String) paramHash.get("card");
		}
		else {
			System.out.println(prefix + "checkout needs a customer's card number."); 
		}
	}
	
	public void go() {
		LibraryItem item=null;
		try {
			item = (LibraryItem) 
				CommandProcessor.getProcessor().getResults().get(number);
		}
		catch (IndexOutOfBoundsException x) {
			System.out.println(prefix + " number must be between 0 and " +
			    (CommandProcessor.getProcessor().getResults().size() -1));
			return;
		}
		CardHolder librarian = CommandProcessor.getProcessor().getUser();
		CardHolder customer = CommandProcessor.getProcessor().lookupUser(cardNumber);

		if (librarian == null){
			System.out.println(prefix + "librarian must login before using checkout command."); 
			return;
		}
		if (customer == null) {
			System.out.println(prefix + "cardnumber must be that of a valid customer."); 
			return;
		}
		Date dueDate = new Date();
		// POLICY IN UI FIX!!!!
		dueDate.setTime(dueDate.getTime() + 1209600); // 2 weeks
		
		try { 
			item.checkOut(customer, librarian, dueDate); 
			System.out.println("Item: " + item + "\n  successfully checked out"
				+ "\n  by: " +   customer);
		}
		catch (CheckOutException x) {
			System.out.println(prefix + x);	
		} catch (HoldException e) {
			System.out.println(prefix + e);
		}	
	}
	
	public boolean librariansOnly() {
		return true;
	}
}

