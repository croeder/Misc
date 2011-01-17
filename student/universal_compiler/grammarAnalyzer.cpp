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

using namespace std;

#include "functions.h"

GrammarAnalyzer::GrammarAnalyzer(string filename)  {
	this->filename = filename;
}

string GrammarAnalyzer::trim(string symbol) {
	// trim spaces off beginning and end of a symbol
	string cleaned;
	string lower = "abcdefghijklmnopqrstuvwxyz";
	string number = "0123456789";
	string upper= "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	string symbols = "<>-_#$)";

	int start=symbol.find_first_of(lower + number + upper + symbols);
	int end = symbol.find_last_of(lower + number + upper + symbols);
	if (start > -1 && end > -1) {
		cleaned = symbol.substr(start, end-start+1);
	}

	return cleaned;
}

bool GrammarAnalyzer::test_trim() {
	string input0="";
	string input1="  abc ";
	string input2="abc ";
	string input3="  abc";
	string correct="abc";
	string result;
	if ((result=trim(input0)) != "") {
		cout << "\"" << input0 << "\"" << result << "\"" << endl;
		return false;
	}
	if ((result=trim(input1)) != correct) {
		cout << "\"" << input0 << "\"" << result << "\"" << endl;
		return false;
	}
	if ((result=trim(input2)) != correct) {
		cout << "\"" << input0 << "\"" << result << "\"" << endl;
		return false;
	}
	if ((result=trim(input3)) != correct) {
		cout << "\"" << input0 << "\"" << result << "\"" << endl;
		return false;
	}

	return true;
}
 
vector<string> GrammarAnalyzer::split(const string &source, 
									const string &delimiter) {

	vector<string> symbols;
	int start=0;
	int end = source.find(delimiter);
	while (end != string::npos ) {
		if (end > start) {
			string part = source.substr(start, end-start);
			symbols.push_back(trim(part));
			start = end;
			end = source.find(delimiter, start);
		}
		else {
			start++;
			end = source.find(delimiter, start);
		}
	}
	// might still be a symbol at the end
	if (start < source.size()) {
		string part = trim(source.substr(start, source.size()));
		if (part != "")
			symbols.push_back(part);
	}
			
	return symbols;	
}


void GrammarAnalyzer::test_split() {
	string test = " ab c def  g  hi jklmn";
	vector<string> parts = split(test, " ");
	for (vector<string>::iterator it = parts.begin();
		 it != parts.end();
		 it++) {
		cout << *it << endl;
	}
	
}

void GrammarAnalyzer::analyze() {
	ifstream in;
	in.open(filename.c_str());
	string line;
	int count=1;
	while (!in.eof()) {
		getline(in, line);
		int arrow = line.find("->");
		if (arrow >  -1) {
			string lhs = line.substr(0, arrow);
			string rhs = line.substr(arrow +2, line.size() - arrow);
			vector<string> temp_vector;
			vector<string> temp;
			vector<string> left_parts; 
			vector<string> right_parts;

			temp_vector = split(lhs, " ");
			if (temp_vector.size() > 1) {
				cout <<  "limit of one symbol on left-hand side of productions" << endl;
				cout <<  "  " << line << endl;	
				for_each(temp_vector.begin(), temp_vector.end(), print_quoted_string(cout));
				exit(1);
			}
			left_parts.insert(left_parts.begin(), temp_vector.begin(), temp_vector.end());
			set_union(leftHandSides.begin(), leftHandSides.end(), left_parts.begin(), left_parts.end(),
									inserter(temp, temp.begin()) );
			leftHandSides = temp;
			temp.clear();
			temp_vector = split(rhs, " ");
			right_parts.insert(right_parts.begin(), temp_vector.begin(), temp_vector.end());

			// add this batch to list with action symbols
			productions_w_actions.insert(make_pair(left_parts[0],right_parts));

			// filter out the action symbols
			vector<string>::iterator new_end = remove_if(right_parts.begin(), right_parts.end(), IsActionSymbol());
			right_parts.erase(new_end, right_parts.end());

			// add this batch to list withOUT action symbols
			set_union(rightHandSides.begin(), rightHandSides.end(), right_parts.begin(), right_parts.end(),
									inserter(temp, temp.begin()) );
			rightHandSides = temp;

			productions.insert(make_pair(left_parts[0],right_parts));
			FullProduction fp =  make_pair(left_parts[0], right_parts); 
			reverse.insert(make_pair(fp,count));
			count++;
		}
	}
	set_union(rightHandSides.begin(), rightHandSides.end(), leftHandSides.begin(), leftHandSides.end(),
									inserter(symbols, symbols.begin()) );

	terminals = symbols;
	nonterminals = symbols;

	vector<string>::iterator new_end = remove_if(nonterminals.begin(), nonterminals.end(), IsTerminal());
	nonterminals.erase(new_end, nonterminals.end());
	new_end = remove_if(nonterminals.begin(), nonterminals.end(), IsActionSymbol());
	nonterminals.erase(new_end, nonterminals.end());

	new_end = remove_if(terminals.begin(), terminals.end(), IsNonTerminal());
	terminals.erase(new_end, terminals.end());
	new_end = remove_if(terminals.begin(), terminals.end(), IsActionSymbol());
	terminals.erase(new_end, terminals.end());
}

