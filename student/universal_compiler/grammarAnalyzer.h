#ifndef GRAMMAR_ANALYZER_H
#define GRAMMAR_ANALYZER_H
#include <string>
#include <vector> 
#include <list> 
#include <map>
#include <set>
using namespace std;
#include "scanner.h"
/* 
 * part of work for CSC 5815 Universal Compiler: Theory and Construction, Boris Stilman
 * Fall 2005
 * by  Chris Roeder August-November 2005
 */


typedef  vector<string>  Production;
typedef  multimap<string, Production > Grammar;
typedef  map< string, set<string> > FirstSet;

typedef  pair<string, vector<string> > FullProduction;
typedef  map<FullProduction, int> Reverse;
typedef  map<FullProduction, set<string> > ReversePredictSet;
typedef  map<set<string>, FullProduction > PredictSet;

typedef map< pair<string, string>, FullProduction  > ParseTable;

class GrammarAnalyzer {
public:
	GrammarAnalyzer(string filename);
	void analyze();
	void print();
	void print(vector<string>);
	void print(int, int, string);
	static vector<string> split(const string &source, const string &delimeter);
	void test_split();
//	bool is_terminal(string s);
	static string trim(string);
	bool test_trim();

	set<string> find_derivesLambda();
	void initFirstSet();
	void fillFirstSet();
	void print_firstSet(); 
	set<string> computeFirst(vector<string> symbols);
	void fillFollowSet();
	void print_followSet();
	void buildPredictSet();
	void print_predictSet();
	void fillParseTable();
	void printParseTable_list();
	void printParseTable();

	void LLDriver(Scanner &s);
	void printLLDriver(string a, list<string> &token_queue, vector<string> &parse_stack, list<string> &buffer_queue);


	vector<string> &get_nonterminals() { return nonterminals; }
	vector<string> &get_terminals() { return terminals; }
	ParseTable     &get_parseTable() { return parseTable; }	

	// fix, these should be private:
private:
public: set<string> derivesLambda;
public: Grammar productions;
public: Grammar productions_w_actions;
private: 
	FirstSet firstSet;
	FirstSet followSet;
	PredictSet predictSet;
	ReversePredictSet reversePredictSet;
	ParseTable parseTable;
	Reverse reverse;
	string filename;
	vector<string> symbols;
	vector<string> nonterminals;
	vector<string> terminals;
	vector<string> rightHandSides;
	vector<string> rightHandSides_w_actions;
	vector<string> leftHandSides;
};
#endif
