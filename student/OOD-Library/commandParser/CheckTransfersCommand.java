/*
 * Created on Apr 6, 2004
 *
 */
package commandParser;
import java.util.Collection;
import java.util.Iterator;
import library.Hold;
import library.LibraryItem;
import library.Catalog;

/**
 * @author User
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CheckTransfersCommand extends Command {
	
	public CheckTransfersCommand(String n, String s) {
		super(n,s);
	}
	
	public void go() {
		Collection holds = Catalog.getHoldList();
			Iterator iter = holds.iterator();
			while (iter.hasNext()){
				Hold h  = (Hold) iter.next();
				LibraryItem item = h.getItem();
				if (!item.isCheckedOut()) {
					System.out.println(h);
				}
			}
	}

	public String usage() {
		return "checktransfers"; 
	}
	
	public  boolean onlyLibrarians() {
		return true;
	}
}