void GrammarAnalyzer::print(vector<string> in) {
	for (vector<string>::iterator it = in.begin();
		 it != in.end();
		 it++) {
		cout << *it << endl;
	}
}
 
// -------------- Assignment 6 starts here ------------------

set<string> GrammarAnalyzer::find_derivesLambda() {
	bool changes=true;
	set<string> derivesLambda;
	derivesLambda.insert("Lambda");
	
	while(changes) {
		for (Grammar::iterator p = productions.begin(); p != productions.end(); p++) {
			// this p derives lambda if its left symbol is in the set of symbols that derive lambda
			bool save_this_p_derives_lambda =  (derivesLambda.find(p->first) != derivesLambda.end());
			bool this_p_derives_lambda = true;


			// this p dervies lambda if each of its symbols derives lambda (if each is in the set)
			for (Production::iterator s = p->second.begin(); s != p->second.end(); s++) {
				// this symbol derives lambda if its in the set 
				// bug ignore action symbols
				if (!is_action_symbol(*s)) {
					this_p_derives_lambda &= derivesLambda.find(*s) != derivesLambda.end(); 
				}
			}
			// if this symbol (the lhs of the production) now derives lambda, put it in the set
			if (this_p_derives_lambda)
				derivesLambda.insert(p->first);
			changes = this_p_derives_lambda != save_this_p_derives_lambda;
		}
	}	

	return derivesLambda;
}

void GrammarAnalyzer::initFirstSet() {

	// This function's job is to find the first set,
	// the hard work is done by ComputeFirst(). This one
	// really just does the obvious initialization
	// It doesn't even follow the chain of rules like in findLambda.
	// That's left up to the last loop and ComputeFirst().

	// check non-terminals for those that derive Lambda	 (Step 1)
	for (vector<string>::iterator nti=nonterminals.begin(); nti!=nonterminals.end(); nti++) {
		if (derivesLambda.find(*nti)!=derivesLambda.end()) {
			set<string> s;
			s.insert("Lambda");
			firstSet[*nti] = s;
		}
		else {
			set<string> s;
			firstSet[*nti] = s;
		}
	}

	/* For each terminal, for each rule, see if that rule produces a string 
	 * that starts with the terminal.   (Step 2) */
	for (vector<string>::iterator ti=terminals.begin(); 
 							  	  ti!=terminals.end(); ti++) {
		for (Grammar::iterator p=productions.begin(); p!=productions.end(); p++) {
			// find first non action symbol's index i
			int  i = 0;
			while (is_action_symbol(p->second[i])) {
				i++;
			}
			if (p->second[i] == *ti) { 
				firstSet[p->first].insert(*ti);
			}
		}
	}
}

