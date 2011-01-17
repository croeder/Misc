#ifndef PARSER_H
#define PARSER_H
#include <string>
#include <vector> 
#include <list> 
#include <map>
#include <set>
using namespace std;
#include "scanner.h"
#include "semantic_record.h"
#include "SymbolTable.h"

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


class Parser  {
public:
	Parser(Scanner &s, ParseTable &pt, vector<string> &nt, vector<string> &terminals);
	void LLDriver();
	void printLLDriver(string a);
	void start(); 
	void finish();
	void generate(string s1, string s2, string s3, string s4);
	void generate(string s1, int s2, int s3, string s4);
	void generate(string s1, string s2, string s3);
	void generate(string s1, int s2, string s3);
	void generate(string s1, string s2);
	void generate(int s1, string s2);
	void generate(string s2);
 // lookup, enter, checkid, gettemp
	void checkId(string s);
	void assign(const SemanticRecord &target, const SemanticRecord &source);
	void readId(const SemanticRecord &inVar);
	void writeExpr(SemanticRecord &outExpr);
	SemanticRecord genInfix(const SemanticRecord &e1, const SemanticRecord &op, const SemanticRecord &e2);
	SemanticRecord processId();
	SemanticRecord processLiteral();
	SemanticRecord processOp();
	vector<string> decodeActionString(string a);
	SemanticStackEntry &getSemanticStackItemByString(string a);
	SemanticRecord getTemp();
private:
	list<string> token_queue;
	list<string> buffer_queue;
	vector<string> parse_stack;
	vector<SemanticStackEntry> semantic_stack;
	int leftIndex, rightIndex, currentIndex, topIndex;
	Scanner &s;
	ParseTable &parseTable;
	vector<string> nonterminals;
	vector<string> terminals;
	int temp_number;
	//set<string> symbolTable;
	SymbolTable symbolTable;
};
#endif
