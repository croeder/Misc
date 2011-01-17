
#ifndef FUNCTIONS_H
#define FUNCTIONS_H
#include "grammarAnalyzer.h"
#include <fstream>
#include <sstream>
#include <iomanip>
#include <iostream>
#include <string>
#include "semantic_record.h"
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

using namespace std;

// global function
bool is_action_symbol(string symbol);

class IsActionSymbol : public unary_function<string, bool> {
private:	
public:
	bool operator() (string s) {
		return is_action_symbol(s);
	}
};

bool is_non_terminal(string symbol);

class IsNonTerminal : public unary_function<string, bool> {
private:	
public:
	bool operator() (string s) {
		return is_non_terminal(s);
	}
};

class IsTerminal : public unary_function<string, bool> {
private:	
public:
	bool operator() (string s) {
		return !is_non_terminal(s);
	}
};

void test_is_non_terminal();

//function object for 'for_each' algorithm used below
struct print_string_yo {
	print_string_yo(ostream &out) : os(out){}
	void operator()(string x) {os << "YO " << x << ' ';}
	ostream &os;
};

struct print_semantic_stack_entry {
	print_semantic_stack_entry(ostream &out, int &i) : os(out),  i(i){}
	void operator()(SemanticStackEntry x) {
		os << i << ":" << x.first << "(\"" << x.second.name() << "\", " << x.second.extract() << ")";
		i++;
	}
	ostream &os;
	int &i;
};
struct print_numbered_string {
	print_numbered_string(ostream &out, int &i) : os(out),  i(i){}
	void operator()(string x) {os << i << ":" << x << ' '; i++;}
	ostream &os;
	int &i;
};

struct print_string {
	print_string(ostream &out) : os(out){}
	void operator()(string x) {os << x << ' ';}
	ostream &os;
};

struct print_quoted_string {
	print_quoted_string(ostream &out) : os(out){}
	void operator()(string x) {os << "\"" << x << "\"" << ' ';}
	ostream &os;
};

struct print_comma_string {
	print_comma_string(ostream &out) : os(out){}
	void operator()(string x) {os << "\"" << x << "\"" << ", ";}
	ostream &os;
};

#endif
