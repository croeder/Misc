/*
 * Created on Apr 17, 2004
 *
 */
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Chris Roeder
 * 
 * parses lists out of nested square brackets.
 * keep spaces between tokens.
 * Tokens are:
 * left and right square brackets, comma and strings of characters.
 */
public class MoveTreeNode {
    Point from;
    Point to;
    boolean white;
    boolean resolved; // end
    int optimalValue;
	boolean optimal;
    
    String rawData;
    
    List next;
    
    static String[] tokens;
    static int pos;
    static int maxTokens;
    
    
    public MoveTreeNode(Point from, Point to, boolean white, boolean resolved) {
    	this.from = from;
    	this.from = to;
    	this.white = white;
    	this.resolved = resolved;
    	next = new ArrayList();
    }
    
    public MoveTreeNode() {
    	this.from = null;
    	this.to = null;
    	this.white = false;
    	this.resolved = false;
    	next = new ArrayList();
    }
    
    protected void mungeRawData() {
    	//System.out.print("rawData: \"" + rawData + "\" ");
    	// w:x9:x9:-1 leaf
    	// w:x9:x9:   interior node
    	
    	String parts[] = rawData.split("\\.");
    	//System.out.println(parts.length);
    	//for (int i=0; i<parts.length; i++)
    	//	System.out.print(" \""  + parts[i] + "\" ");
    	//System.out.println("");
    	from = new Point(parts[1]);
    	to = new Point(parts[2]);
    	white = parts[0].equals("w");
    	if (parts.length == 4) {
    		resolved = true;
    		optimalValue = Integer.parseInt(parts[3]);
    	}
    	else if (parts.length == 3){
    		resolved = false; 
    	}
    	else {
    		System.out.println("something wrong in mungeRawData()");
    	}
    }
    
    public void add(MoveTreeNode mtn) {
    	next.add(mtn);
    }
    
    public Iterator iterator() {
    	return next.iterator();
    }
    
    public static void main(String args[]) {
    	MoveTreeNode m = null;
    	String test1 = "[ w.x9.x9. : b.c7.c8.-1 , b.c7.d8.0 ]";
    	System.out.println("\n" + test1);
    	m = parse(test1);
        m.traverse("", true);
        
    	String test2 = "[ w.x9.x9. : b.c7.c8.-1 , [ b.c7.d8. : w.h1.h2.1 ] ]";
    	System.out.println("\n" + test2);
    	m = parse(test2);
   		m.traverse("", true);
   		
		String test21 = "[ w.x9.x9. : [ b.c7.d8. : w.h1.h2.1 ] , [ b.c7.d4. : w.h4.h4.3 ] ]";
		System.out.println("\n" + test21);
		m = parse(test21);
		m.traverse("", true);   		
  		
		String test22 = "[ w.x9.x9. : b.c7.c8.-1 , [ b.c7.d8. : w.h1.h2.1 ] , [ b.c7.d4. : w.h4.h4.3 ] ]";
		System.out.println("\n" + test22);
		m = parse(test22);
		m.traverse("", true);   		

    	String test3 = "[ w.x9.x9. : [ b.c7.c8. : w.h1.h2.1 ] , b.c7.d8.-1 ]";
    	System.out.println("\n" + test3);
    	m = parse(test3); 
   		m.traverse("", true);
   		    	String test4 =	
 			"[ w.x9.x9. : [ b.c7.c8. : w.h1.h2.1 , [ w.h1.g1. : b.c8.c9.-1 ] , [ w.h2.h3. :  b.d4.d6.-1 ] ]";
    	System.out.println("\n" + test4);
    	m=parse(test4);
        m.traverse("", true);
    }
    /*
     * 1. Start -> LIST
     * 2. LIST ->  [ BASETHING : THING,  MORETHINGS ]
     * 3. LIST ->  [ BASETHING : THING ]
     * 4. THING -> LIST
     * 5. THING -> BASETHING
     * 6. MORETHINGS -> THING
     * 7. MORETHINGS -> THING, MORETHINGS
     * 8. BASETHING -> t
     */
    public static MoveTreeNode parse(String s) {
    	// with this lame regexp  requires space between punctutation and other tokens
    	MoveTreeNode.tokens = s.split("\\s+");
    	MoveTreeNode.pos = 0;
    	MoveTreeNode.maxTokens = tokens.length;

  	    if ( tokens[pos].equals("[")) {
  	    	pos++;
  	    	MoveTreeNode it=null;
  	    	try {
  	    		it = new MoveTreeNode();
  	    		parseList(it);
			}
			catch (RuntimeException x) {
				x.printStackTrace();
			}
			catch (Exception x) {
				System.out.println(x);
			}
  	    	return it;
  	    }
  	    else {
  	    	System.out.println("error in parse \"" + tokens[pos] + "\" " + pos);
  	    }
  	    return null;
    }
    
