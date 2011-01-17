/*
 * Created on Feb 21, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
import java.util.*;
/**
 * @author User
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class TutEx {

	static final int MIN = 1;
	static final int MAX = 10;
    
	public static void main(String args[]) {
		Set sethash = new HashSet();
		for (int i = MAX; i >= MIN; i--) {
			sethash.add(new Integer(i*i));
		}
		System.out.println("HashSet = " + sethash);
    
		Set setlink = new LinkedHashSet();
		for (int i = MAX; i >= MIN; i--) {
			setlink.add(new Integer(i*i));
		}
		System.out.println(
					 "LinkedHashSet = " + setlink);
    
		Set settree = new TreeSet();
		for (int i = MAX; i >= MIN; i--) {
			settree.add(new Integer(i*i));
		}
		System.out.println("TreeSet = " + settree);
	}
}

