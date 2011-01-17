
/* nodes in the linked list of symbols and related attributes */
#include "string.h"
#include "StringSpace.h"
#include <iostream>
using namespace std;

extern StringSpace ss;

class SymbolAttributes {
public:
  SymbolAttributes() : _next(NULL) {}
  SymbolAttributes(const char *str, int s) : _scope(s), _next(NULL) {
    _index = ss.enter(str);
  }
  ~SymbolAttributes() { cout << " WTF ~SymbolAttributes" << endl; }

  static void clear_scope(SymbolAttributes *&root, int scope) {
    SymbolAttributes *sap = root;
    while (sap != NULL && sap->_scope >= scope) {
      sap = sap->_next;
    }
    root = sap;
  }
  
  void delete_on_the_way_out() {
    if (_next == NULL)
      return;
    else
      _next->delete_on_the_way_out();
    delete _next;
  }

  static void insert(SymbolAttributes *&root, const char *s, int scope) {
	// insert at the beginning, MAY CHANGE ROOT, 
	SymbolAttributes *sap = new SymbolAttributes(s, scope);
        sap->_index = ss.enter(s);
	sap->_next = root;
	root = sap;
  }

  void dump() {
	char buffer[100];
	int l;
	char *s = ss.get(_index, l);
cout << "IN DUMP: " << l << endl;
	strncpy(buffer, s, l);
	buffer[l] = '\0';
	cout << "index: " << _index << " scope: " << _scope << " string: " << buffer << endl;
  }

  void recursive_dump() {
	dump();
	if (_next) {
	  _next->recursive_dump();
	}
  }

  bool find(const char *str, int scope, SymbolAttributes *&sap) {
        
    int l;
    char *my_str = ss.get(_index, l);
    if (strncmp(my_str,str, l) ==0 
        && scope >=_scope
        && strlen(str) == l) {
	  sap = this;
	  return true;
	}
	else if (_next) {
	  return _next->find(str, scope, sap);
	}
	else {
	  return false;
	}
  }

  int get_index() { return _index; }
  int get_scope() { return _scope; }

private:
  SymbolAttributes *_next;
  int _index; // into the single, global, string space
  int _scope;  
};
