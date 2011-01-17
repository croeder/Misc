
#include "SymbolTable.h"
#include <iostream>
using namespace std;

	StringSpace ss;

int main(int argc, char*argv[]) {

  SymbolAttributes *sa;
  int i;
  /*
   begin
       int a;
       begin
           int a,b;
           begin
               int a, b,c;
           end
       end
  end 
  */

  SymbolTable st;
  st.enter("a", 1);
  st.enter("a", 2);
  st.enter("b", 2);
  st.enter("a", 3);
  st.enter("b", 3);
  st.enter("c", 3);

  cout << "done entering.." << endl;

  cout << "find x,0" << endl;
  if (st.find("x", 0, sa)) {
    cout << "error x, 0" << endl;
  }
  cout << "find x,1" << endl;
  if (st.find("x", 1, sa)) {
    cout << "error x, 1" << endl;
  }
  cout << "find x,2" << endl;
  if (st.find("x", 2, sa)) {
    cout << "error x, 2" << endl;
  }
  cout << "find x,3" << endl;
  if (st.find("x", 3, sa)) {
    cout << "error x, 3" << endl;
  }
  cout << "find x,4" << endl;
  if (st.find("x", 4, sa)) {
    cout << "error x, 4" << endl;
  }

  cout << "find a" << endl;
  if (!st.find("a", 1, sa)) {
    cout << "errror a, 1" << endl;
  }
  if (!st.find("a", 2, sa)) {
    cout << "errror a, 2" << endl;
  }
  if (!st.find("a", 3, sa)) {
    cout << "errror a, 3" << endl;
  }

  cout << "find b" << endl;
  if (st.find("b", 1, sa)) {
    cout << "errror b, 2" << endl;
  }
  if (!st.find("b", 2, sa)) {
    cout << "errror b, 2" << endl;
  }
  if (!st.find("b", 3, sa)) {
    cout << "errror b, 3" << endl;
  }


  cout << "find c" << endl;
  if (st.find("c", 1, sa)) {
    cout << "errror c, 1" << endl;
  }
  if (st.find("c", 2, sa)) {
    cout << "errror c, 2" << endl;
  }
  if (!st.find("c", 3, sa)) {
    cout << "errror c, 3" << endl;
  }

}
