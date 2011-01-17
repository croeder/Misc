#ifndef SYMBOL_TABLE_H
#define SYMBOL_TABLE_H

#include <stdio.h>
using namespace std;

#include "SymbolAttributes.h"

// SymbolTable.h
// A hand-coded symbol table using space-saving, brain-frying
// techniques like String Spaces instead of template-crazy
// structures built from <map> and <vector>.
//
// see also test_SymbolTable.cpp


class SymbolTable {
 public:
  SymbolTable() {
    for (int i=0; i< HASH_SIZE; i++) {
	table[i] = NULL;
    }
  }

  ~SymbolTable();
  bool enter(const char * symbol, int scope);
  bool find(const char * symbol, int scope, SymbolAttributes *&sap);
  int find_scope(const char *symbol, SymbolAttributes *&sap, int max_scope);
  void clear_scope(int i);
  void dump();
 private:
	static const int HASH_SIZE;
	int hash(const char *);
	//SymbolAttributes *table[HASH_SIZE];//WTF
	SymbolAttributes *table[7];
	StringSpace ss;
};
#endif
