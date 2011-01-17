/*
 * Created on Apr 6, 2004
 *
 */
package library;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * keeps track of all the LibraryItems (books, dvds, etc.)
 * 
 * @author Chris Roeder
 *
 */
public class Catalog {

	private Collection items = new ArrayList();
	private static Collection holdList = new ArrayList();
	/**
	 * 
	 */
	public Catalog() {
		addTestData();
	}
	private void addTestData() {
		String title = "C#";// How to Program";
		String author = "Deitel";
		LibraryItem i = new Book(title, author, "vvillage", 1200);
		addItem(i);
		
		title="Programming Ruby";
		author="Dave Thomas";
		i = new Book(title, author,  "vvillage", 350);
		addItem(i);
		
		title="Pragmatic Programmer";
		author="Dave Thomas";
		i = new Book(title, author,  "vvillage", 350);
		addItem(i);
		
		title="Against All Enemies: Inside America's war on Terror";// Programmer";
		author="Richard A. Clarke";
		i = new Book(title, author,  "vvillage", 350);
		addItem(i);
		
		title="Against All Enemies: Interpretations of American Military History";// Programmer";
		author="Kenneth J. Hagan";
		i = new Book(title, author, "vvillage",  350);
		addItem(i);
		
		title="Basic Wine";// Programmer";
		author="Suzy Somelier";
		i = new Book(title, author,  "vvillage", 350);
		addItem(i);	
		
		title="Car Buyers Guide";// Programmer";
		author="Car Guy";
		i = new Book(title, author,  "vvillage", 350);
		addItem(i);	
		
		title="Daily Affirmations";// Programmer";
		author="Stuart Smiley";
		i = new Book(title, author,  "vvillage", 350);
		addItem(i);		
	}
	public void addItem(LibraryItem i){
		items.add(i);
	}
	
	/**
	 * Searches for Books by Author. The name must appear in the 
	 * getAuthor() field somewhere.
	 * 
	 * Does a full-table scan, but hey with a wildcard, even Oracle does that.
	 * 
	 * @return returns a Collection of the results. The collection
	 * won't be null, but it might be empty.
	 */
	public ArrayList searchByAuthor(String a){
		ArrayList results = new ArrayList();
		//Object o = authorHash.get(a);
		Object o=null;
		Iterator iter = items.iterator();
		while (iter.hasNext()){
			LibraryItem i = (LibraryItem) iter.next();
			String author = i.getAuthor();
			if (author.indexOf(a) > -1 && i!=null){
				results.add(i);
			}
		}
			
		return results;
	}
	
	/**
	 * Searches for books by Title. Books must have the single word (no spaces)
	 * in the title somewhere.
	 * 
	 * Does a full-table scan, but hey with a wildcard, even Oracle does that.
	 * 
	 * @return returns a Collection of the results. The collection
	 * won't be null, but it might be empty.
	 */	public ArrayList searchByTitle(String a) {
		ArrayList results = new ArrayList();
		Iterator iter = items.iterator();
		while (iter.hasNext()){
			LibraryItem i = (LibraryItem) iter.next();
			String title = i.getTitle();
			if (title.indexOf(a) > -1 && i!=null){
				results.add(i);
			}
		}
	
		return results;
	}
	public static Collection getHoldList() {
		return holdList;
	}
}