void GrammarAnalyzer::fillFirstSet() {
	initFirstSet();
	// repeat until you don't see any changes (Step 3):
	//   combine each production's FirstSet with what ComputeFirst comes up with...
	bool changes = true;
	while (changes) {
		changes = false;
		for (Grammar::iterator p=productions.begin(); p!=productions.end(); p++) {
			set<string> cf = computeFirst(p->second);
			set<string> old = firstSet[p->first];
			firstSet[p->first].insert(cf.begin(), cf.end());

			set<string> both;
			set_difference(cf.begin(), cf.end(), old.begin(), old.end(),	
				inserter(both, both.begin()) );
			changes = changes || (both.size() > 0);
		}	
	}

	// add non-terminals
	for (vector<string>::iterator it = terminals.begin();
		it != terminals.end(); it++) {
		firstSet[*it].insert(*it);
	}
}


/**

// BROKEN
set<string> GrammarAnalyzer::computeFirst(vector<string> symbols) {
	// computes the first set for a whole production, given the sets for individual symbols
	// I really like the commented out implementation below better.
	// I believe it does the same as this one, and is much simpler.
	// However, suspicion remains as Dr. Stilman made a comment about making sure
	// lambda is found in the most recent set. I'll make it work this way too.
	set<string> result;

	if (symbols.size() == 0) {
		result.insert("Lambda");
		return result;
	}
	else {
		// always include firsSet of first symbol, and remove lambda from the *result*
		result.insert( firstSet[symbols[0]].begin(), firstSet[symbols[0]].end() );
		result.erase("Lambda");

		// if lambda is a member of the set you just included, include the next one, but remove lambda here
		int i=1;
		while (i < symbols.size() && firstSet[symbols[i-1]].find("Lambda") != firstSet[symbols[i-1]].end()) {
			result.insert( firstSet[symbols[i]].begin(), firstSet[symbols[i]].end() );
			result.erase("Lambda");
			++i;
		}

		// if you got through them all, and the last one had lambda, add it back in.
		if (i == symbols.size() && firstSet[symbols[i-1]].find("Lambda") != firstSet[symbols[i-1]].end()) {
			firstSet[symbols[i-1]].insert("Lambda");	
		}
	}
}
*/
set<string> GrammarAnalyzer::computeFirst(vector<string> symbols) {
	// given the right side of a production, find all the symbols
	// that could possibly start a string the production produces.
	// As long as a symbol produces a set without lambda, that's it.
	// If a symbol can produce lambda, then you have to include the
	// the symbols the next symbol may produce.
	// Dr. Stilman made a comment in class about making sure that lambda was
	// found in the most recent set included. I beleive this algorithm is
	// careful in that regard in that when checking for lambda in the entire
	// result set (instead of the most recent), any lambda found can only
	// come from the most recent set added, because it is always removed.

	set<string> result;
	if (symbols.size() == 0) {
		result.insert("Lambda");
	}
	else {
		int i=0;

		// if the symbol isn't in the hash, STL will insert it for you...
		// ...unless you check first
		if (firstSet.find(symbols[i]) != firstSet.end()) {
			result.insert(firstSet[symbols[i]].begin(), 
     					  firstSet[symbols[i]].end() );
		}
		++i;

		// If lambda is a member of the set you just included, include the next one,
		//  but remove lambda here
		while (i < symbols.size() && result.find("Lambda") != result.end()) {
			result.erase("Lambda");
			if (firstSet.find(symbols[i]) != firstSet.end()) {
				result.insert(firstSet[symbols[i]].begin(), 
     						  firstSet[symbols[i]].end() );
			}
			++i;
		}
	}
	return result;
}

void GrammarAnalyzer::print_firstSet() {
	for (FirstSet::iterator i = firstSet.begin(); i != firstSet.end(); ++i) {
		cout << setw(25) << i->first << " --> ";
		for_each(i->second.begin(), i->second.end(), print_string(cout));
		cout << endl;
	}
}

void GrammarAnalyzer::print_followSet() {
	for (FirstSet::iterator i = followSet.begin(); i != followSet.end(); ++i) {
		cout <<  "followSet(" <<  setw(15) << i->first << ") = {";
		for_each(i->second.begin(), i->second.end(), print_comma_string(cout));
		cout << " }" << endl;
	}
}

