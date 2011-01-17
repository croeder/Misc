/*
 * Created on Apr 7, 2004
 *
 */
package commandParser;
import junit.framework.TestCase;

/**
 * @author User
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CommandTest  extends TestCase {

	public CommandTest (String name) {
		super(name);
	}
	public void testSay() {
		//Command hi = new LoginCommand("silly", "silly test");
		assertEquals("Hello World!", "fail lthis test");
	}
	public static void main(String[] args) {
		junit.textui.TestRunner.run(
			CommandTest.class);
	}
}