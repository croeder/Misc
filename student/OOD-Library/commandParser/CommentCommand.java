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
public class CommentCommand extends Command {

    String text=null;
	public CommentCommand(String n, String s) {
		super(n,s);
		if ((text = (String) paramHash.get("text")) == null) {
		    System.out.println("CommentCommand usually has a parameter \"text\"");
		}
	}
	
	public void go() {
		//System.out.print("\nComment: " + text);
	}
	
	public String usage() {
		return "comment text=<text>";
	}
}