void GrammarAnalyzer::print_predictSet() {
	for (PredictSet::iterator i = predictSet.begin(); 
							  i != predictSet.end(); ++i) {
		ostringstream ss1;
		for_each(i->first.begin(), i->first.end(), print_comma_string(ss1));
		cout << "{ " << setw(30) << ss1.str() <<  " } " << "predicts: ";

		ostringstream ss2;
		cout << setw(14) << i->second.first << " --> ";
		for_each(i->second.second.begin(), i->second.second.end(), print_string(ss2));
		cout << left <<  setw(37) << ss2.str() << endl;
	}
}

string getNextNonTerminal(vector<string>::iterator i, vector<string>::iterator e,
						  vector<string> &y) {
	// starting after i, return the next symbol that is a non-terminal
	// returns "" if all are terminals

// starting *with* i???
	///++i;
	string retval="";
	while (i != e) {
		if (::is_non_terminal(*i)) {
			retval = *i;
			break;
		}
		++i;
	}
	if (i != e)
		++i;
	while (i != e) {
		y.push_back(*i);
		++i;
	}
	return retval;
}

void GrammarAnalyzer::fillFollowSet() {

	// init
	for (vector<string>::iterator nti=nonterminals.begin(); 
								  nti!=nonterminals.end(); ++nti) {
		set<string> s;
		followSet[*nti] = s;
	}
	set<string> l;
	l.insert("Lambda");
	followSet["<system-goal>"]  = l;


	// big loop
	bool changes=false;
	int count=0;
	do {
		changes = false;
		for (Grammar::iterator p=productions.begin(); 
                               p!=productions.end(); ++p) {
			string A = p->first;

			// for each symbol in the production
			for (vector<string>::iterator s=p->second.begin(); 
			                             s!=p->second.end(); ++s) {

				if ((*s)[0] != '#') {

			    set<string> old;
				set<string> myfirstSet;
				vector<string> y;
				string B=getNextNonTerminal(s, p->second.end(), y);
				if (B != "" ) {
					if (y.size() > 0) {
						myfirstSet = firstSet[y[0]]; // = computeFirst(y);
					}
					else {
						myfirstSet.insert("Lambda");	
					}
					old = followSet[B];
					set_union( followSet[B].begin(), followSet[B].end(),
 					           myfirstSet.begin(), myfirstSet.end(), 
						       inserter(followSet[B], followSet[B].begin()) );
					followSet[B].erase("Lambda");	
					if ( myfirstSet.find("Lambda") != myfirstSet.end()) {
						set_union( followSet[B].begin(), followSet[B].end(),
						   followSet[A].begin(),followSet[A].end(), 
						   inserter(followSet[B], followSet[B].begin()) );
					}

					set<string> diff;
					set_difference(followSet[B].begin(), followSet[B].end(),
                                          old.begin(), old.end(),
									   inserter(diff, diff.begin()) );
					if (diff.size() > 0) {
						changes = true;
					}
				} // end if	

				}
			} // end for-symbole
		} // end for-production

	} while (changes);
}


void GrammarAnalyzer::buildPredictSet() {
	// pre-condition: firstSet and followSets are populated
	bool lambda=false;
	for (Grammar::iterator p=productions.begin(); p!=productions.end(); ++p) {
		FullProduction fp = make_pair(p->first, p->second);
		Production prod = p->second;
        set<string> fs = firstSet[p->second[0]];

		if (fs.find("Lambda") != fs.end()) {
			set_union(fs.begin(), fs.end(), 
				  	  followSet[p->first].begin(), followSet[p->first].end(), 
				      inserter(fs, fs.begin()) );
			fs.erase("Lambda");
		}
		predictSet.insert(make_pair(fs, fp) );
		reversePredictSet.insert(make_pair(fp, fs) );
 	}
}
// -------------- end Asn6 stuff-----------------------

