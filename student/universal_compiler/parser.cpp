#include "parser.h"
#include "eop.h"
#include <fstream>
#include <sstream>
#include <iomanip>
#include <iostream>
#include <string>
#include "string.h"
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
#include "unistd.h"

using namespace std;

#include "functions.h"
#include "SymbolTable.h"
StringSpace ss;


Parser::Parser(Scanner &s, ParseTable &pt, vector<string> &nt, vector<string> &t) : s(s), parseTable(pt), nonterminals(nt), terminals(t){
	leftIndex=-1; rightIndex=-1; currentIndex=0; topIndex=1;
}

void Parser::printLLDriver(string a) {
	cout << "\n------------------------------------>\n";
	cout << leftIndex << ", " << rightIndex << ", " << currentIndex << ", " << topIndex << endl;
	//cout << semantic_stack.size() << endl;
	ostringstream ss1;
	//for_each(token_queue.begin(), token_queue.end(), print_string(ss1)); 
	ss1 << a << " ";
	for_each(buffer_queue.begin(), buffer_queue.end(), print_string(ss1)); 

	ostringstream ss2;
	for_each(parse_stack.rbegin(), parse_stack.rend(), print_string(ss2));

	ostringstream ss3;
	int c=0;
	for_each(semantic_stack.begin(), semantic_stack.end(), print_semantic_stack_entry(ss3,c));

	cout << left << "Token: " << ss1.str() << endl; ;
	cout << left  << "Parse (l-r): " << ss2.str() << endl;
	cout << "Semantic (r-l): " << left << ss3.str() << endl;
	cout << "<------------------------------------\n\n";
}

SemanticRecord Parser::getTemp() {
	ostringstream ss1;
	ss1 << "temp_" << temp_number++;
	
	return SemanticRecord(ss1.str(), 0, 's');
}

