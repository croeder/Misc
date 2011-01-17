/*
 * Created on Apr 6, 2004
 *
 */
package commandParser;
import library.CardHolder;
import library.Catalog;
import library.Hold;
import library.LibraryItem;

/**
 * @author User
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class HoldCommand extends Command {

	private CardHolder user;
	private Catalog catalog;
	private int number;
	private String notify;
	private String transfer;
	private String prefix="HoldCommand: Error: ";
	
	public HoldCommand(String n, String s, CardHolder user, Catalog catalog){
		super(n,s);
		this.catalog = catalog;
		this.user = user;
		String numberString;
		int numParams=0;
		if (paramHash.get("number") != null) {
			numberString = (String) paramHash.get("number");
			numParams++;
			try {
				number = Integer.parseInt(numberString);
			}
			catch (NumberFormatException x) {
				System.out.println(prefix + " right side of \"number=\" must be a number." + numberString); 
			}
		}
		if (paramHash.get("notify") != null) {
			notify = (String) paramHash.get("notify");
			numParams++;
		}
		if (paramHash.get("transfer") != null){
			transfer = (String) paramHash.get("transfer");
			numParams++;
		}
		if (numParams < 3) {
			System.out.println(usage());
		}
		 	
	}

	public String usage() {
		return "hold number=<item number> notify=[email|phone] transfer=<branch>";
	}
	
	public void go() {
		LibraryItem item = (LibraryItem) CommandProcessor.getProcessor().getResults().get(number);
		Hold h = new Hold(item, CommandProcessor.getProcessor().getUser(), transfer, notify);
		Catalog.getHoldList().add(h);
		System.out.println("Hold created: " + h);
	}
}