// ------------ start asn 7 -------------------------
void GrammarAnalyzer::fillParseTable() {

	// init
	vector<string>::iterator nti; 
	vector<string>::iterator ti;
	for (nti = nonterminals.begin(); nti != nonterminals.end(); nti++) {
		for (ti = terminals.begin(); ti != terminals.end(); ti++) {
			pair<string, string>  domain(*nti, *ti);	
			FullProduction empty;
			parseTable[domain] = empty;
		}	
	}

	Grammar::iterator p;
	Grammar::iterator pa=productions_w_actions.begin();
	for (p = productions.begin(); p != productions.end(); p++) {
		FullProduction fp = make_pair(p->first, p->second);
		FullProduction fpa = make_pair(p->first, pa->second);
		set<string> predictingTerms = reversePredictSet[fp];
		set<string>::iterator i;
		for (i = predictingTerms.begin(); i != predictingTerms.end(); i++) {
			//parseTable[make_pair(p->first, *i)] =  fp;	
			parseTable[make_pair(p->first, *i)] =  fpa;	
		}
		pa++;
	}
	
}

void GrammarAnalyzer::printParseTable_list() {
	vector<string>::iterator nti; 
	vector<string>::iterator ti;

	// table
	for (nti = nonterminals.begin(); nti != nonterminals.end(); nti++) {
		for (ti = terminals.begin(); ti != terminals.end(); ti++) {
			pair<string, string> key = make_pair(*nti, *ti);
			ParseTable::iterator i = parseTable.find(key);
			if (i!=parseTable.end()) {
				cout << "(" << i->first.first << ", " << i->first.second << ") --> " << i->second.first;
				for_each(i->second.second.begin(), i->second.second.end(), print_string(cout));
				cout << endl;
			}
			else {
				cout << "Error" << endl;
			}
		}
		cout << endl;
	}
}

void GrammarAnalyzer::printParseTable() {
	vector<string>::iterator nti; 
	vector<string>::iterator ti;
	int colsize = 15;
	// header
	cout << setw(colsize) << "rule number";
	for (ti = terminals.begin(); ti != terminals.end(); ti++) {
		cout << setw(colsize) << *ti;
	}
	cout << endl;

	// table
	for (nti = nonterminals.begin(); nti != nonterminals.end(); nti++) {
		cout << setw(colsize) << *nti + ":";
		for (ti = terminals.begin(); ti != terminals.end(); ti++) {
			pair<string, string> key = make_pair(*nti, *ti);
			ParseTable::iterator i = parseTable.find(key);
			if (i==parseTable.end()) {
				cout << "crap 1" << endl;
				exit(1);		
			}
			FullProduction fp = i->second;
			Reverse::iterator ri = reverse.find(fp);	
			if (ri != reverse.end()) {
				cout << setw(colsize) << ri->second;	
				//cout << "\"" << fp.first << "\" -->";
				//for_each(fp.second.begin(), fp.second.end(), print_quoted_string(cout));
			}
			else {
				cout << setw(colsize) << "x";	
			}
		}
		cout << endl;
	}
}
// ------------ end asn 7  -------------------------


void GrammarAnalyzer::print() {
	cout << "--left--" << endl;
	for_each(leftHandSides.begin(), leftHandSides.end(), print_string(cout));
	cout << endl;

	cout << "--right--" << endl;
	for_each(rightHandSides.begin(), rightHandSides.end(), print_string(cout));
	cout << endl;

	cout << "--terminals--" << endl;
	for_each(terminals.begin(), terminals.end(), print_string(cout));
	cout << endl;
	
	cout << "--nonterminals--" << endl;
	for_each(nonterminals.begin(), nonterminals.end(), print_string(cout));
	cout << endl << endl;

	cout << "--productions--" << endl;
	for (Grammar::iterator i = productions.begin(); i != productions.end(); i++) {
		cout << i->first << " --> ";
		for_each(i->second.begin(), i->second.end(), print_string(cout));
		cout << endl;
	}
	cout << "--productions with actions--" << endl;
	for (Grammar::iterator i = productions_w_actions.begin(); i != productions_w_actions.end(); i++) {
		cout << i->first << " --> ";
		for_each(i->second.begin(), i->second.end(), print_string(cout));
		cout << endl;
	}
}

void GrammarAnalyzer::print(int start, int end, string source) {
	cout << source << "    " << start << "  " << end << endl;
	for (int i=0; i<source.size(); i++) {
		if (i==start) cout << "+";
		else if (i==end) cout << "-";
		else cout << " ";
	}
	//cout << endl;
}

