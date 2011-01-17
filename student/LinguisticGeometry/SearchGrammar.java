/*
 * Created on Apr 18, 2004
 *
 */

/**
 * @author Chris Roeder
 *
 * Implement the language of searches grammar.
 * Keep in mind that implementing a grammar to *create* a string, is
 * a little different than parsing a language to *recognize* a string.
 */

class Relationship {
	Relationship() {
		child = 0;
		sibling = 0;
		parent = 0;
	}
	int child;
	int sibling;
	int parent;
}


public class SearchGrammar {
	
	Relationship relationshipTable[] = new Relationship[100];
    int end=0;
    int i=0;
    MoveTreeNode tree;
    
	public static void main(String args[]) {
		MoveTreeNode mtn = MoveTreeNode.parse(page10);
		mtn.minmax();
		mtn.traverse("", true);
		/*
		SearchGrammar sg = new SearchGrammar(mtn);
		sg.generate();
		*/
	}
	
	public SearchGrammar(MoveTreeNode mtn) {
		this.tree = mtn;
		for (int i=0; i<100; i++) {
			relationshipTable[i] = new Relationship();
		}
	}
	
	public String generate() {
		String output="";
		
		return output;
	}
	
	static final String page10 = 
    "[ w.x9.x9. :                                                                         \n" +
	"   [ w.c6.c7. :                                                                      \n" +
	"     [ b.a6.b7. :                                                                    \n" +	"       [ w.c7.c8. : b.b7.c8.-1 ] ,                                                   \n" +
	"       [ w.h8.g7. : b.b7.c7.-1 ]                                                     \n" +
	"     ]                                                                               \n" +
	"   ] ,                                                                               \n" +   
	"   [ w.h8.g7. :                                                                      \n" +
	"     [ b.a6.b6. :                                                                    \n" + 
	"       [ w.c6.c7. : b.b6.c7.-1 ] ,                                                   \n" +
	"       [ w.g7.f6. : b.b6.c6.0 ,                                                      \n" +
    "         [ b.h5.h4. :                                                                \n" +
    "           [ w.c6.c7. :  b.b6.c7.-1 ] ,                                              \n" +
    "           [ w.f6.c5. : b.b6.c6.0 ,                                                  \n" +
    "             [ b.h4.h3. : w.c5.d6.0 ]                                                \n" +
    "           ]                                                                         \n" +
    "         ] ,                                                                         \n" +
    "         [ b.b6.b7. : w.c6.b7.1 ]                                                    \n" +
	"       ]                                                                             \n" +	"     ] ,                                                                             \n" +
	
	"     [ b.h5.h4. :                                                                    \n" +
	"       [ w.c6.c7. :                                                                  \n" +
	"         [ b.a6.b7. :                                                                \n" +
    "           [ w.c7.c8. : b.b7.c8.-1 ] ,                                               \n" +
    "           [ w.g7.f6. : b.b7.c7.-1 ]                                                 \n" +
	"         ]                                                                           \n" +	"       ] ,                                                                           \n" +
	"       [ w.g7.f6. :                                                                  \n" +
	"         [ b.a6.b7. : w.c6.b7.1 ] ,                                                  \n" +
	"         [ b.a6.b6. :                                                                \n" +
	"           [ w.c6.c7. : b.b6.c7.-1 ] ,                                               \n" +
	"           [ w.f6.c5. : b.b6.c6.0 ,                                                  \n" +
	"             [ b.h4.h3. : w.c5.d6.0 ]                                                \n" +	"           ]                                                                         \n" +
	"         ] ,                                                                         \n" +
	"         [ b.h4.h3. : w.f6.c7.0 ]                                                    \n" +
	"       ]                                                                             \n" +
	"     ] ,                                                                             \n" +

    "     [ b.a6.b5. : [ w.c6.c7. : [ b.h5.h4. : w.c7.c8.1 ] ] ]                          \n" +	"  ]  "+
	"]";                                                                                
	   
	   
}
