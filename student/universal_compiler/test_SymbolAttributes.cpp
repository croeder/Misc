#include "SymbolAttributes.h"
#include "StringSpace.h"

StringSpace ss;

int main(int argc, char *argv[]) {
  SymbolAttributes *root;

  char *tests[] = {
    "root", "two", "three", "four" };

  root = new SymbolAttributes("root", 1);
  SymbolAttributes::insert(root, "two", 1);
  SymbolAttributes::insert(root, "three", 1);
  SymbolAttributes::insert(root, "four", 2);
  SymbolAttributes::insert(root, "four", 1);
  SymbolAttributes::insert(root, "five", 1);

  SymbolAttributes *search;
  for (int x=0; x<5; x++) {
    if (root->find(tests[x], 1, search)) {
      cout << "FOUND x:" << x << "  " << tests[x] <<  endl;
      //      search->dump();
    }
    else {
      cout << "NOT found x:" << x << "  " << tests[x] <<  endl;
    }
  }


  cout << "dumping...."<<endl;


  cout << " ------------" << endl;
  root->recursive_dump();
  cout << " ------------" << endl;
  ss.dump2();


  cout << "---------------------------------" << endl;
  delete root;
  root = new SymbolAttributes("root", 1);
  SymbolAttributes::insert(root, "two", 1);
  SymbolAttributes::insert(root, "three", 2);
  SymbolAttributes::insert(root, "four", 3);
  SymbolAttributes::insert(root, "four", 4);
  SymbolAttributes::insert(root, "five", 5);
  root->recursive_dump();
  cout << "---------------------------------" << endl;
  SymbolAttributes::clear_scope(root, 3);
  root->recursive_dump();
  

}
