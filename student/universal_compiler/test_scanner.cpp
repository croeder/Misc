#include "scanner.h"
#include <iostream>
#include <iomanip>
#include <string>
using namespace std;


int main(int argc, char **argv) {
	cout << "initializing r..." << endl;
	if (argc < 2) {
		cout << "need a file..." << endl;
		exit(1);
	}
  	Scanner r(argv[1]);
  	Token t;
  	while ((t = r.getNextToken()) != EofTok) {
    	cout << setw(10) << t << setw(10)<< r.decodeTokenType(t) << "  " << r.getTokenBuffer() 
         << endl;
		r.advance();
	}
}