void Parser::LLDriver() {

  int scope_depth=0;

	temp_number=0;	
	// init token queue

	// init token queue from Scanner
	while (s.decodeTokenType(s.getNextToken()) != "Eof") {
		while (s.decodeTokenType(s.getNextToken()) == "WhiteSpace" ||
			  s.decodeTokenType(s.getNextToken()) == "Comment") {
			s.advance();
		}
		string a=s.decodeTokenType(s.getNextToken());
		s.advance(); 	
	
		token_queue.push_back(a);
		string b = s.getTokenBuffer();
		buffer_queue.push_back(b);
	}
	token_queue.push_back("Eof");
	buffer_queue.push_back("Eof");
	
	parse_stack.push_back("<system-goal>");
	SemanticRecord sys_goal;
	SemanticStackEntry se("<system-goal>", sys_goal);
	semantic_stack.push_back(se);
	string a=token_queue.front();
	string current_buffer=buffer_queue.front();	
	token_queue.pop_front();
	buffer_queue.pop_front();

	while (parse_stack.size() > 0)  {
		string X = parse_stack.back();
		vector<string>::iterator i;
		bool X_is_a_nonterminal = false;
		bool X_is_a_terminal = false;
		if (X[0] != '#') {
			for (i = nonterminals.begin(); i != nonterminals.end(); i++) {
				if (X == *i)	{
					X_is_a_nonterminal = true;	
					break;
				}
			}
			for (i = terminals.begin(); i != terminals.end(); i++) {
				if (X == *i)	{
					X_is_a_terminal = true;	
					break;
				}
			}
		}

		if (X_is_a_nonterminal) {
//cout << "nonterminal: " << X << endl;
			pair<string, string> entry(X, a);
			ParseTable::iterator tableIter = parseTable.find(entry);
			if (tableIter != parseTable.end()  ) {
				FullProduction fp (tableIter->second);

				// output
				/***
				cout << "(" << X << ", " << a << ") " << "predict: \"" << fp.first << "\" --> ";	
				for (i = fp.second.begin(); i != fp.second.end(); i++) {
					cout << "\"" << *i << "\" ";
				}
				cout << endl;
				**/

				printLLDriver(a);
				//cout << endl;

				// pop X from the parse stack
				parse_stack.pop_back();

				// Push EOP onto parse stack
				EndOfProduction eop(leftIndex, rightIndex, currentIndex, topIndex);
				parse_stack.push_back(eop.to_string());

				// push YmYm-1...Y1 onto the parse stack
				vector<string>::reverse_iterator ri;
				for (ri = fp.second.rbegin(); ri != fp.second.rend(); ri++) {
					parse_stack.push_back(*ri);	
				}	

				// push non-actions symbols Y1Y2..Yn onto semantic stack
				int m=0;
				vector<string>::iterator i;
				for (i = fp.second.begin(); i != fp.second.end(); i++) {
					if (!is_action_symbol(*i)) {
//cout << *i << " "; 
						SemanticRecord term;
						SemanticStackEntry se(*i, term);
						semantic_stack.push_back(se);	
						m++;
					}
				}	
//cout << "  m is: " << m << endl;
			
				//update indeces
				leftIndex = currentIndex; rightIndex = topIndex; currentIndex = rightIndex;
				topIndex += m;
			}
			else {
				// syntax error XXXXXX
				cout << "syntax error 1 " << endl;
				cout << "parse stack: ";
				for_each(parse_stack.rbegin(), parse_stack.rend(), print_string(cout));
				cout << endl;
				exit(1);
			}
		}
		else if (X_is_a_terminal) {
//cout << "X is a TERMINAL" << endl;
			if (X == "Lambda") {
				//cout << "match:" << X << endl;
				printLLDriver(a);
				parse_stack.pop_back();
			}
			else if (X == a) {
				//cout << "match:" << X ;
				if (X=="Eof") exit(1); // there will be no more tokens and the following lines will crash
				if (a=="PlusOp" || a =="MinusOp") {
					// store the named version in the record so that's what gets output as an instruction
					SemanticRecord sr(a, 0, 's');
					SemanticStackEntry sse(X , sr);
					semantic_stack[currentIndex] = sse;
				}
				else {
					SemanticRecord sr(current_buffer, 0, 's');
					SemanticStackEntry sse(X , sr);
					semantic_stack[currentIndex] = sse;
				}
				printLLDriver(a);
				parse_stack.pop_back();
				a=token_queue.front();
				current_buffer=buffer_queue.front();	
				token_queue.pop_front();
				buffer_queue.pop_front();
				
				currentIndex++;
			}
		}
		else if (EndOfProduction::is_EOP(X)) {
			EndOfProduction eop(X);
			leftIndex = eop.left();	
			rightIndex = eop.right();	
			currentIndex = eop.current();	
			topIndex = eop.top();	

			// trim stack down to the new top
			while (semantic_stack.size() > topIndex) {
				semantic_stack.pop_back();
			}

		currentIndex++;
		parse_stack.pop_back();
		}
		else if (is_action_symbol(X)) {
//cout << "Action: " << X << endl;
			parse_stack.pop_back();
			vector<string> arg_symbols = decodeActionString(X);
			for (vector<string>::iterator i=arg_symbols.begin(); i!=arg_symbols.end(); i++) {
				SemanticStackEntry sse = getSemanticStackItemByString(*i);	
				//cout << "found:" << sse.first << ", " << sse.second.name() << " " << sse.second.extract() << endl;
			}
			if (X.find("#Start") != string::npos) {
				//cout << "found #Start" << endl;
			}
			else if (X.find("#Assign") != string::npos) {
				assign(getSemanticStackItemByString(arg_symbols[1]).second, getSemanticStackItemByString(arg_symbols[0]).second); 
			}
			else if (X.find("#ReadId") != string::npos) {
				//cout << "found #ReadId" << endl;
				readId(getSemanticStackItemByString(arg_symbols[0]).second);
			}
			else if (X.find("#WriteExpr") != string::npos) {
				writeExpr(getSemanticStackItemByString(arg_symbols[0]).second);
			}
			else if (X.find("#Copy") != string::npos) {
				getSemanticStackItemByString(arg_symbols[1]).second = getSemanticStackItemByString(arg_symbols[0]).second; 
			}
			else if (X.find("#GenInfix") != string::npos) {
				getSemanticStackItemByString(arg_symbols[3]).second =
					genInfix(getSemanticStackItemByString(arg_symbols[0]).second,
							 getSemanticStackItemByString(arg_symbols[1]).second,
							 getSemanticStackItemByString(arg_symbols[2]).second);
			}
			else if (X.find("#ProcessLiteral") != string::npos) {
				getSemanticStackItemByString(arg_symbols[0]).second = processLiteral();
			}
			else if (X.find("#ProcessOp") != string::npos) {
				cout << "found #ProcessOp" << endl;
				getSemanticStackItemByString(arg_symbols[0]).second = processId();
			}
			else if (X.find("#ProcessId") != string::npos) {
				getSemanticStackItemByString(arg_symbols[0]).second = processId();
				string var = getSemanticStackItemByString(arg_symbols[0]).second.name();
				SymbolAttributes *sap;
				int scope = symbolTable.find_scope(var.c_str(), sap, scope_depth);
				string scoped_var = "";
				if (scope > 0) {
				  // found
				  ostringstream ss;
				  ss << var << "_" << scope;
				  scoped_var = ss.str();
				}
				else {
				  // new
				  ostringstream ss;
				  ss << var << "_" << scope_depth ;
				  scoped_var = ss.str();
				  symbolTable.enter(var.c_str(), scope_depth);
				  checkId(scoped_var);
				}
				// Modify Semantic Record to have a scoped name
				getSemanticStackItemByString(arg_symbols[0]).second.setName(scoped_var);
			}
			else if (X.find("#Begin") != string::npos) {
			  scope_depth++;
			}
			else if (X.find("#End") != string::npos) {
			  // close out a level in the symbol table
			  symbolTable.clear_scope(scope_depth);
			  scope_depth--;
			  if (scope_depth==0) {
				finish();
			  }
			}
		}
		else {
			// syntax error XXXXX
			cout << "syntax error 2  X:" <<  X << " a: " << a << endl;
			cout << "parse stack: ";
			for_each(parse_stack.rbegin(), parse_stack.rend(), print_string(cout));
			cout << endl;
			exit(1);
		}
	}
}


