#ifndef SEMANTIC_RECORD_H 
#define SEMANTIC_RECORD_H
#include <iostream>
#include <sstream>
using namespace std;
class SemanticRecord {
public:
	SemanticRecord() :_name(""), _value(0), _type('i'), _scope(1){}
	SemanticRecord(string n, int v, char t) :_name(n), _value(v), _type(t), _scope(1) {}
	//string name() const { return _name; }
	//string name() const { return get_scoped_name(); }
	string name() const { return _name; }
	char type() const { return _type; }
	int extract() const { return _value; } 
	void setName(const string &s) { _name = s; }
	void print() { cout << "SemanticRecord: -name:" << _name << " -value:" << _value << " type:" << _type << endl; }
	int get_scope() { return _scope; }
	void set_scope(int s) { _scope = s; }
	string get_scoped_name() const { 
	  ostringstream ss;
	  ss << _name << "_" << _scope;
	  return ss.str();
	}
private:
	string _name;
	int _value;
	char _type; // s -string, i -integer
	int _scope;
};
typedef pair<string, SemanticRecord> SemanticStackEntry;

#endif
