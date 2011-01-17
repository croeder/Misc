#ifndef EOP_H
#define EOP_H
#include <sstream>
#include <vector>
#include <string>
#include "grammarAnalyzer.h" // for trim()
using namespace std;

class EndOfProduction {
public:
	static bool is_EOP(string &s) {
		return (s.find("EOP:") == 0);
	}

	EndOfProduction(int left, int right, int current, int top) :
		 _left(left), _right(right), _current(current), _top(top) {
	}

	EndOfProduction(string &s) {
		string t = GrammarAnalyzer::trim(s);
		vector<string> parts = GrammarAnalyzer::split(t, ":");

		istringstream ss(parts[1]);
		ss >> _left;
		istringstream ss1(parts[2]);
		ss1 >> _right;
		istringstream ss2(parts[3]);
		ss2 >> _current;
		istringstream ss3(parts[4]);
		ss3 >> _top;
	}

	string to_string() {
		stringstream s;
		s << "EOP:" << _left << ":" << _right << ":" << _current << ":" << _top << ":EOP";
		return s.str();		
	}

	int left()    { return _left; }
	int right()   { return _right; }
	int current() { return _current; }
	int top()     { return _top; }

private:
	int _left;
	int _right;
	int _current;
	int _top;
};

#endif
