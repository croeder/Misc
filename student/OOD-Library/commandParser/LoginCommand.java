/*
 * Created on Apr 6, 2004
 *
 */
package commandParser;


import java.util.HashMap;
import library.CardHolder;

/**
 * @author User
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class LoginCommand extends Command {

    HashMap users=null;
    String branch=null;
	String cardNumber=null;
	
	public LoginCommand(String n, String s, HashMap users){
		super(n,s);
		this.users = users;
		int errors=0;
		String prefix = "Login Command: Error: ";
		
		if ((cardNumber = (String) paramHash.get("card")) == null) {
			System.out.println(prefix + "command needs a card number."); 
			errors++;
		}
		if ((branch = (String) paramHash.get("branch")) == null) {
			System.out.println(prefix + "login command requires branch parameter.");
			errors++;
		}
		if (errors > 0) {
			usage();
		}
		
		
	}
	
	public void go(){
		if (cardNumber != null) {
			CardHolder user = (CardHolder) users.get(cardNumber);
			if (user != null){
				user.setBranch(branch);
				CommandProcessor.getProcessor().setUser(user);
				System.out.println(user.toString() + " is good to go.");
			}
			else {
				System.out.println("Login Command: Error: card number not found.\n" +
				     "  Check your card number. Has your card expired?\n" +
				     "  Do you have too many overdue books?"); 
				usage();
			}
		}
	}
	
	public String usage() {
		return "login card=<card number> branch=<branch>";
	}
}
