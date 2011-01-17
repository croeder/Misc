#include "grammarAnalyzer.h"
#include <fstream>
#include <sstream>
#include <iomanip>
#include <iostream>
#include <string>

// STL references:
// _The_C++_Standard_Library_, Nicolai Josuttis, Addison-Wesley, 1999
// _Generic_Programming_and_the_STL_, Matthew H. Austern, Addison-Wesley, 1999
/* 
 * part of work for CSC 5815 Universal Compiler: Theory and Construction, Boris Stilman
 * Fall 2005
 * by  Chris Roeder August-November 2005
 */


#include <vector>
#include <list>
#include <map>
#include <set>
#include <algorithm>
#include <iterator>
#include <functional>

#include "functions.h"

using namespace std;

// global function
bool is_action_symbol(string symbol) {
	int start = symbol.find("#");
	return (start==0);
}

bool is_non_terminal(string symbol) {
	int start = symbol.find("<");
	int end = symbol.find(">");
	return (start==0 && end==(symbol.length()-1));
		
	// its a non-terminal if it starts with a < and ends with a >
	// any characters before or after are spaces
}


void test_is_non_terminal() {
	string test1 = "<abc>";
	string test2 = "x<abc>";
	string test3 = "<abc>x";
	string test4 = "<abcx";
	string test5 = "abc>x";
	string test6 = "abcx";
	if (!is_non_terminal(test1))  cout << "test1: " <<  test1 << " failed." << endl;
	if (is_non_terminal(test2))  cout << "test2: " <<  test2 << " failed." << endl;
	if (is_non_terminal(test3))  cout << "test3: " <<  test3 << " failed." << endl;
	if (is_non_terminal(test4))  cout << "test4: " <<  test4 << " failed." << endl;
	if (is_non_terminal(test5))  cout << "test5: " <<  test5 << " failed." << endl; 
	if (is_non_terminal(test6))  cout << "test6: " <<  test5 << " failed." << endl;
}


//function object for 'for_each' algorithm used below

