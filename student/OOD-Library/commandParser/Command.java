/*
 * Created on Apr 5, 2004
 *
 */
package commandParser;

import java.util.HashMap;
import library.Catalog;
import library.CardHolder;

/**
 * @author Chris Roeder
 * @version 20040405
 * 
 * This is a base class to commands in the command parser.
 * A command has a name and zero or more named  parameters.
 * <command> <param1>=<value> <param2>=<value>
 * A value can be a quoted string.
 * 
 * Early Restrictions: (FIX)
 * - no prompting in commands
 * - no quoted strings for multiple word titles or authors
 *
 */
public abstract class Command {

	protected String commandString;
	protected String commandName;
	protected String[] paramNames=null;
	
	/**
	 * just a hash of name=value, the value is still a string
	 * parameter objects come from the derived commands...they
	 * know what type the named parameter should be.
	 */
	protected java.util.HashMap paramHash = new HashMap();
	
	/**
	 * tells if a regular user can do this command, or if it must
	 * be a Librarian.
	 * @return
	 */
	public  boolean onlyLibrarians() {
		return false;
	}
	
	/**
	 * shows required parameters for a command
	 * @return
	 */
	public abstract String usage();
	
	/**
	 * This function is the "action" function of the command.
	 */
	public abstract void go();
	
	public String toString(){
		return commandName + paramHash;
	}
	
	/**
	 * Creates a command object based on the command name.
	 * @param s the  command line, must start with the command name.
	 * @return returns a new Command object of the apropriate class (based
	 * on the command name)
	 */
	public static Command commandFactory(String s, CommandProcessor cp, 
		Catalog catalog, HashMap users, CardHolder ch){
		int firstSpace = s.indexOf(' ');
		String commandName = null;
		if (firstSpace == -1){
			commandName = s;
		}
		else {	
			commandName = s.substring(0, firstSpace);
		}
		
		if (commandName.equals("quit")) {
			return new QuitCommand(commandName, s, cp);
		}
		else if (commandName.equals("lookup")) {
			return new LookupCommand(commandName, s, catalog);
		}
		else if (commandName.equals("login")) {
			return new LoginCommand(commandName, s, users);
		}
		else if (commandName.equals("hold")) {
			return new HoldCommand(commandName, s, ch, catalog);
		}
		else if (commandName.equals("showholds")) {
			return new ShowHoldsCommand(commandName, s);
		}
		else if (commandName.equals("checkout")){
			return new CheckOutCommand(commandName, s);
		}
		else if (commandName.equals("checkin")) {
			return new CheckInCommand(commandName, s);
		}
		else if (commandName.equals("help")) {
			return new HelpCommand(commandName, s);
		}
		else if (commandName.equals("transfer")) {
			return new TransferBookCommand(commandName, s);
		}
		else if (commandName.equals("comment")) {
			return new CommentCommand(commandName, s);
		}
		else if (commandName.equals("checktransfers")) {
			return new CheckTransfersCommand(commandName, s);
		}
		else {
			return null;
		}
	}
	
	/**
	 * Base class constructor for all the commands. Just keeps the string
	 * and builds/parses the parameters.
	 * @param s
	 */
	public Command(String name, String s) {
		commandName = name;
		commandString = s;
		// no parse method, just do it here, was protected anyway
		String [] params = s.split(" ");
		for (int i=1; i<params.length; i++)  {
			String[] parts = params[i].split("=");
			if (parts.length == 2){
				paramHash.put(parts[0], parts[1]);
			}
			else if (parts.length==1) {
				paramHash.put(parts[0], "");
			}
			else {
				// FIXC
				System.err.println("command parameter error, too many  parts to parameter");
			}
		}
	}


}