    public String toString(boolean highlight) {
    	String temp = "";
    	if (highlight) 
    		temp = "*";
		else
			temp = " ";
    	if (highlight) {
    		return temp + rawData + " *" + optimalValue + "*";
    	}
    	else {
    		return temp + rawData + " " + optimalValue;
    	}
    }
    
    public void traverse(String prefix, boolean onOptimalPath) {
    	System.out.println(prefix + toString(onOptimalPath));
    	Iterator iter = iterator();
    	while (iter.hasNext()) {
    		MoveTreeNode m = (MoveTreeNode) iter.next();
    		m.traverse(prefix + "   ", m.optimal && onOptimalPath );
    	}
    	
    }
    
    public void minmax() {
		Iterator iter = iterator();
		int minmaxValue=0;
		if (white)
			minmaxValue = -1;
		else
			minmaxValue = 1;
		
		if (!resolved) {
			// find the optimal value
			while (iter.hasNext()) {
				MoveTreeNode m = (MoveTreeNode) iter.next();
				m.minmax();
				if (white) {
					// maximizing
					if (m.optimalValue > minmaxValue) {
						minmaxValue = m.optimalValue;
					}
				}
				else {
					// minimizing
					if (m.optimalValue < minmaxValue) {
						minmaxValue = m.optimalValue;
					}
				}
			}
			optimalValue = minmaxValue;
			
			// mark optimal choices
			iter = iterator();
			while (iter.hasNext()) {
				MoveTreeNode m = (MoveTreeNode) iter.next();
				if (m.optimalValue == minmaxValue)
					m.optimal = true;
			}
		}
    }
    
    static boolean isBaseThing(String s) {
    	return (!s.equals("[") && !s.equals("]") && !s.equals(",") 
    	     && !s.equals(":")) ;
    }
    
    static void parseList(MoveTreeNode parent)  throws Exception {
    	//System.out.println("parseList: " + pos + " " + tokens[pos]);
    	
    	if (isBaseThing(tokens[pos])) {
    		parent.rawData = tokens[pos];
    		parent.mungeRawData();
    		pos++;
    	}
    	else {
    		throw new Exception("error 1 in parseList: \"" + tokens[pos] + "\"");
    	}
    	
    	if (!tokens[pos].equals(":")) {
    		throw new Exception("error 2 in parseList: \"" + tokens[pos] + "\"");
    	}
    	pos++;
    	
    	// p should point to the character just after the open brace
    	parent.add(parseThing());
    	while (tokens[pos].equals(",")) {
    		pos++;
    		parent.add(parseThing());
    	}
    	
    	if (!tokens[pos].equals("]")) {
    		throw new Exception("error 3 in parseList \"" + tokens[pos] + "\" " + pos);
    	}
    	pos++;
    	
    	return;
    	
    }
    
    static MoveTreeNode parseThing() throws Exception {
    	//System.out.println("parseThing: " + pos + " " + tokens[pos]);
    	MoveTreeNode it = new MoveTreeNode();
    	if (tokens[pos].equals("[")) {
    		pos++;
    		parseList(it);
    	}
    	else if (!tokens[pos].equals("[") && !tokens[pos].equals("]") && 
    	           !tokens[pos].equals(",")) {
    		it.rawData = tokens[pos];
    		it.mungeRawData();
    		pos++;
    	}
    	else {
    		throw new Exception("error in ParseThing \"");
    	}
    	return it;
    }
    
}
