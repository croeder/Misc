/*
 * Created on Apr 6, 2004
 *
 */
package commandParser;
import java.util.Iterator;
import java.util.Collection;
import library.Hold;
import library.Catalog;


/**
 * @author Chris Roeder
 *
 */
public class ShowHoldsCommand extends Command {

	public ShowHoldsCommand(String n, String s){
		super(n,s);
	}
			
	public void go() {
		Collection holds = Catalog.getHoldList();
		Iterator iter = holds.iterator();
		while (iter.hasNext()){
			Hold h  = (Hold) iter.next();
			System.out.println(h);
		}
	}
	
	public String usage() {
		return "holds";
	}
}
