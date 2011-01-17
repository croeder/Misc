/*
 * Created on Apr 8, 2004
 *
 */
package commandParser;

/**
 * @author User
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class HelpCommand extends Command {
	
	/**
	 * @param name
	 * @param s
	 */
	public HelpCommand(String name, String s) {
		super(name, s);
	}

	public void go() {
		System.out.println("command:    description:");
		System.out.println("checkin     used by a librarian to check out a book");
		System.out.println("checkout    used by a librarion to check in a book");
		System.out.println("help        shows this list");
		System.out.println("hold        used by a customer to place a hold on a book");
		System.out.println("login       used by a customer or libarian to log in");
		System.out.println("lookup      used to find books");
		System.out.println("quit        exits the system");
		System.out.println("showholds   shows books with active holds on them");
		System.out.println("checktransfers  shows holds with checked-in books");
		System.out.println("transfer used to transfer a book from one location to another.");
	}

	/* (non-Javadoc)
	 * @see commandParser.Command#usage()
	 */
	public String usage() {
		return "Usage: help";
	}

}
