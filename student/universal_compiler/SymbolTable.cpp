#include "SymbolTable.h"
using namespace std;

const int  SymbolTable::HASH_SIZE=7;
SymbolTable::~SymbolTable() {
  for (int i=0; i<HASH_SIZE; i++) {
    if (table[i]) {
      table[i]->delete_on_the_way_out();
    }
  }
}

bool SymbolTable::enter(const char * symbol, int scope) {
  int h = hash(symbol);
  cout << "SymbolTable::enter(): hash value=" << h << " symbol: " << symbol;
  if (table[h] != NULL) {
    cout << " ...adding on" << endl;
    SymbolAttributes::insert(table[h], symbol, scope);
  }
  else {
    cout << " ...starting new" << endl;
    SymbolAttributes *sap = new SymbolAttributes(symbol, scope);
    table[h] = sap;
  }
}

bool SymbolTable::find(const char * symbol, int scope, SymbolAttributes *&sap) {
  int h = hash(symbol);
  if (table[h] != NULL) {
    cout << "SymbolTable::finding " << symbol << " in scope: " << scope << endl;
    return table[h]->find(symbol, scope, sap);
  }
  return false;
}

int SymbolTable::find_scope(const char *symbol, SymbolAttributes *&sap, int max_scope) {
  for (int i=1; i<=max_scope; i++) {
    if (find(symbol, i, sap)) {
      return i;
    }
  }

  return -1;
}

void SymbolTable::clear_scope(int s) {
  for (int i=1; i<=HASH_SIZE; i++) {
    SymbolAttributes::clear_scope(table[i], s);
  }
}



void SymbolTable::dump() {
  for (int i=0; i< HASH_SIZE; i++) {
    if (table[i] != NULL) {
      table[i]->dump();
    }
  }
}

int SymbolTable::hash(const char *s) {
	int sum=0;
	for (int i=0; i<strlen(s); i++) {
		sum += (int) s[i];	
	}	
	sum = sum % HASH_SIZE;
}
