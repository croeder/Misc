
package commandParser;

import library.Catalog;
import library.LibraryItem;
import library.CardHolder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;

/**
 * @author Chris Roeder
 * @version 20040405
 *
 */
public class CommandProcessor {

	private static boolean quitFlag=false;
	private static boolean debugFlag=false;
	private Catalog catalog = new Catalog();
	private ArrayList currentResults=null;
	private CardHolder user=null;
	private HashMap users=new HashMap();

	
	private CommandProcessor() {
		
		users.put("C54321", new CardHolder("Chris", "Roeder", "C54321"));
		users.put("C54322", new CardHolder("Al", "Adams", "C54322"));
		users.put("C54323", new CardHolder("Bob", "Brown", "C54333"));
		users.put("L999",   new CardHolder("Marion", "Librarian", "L999"));
	}
	
    private static CommandProcessor singletonProcessor=null;
    public static CommandProcessor getProcessor() { return singletonProcessor; }
    
	public static void main(String[] args) {
		CommandProcessor cp = new CommandProcessor();
		singletonProcessor = cp;
		//cp.commandLoop();
		cp.testLoop();
	}
	
	public void commandLoop() throws IOException  {
		java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));
	
		String input=null;
		while (!quitFlag) {
			System.out.println("");
			System.out.print("library>");
			input = reader.readLine(); 
			Command cmd = Command.commandFactory(input, this, catalog, users, user);
			if (cmd != null) {
				if (cmd.onlyLibrarians()) {
					if (user.librarianPrivileges()) {
						cmd.go();
					}
					else {
						System.out.println("Error: User must be a librarian to execute"
						+ cmd + " command. Please see a librarian at any branch for help."); 
					}
				}
				else {
					cmd.go();
				}
			}
			else {
				System.out.println("Error: Command \"" + input + "\" not found.\n" +
				                   "Enter \"help\" for a list of commands.");
			}
			if (debugFlag)
				System.out.println(cmd);
		}
	}
	
	public void testLoop() {
		    String testStrings[] = {
		    	
		    	"comment text=test_help",
		    	"help",
				"help foo=bar reeberslobber", // non-negative testing
				
				"comment text=checkout_#1",
		    	"login card=L999 branch=vvillage",
		    	"lookup title=Against",
		    	"checkout number=1 card=C54322",
		    	
				"comment text=checkout_too_high_a_number",
				"login card=L999 branch=ccreek", // error not at that branch
				"lookup title=Wine",
				"checkout number=1 card=C54323",
								
		    	"comment text=checkout_from_wrong_branch",
		    	"checkout number=0 card=C54323",
		    	
		    	"comment text=hold_with_transfer_request_to_lowry",
		    	"login card=C54321 branch=lowry",
		    	"lookup title=Against",
		    	"hold number=1 notify=email transfer=lowry",
		    	
		    	"comment text=try_to_check_out_checked_out_book,_should_fail",
		    	"login card=L999 branch=ccreek",
		    	"lookup title=Against",
		    	"checkout number=1 card=C54321", // should be error
		    	
		    	"comment text=check_in_to_ccreek",
		    	"lookup title=Against",
		    	"checkin number=1 card=C54322",
		    	
		    	
		    	"login card=L999 branch=lowry",
		    	"lookup title=Against",
		    	
		    	"comment text=test_show_holds",
		    	"showholds",
		    	
		    	"comment text=test_check_transfers",
		    	"checktransfers",
		    	
		    	"comment text=transfer_the_book_so_checkout_below_will_work",
		    	"transfer number=1 from=ccreek to=lowry",
		    	
		    	"comment text=checkout_with_bogus_card_number,_should_fail",
		    	"checkout number=1 card=C5xxxx",
		    	
				"comment text=checkout_for_someone_w/out_the_hold,_should_fail",
		    	"checkout number=1 card=C54323", 
		    	
				"comment text=checkout_with_user_that_has_the_hold,_should_succeed_if_transferred",
		    	"checkout number=1 card=C54321", 
		    	
		    	"comment text=these_should_be_clear",
		    	"showholds",
		    	"checktransfers",
		    	"quit"
		    };
		    
			String input=null;
			int i=0;
			while (!quitFlag && i < testStrings.length) {
				System.out.print("library>");
				input = testStrings[i++];
				System.out.println("-->" + input);
				Command cmd = Command.commandFactory(input, this, catalog, users, user);
				if (cmd != null) {
					if (cmd.onlyLibrarians()) {
						if (user.librarianPrivileges()) {
							cmd.go();
						}
						else {
							System.out.println("User must be a librarian to execute this command."); // FIX
						}
					}
					else {
						cmd.go();
					}
					System.out.println("");
				}
				else {
					System.out.println("Command \"" + input + "\" not found.");
				}
				if (debugFlag)
					System.out.println(cmd);
			}
	}
	
	protected void quitLoop(){
		quitFlag = true;
	}
	public void setResults(ArrayList c){
		currentResults = c;
	}
	public ArrayList getResults() {
		return currentResults;
	}
	public void printResults() {
		int i=0;
		Iterator iter = currentResults.iterator();
		while (iter.hasNext()) {
			LibraryItem it = (LibraryItem) iter.next();
			System.out.println("   " + i++ + ". " + it.toString());
		}
	}
	public void setUser(CardHolder c) {
		user = c;
	}
	public CardHolder getUser() {
		return user;
	}

	
	public CardHolder lookupUser(String cardNumber) {
		return (CardHolder) users.get(cardNumber);
	}
}