vector<string> Parser::decodeActionString(string a) {
	// just chops an actions symbol's argument list up
	int x = a.find("(");
	int y = a.find(")");
	vector<string> parts;
	if (x != string::npos && y != string::npos) {
		string var_list = a.substr(x + 1,y-x -1);
		int c;
		while ( (c=var_list.find(",")) != string::npos ) {
			parts.push_back(var_list.substr(0, c));
			var_list = var_list.substr(c+1);
		}
		parts.push_back(var_list);
		return parts;
	}
}

SemanticStackEntry &
Parser::getSemanticStackItemByString(string a) {
	// take a arg string from an action item by one of those $x symbols
	if (a == "$$") {
		return semantic_stack[leftIndex];
	}
	else if (a == "$1") {
		return semantic_stack[rightIndex];
	}
	else if (a == "$2") {
		return semantic_stack[rightIndex + 1];
	}
	else if (a == "$3") {
		return semantic_stack[rightIndex + 2];
	}
	else if (a == "$4") {
		return semantic_stack[rightIndex + 3];
	}
	else if (a == "$5") {
		return semantic_stack[rightIndex + 4];
	}
}

void Parser::start() {
	//max_temp =0 ;
}

void Parser::finish()  {
	generate("Halt");
}

void Parser::generate(string s1, int s2, int s3, string s4) {
  cout << "INSTR: (" << s1 << ",\t" << s2 << ",\t" << s3 << ",\t" << s4 << ")" << endl;
}

void Parser::generate(string s1, string s2, string s3, string s4) {
  cout << "INSTR: (" << s1 << ",\t" << s2 << ",\t" << s3 << ",\t" << s4 << ")" << endl;
}

void Parser::generate(string s1, string s2, string s3) {
  cout << "INSTR: (" << s1 << ",\t" << s2 << ",\t" << s3 << ")" << endl;
}

void Parser::generate(string s1, int s2, string s3) {
  cout << "INSTR: ( " <<  s1 << ",\t" << s2 << ",\t" << s3 << ")" << endl;
}

void Parser::generate(string s1, string s2) {
  cout << "INSTR: (" << s1 << ",\t" << s2 << ")" << endl;
}

void Parser::generate(int s1, string s2) {
  cout << "INSTR: (" << s1 << ",\t" << s2 << ")" <<  endl;
}

void Parser::generate(string s1) {
  cout << "INSTR: (" << s1 <<  ")" << endl;
}

 // lookup, enter, checkid, gettemp

void Parser::checkId(string s) {
	//if (!keyword(s)) { // ????FIX
		generate("Declare", s, "Integer");
	//}
}
void Parser::assign(const SemanticRecord &target, const SemanticRecord &source) {
	generate("Store",  source.name(), target.name());
}

void Parser::readId(const SemanticRecord &inVar) {
	generate("Read", inVar.name());
}

void Parser::writeExpr(SemanticRecord &outExpr) {
	generate("Write", outExpr.name(), "Integer");
}

SemanticRecord Parser::genInfix(const SemanticRecord &e1,  const SemanticRecord &op,  const SemanticRecord &e2) {
	SemanticRecord eRec;
	eRec = getTemp(); 
	//generate(op.name(), e1.extract(), e2.extract(), eRec.name());
	generate(op.name(), e1.name(), e2.name(), eRec.name());
	return eRec;
}

SemanticRecord Parser::processId() {
  return   semantic_stack[currentIndex-1].second;
}

SemanticRecord Parser::processLiteral() {
	return semantic_stack[currentIndex-1].second;
}

SemanticRecord Parser::processOp() {
	return semantic_stack[currentIndex-1].second;
}
