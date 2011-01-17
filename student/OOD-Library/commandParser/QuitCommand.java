
package commandParser;

/**
 * @author Chris Roeder
 *
 */
public class QuitCommand extends Command {

	private CommandProcessor commandParser;
	public String usage() {
		return "quit";
	}
	public QuitCommand(String n, String s, CommandProcessor cp) {
		super(n,s);
		commandParser = cp;
	}
	
	public void go(){
		System.out.println("quitting library system");
		commandParser.quitLoop();
	}

}
