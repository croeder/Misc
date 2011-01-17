/*
 * Created on Apr 16, 2004
 *
 */

/**
 * @author User
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class GenerateNumbers {
	public static void main(String args[]) {
		System.out.println("Generate Numbers");
		int numCombinations =1;
		int numDigits = 3; // same as numBundles would be
		int bundleCounts[] = new int [numDigits];
		int bundleMaxes[] = {2,3,4}; //same as the number of trajectories per bundle 
		
		for (int i=0; i<numDigits; i++) {
			bundleCounts[i] = 0;
			numCombinations *= bundleMaxes[i];
		}
	
	    System.out.println("number combinations: " + numCombinations);
		for (int i=0; i<numCombinations; i++) {
			System.out.print("" + i + " ");
		
			int carry=1;
			for (int col=numDigits-1; col>=0; col-- ){
				if (carry > 0) {
					bundleCounts[col] += carry;
					if (bundleCounts[col] == bundleMaxes[col]) {
						bundleCounts[col] = 0;
						carry=1;
					}
					else {
						carry=0;
					}
				}
			}
			
			for (int col=0; col<numDigits; col++) {
				System.out.print(bundleCounts[col] + " ");
			}
			System.out.println("");
		}
	}
}
