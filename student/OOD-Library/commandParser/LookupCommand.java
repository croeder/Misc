/*
 * Created on Apr 5, 2004
 *
 */
package commandParser;
import library.Catalog;
import java.util.ArrayList;

/**
 * @author Chris Roeder
 *
 */
public class LookupCommand extends Command {
	private String title=null;
	private String author=null;
	private Catalog itemCatalog=null;
	private String prefix = "Error: LookupCommand: ";
	public String usage() {
		return "lookup has two forms:\n" +
		       "  lookup title=<yourtitlehere>\n" +
		       "  lookup author=<yourauthorhere>";
	}
	
	public LookupCommand(String n, String s, Catalog c) {
		super(n,s);
		itemCatalog = c;
		
		int numParams=0;
		if (paramHash.get("title") != null) {
			title = (String) paramHash.get("title");
			numParams++;
		}
		if (paramHash.get("author") != null) {
			author = (String) paramHash.get("author");
			numParams++;
		}
		 if (numParams > 1) {
			System.err.println(prefix + " lookup only takes one parameter");
			usage();
		}
	}
	
	public void go() {
		ArrayList c=null;
		if (title != null && author == null){
			c=itemCatalog.searchByTitle(title);
		}
		else if (title == null && author != null){
			c=itemCatalog.searchByAuthor(author);
		}
		else {
			System.out.println(prefix + " lookup command needs either title or author parameter"); // FIX
			return;
		}
		System.out.println("lookup found " + c.size() + " results:");
		CommandProcessor.getProcessor().setResults(c);
		CommandProcessor.getProcessor().printResults();
	}
	
	
}
