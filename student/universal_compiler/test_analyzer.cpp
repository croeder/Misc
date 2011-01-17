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
	//ga.test_split();
	if (!ga.test_trim())
		cout << "trim failed" << endl;
	ga.analyze();
	cout << "------printing-------" << endl;
	ga.print();


	//cout << "------find lambda-------" << endl;
	set<string> derivesLambda = ga.find_derivesLambda();
	for_each(derivesLambda.begin(), derivesLambda.end(), print_string(cout));
	ga.derivesLambda = derivesLambda;
	cout << endl;

	cout << "------test initFirst-------" << endl;
	ga.initFirstSet();
	ga.print_firstSet();	
	cout << "---------------------------" << endl;
		

	cout << "------fillFirstSet-------" << endl;
	ga.fillFirstSet();
	cout << " First Set contents: " << endl;
	ga.print_firstSet();	
	cout << "------------------------------------------" << endl;

	//cout << endl;
	cout << "------fillFollowSet-------" << endl;
	ga.fillFollowSet();
	ga.print_followSet();
	cout << endl;

	cout << "----- fillPredictSet-----" << endl;
	ga.buildPredictSet();
	ga.print_predictSet();

	cout << "----- fillParseTable-----" << endl;
	ga.fillParseTable();
	ga.printParseTable_list();
	ga.printParseTable();
	cout << endl;


	cout << "----- do Parse ------" << endl;
	if (argc > 2) {
		cout << "------ Init Scanner----" << endl;
		Scanner s(argv[2]);
		
		cout << "------------Init Parser ----------" << endl;
		Parser p(s, ga.get_parseTable(), ga.get_nonterminals(), ga.get_terminals());

		cout << "-------------LLDriver ----------" << endl;
		p.LLDriver();
		//cout << endl;
	}
	else {
		cout << "need both a grammar and an input file, in that order:" << endl;
		cout << "analyzer <grammar> <input_source>" << endl;
	}
}
