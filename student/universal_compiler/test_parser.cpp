#include "grammarAnalyzer.h"
#include "parser.h"
#include <iostream>
#include <string>
#include <set> 
#include <algorithm>
using namespace std;
/* 
 * part of work for CSC 5815 Universal Compiler: Theory and Construction, Boris Stilman
 * Fall 2005
 * by  Chris Roeder August-November 2005
 */


struct print_string {
	print_string(ostream &out) : os(out){}
	void operator()(string x) {os << x << ' ';}
	ostream &os;
};

int main(int argc, char **argv) {
	if (argc < 2) {
		cout << "usage: analyzer <grammarfile>" << endl;
		exit(1);
	}
	GrammarAnalyzer ga(argv[1]);
	if (!ga.test_trim())
		cout << "trim failed" << endl;
	ga.analyze();
	set<string> derivesLambda = ga.find_derivesLambda();
	ga.derivesLambda = derivesLambda;
	ga.initFirstSet();
	ga.fillFirstSet();
	ga.fillFollowSet();
	ga.buildPredictSet();
	ga.fillParseTable();


	if (argc > 2) {
		Scanner s(argv[2]);
		Parser p(s, ga.get_parseTable(), ga.get_nonterminals(), ga.get_terminals());
		p.LLDriver();
	}
	else {
		cout << "need a grammar, a grammar with action symbols and an input file, in that order:" << endl;
		cout << "analyzer <std grammar> <grammar with action> <input_source>" << endl;
	}
}
