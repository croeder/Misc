/*
 * Created on Apr 6, 2004
 *
 */
package commandParser;
import library.LibraryItem;
import library.CardHolder;
import library.CheckInException;
/**
 * @author User
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CheckInCommand extends Command {

	private int number;
	private String cardNumber=null;
	private String prefix="CheckInCommand: Error ";
	
	public CheckInCommand(String n, String s) {
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
			System.out.println(prefix + "checkin needs a customer's card number."); 
		}
	}

	public void go() {
		CardHolder user = CommandProcessor.getProcessor().getUser();
		CardHolder customer = CommandProcessor.getProcessor().lookupUser(cardNumber);

		if (user == null){
			System.out.println(prefix + "must login before using checkin command."); 
			return;
		}
		if (customer == null) {
			System.out.println(prefix + "cardnumber must be that of a valid customer.");
			return;
		}
		
		LibraryItem item = (LibraryItem) 
		        CommandProcessor.getProcessor().getResults().get(number);
		try {
			item.checkIn(customer, user);
			System.out.println("Item: " + item.toString() + "\n  checked in.");
			System.out.println("\n  by " + customer + "\n");
		}
		catch (CheckInException x) { 
			System.out.println(prefix + x);
		}
	}

	public boolean librariansOnly() {
		return true;
	}
	
	public String usage() {
		return "lookup number=<item number> card=<card number>";
	}
	
}