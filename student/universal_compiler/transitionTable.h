// an ascii code to state mapping
// initialized from a text file that shows the more workable
// mappings from common characters and character classes:
//
// Letter Digit Blank + - = * / 

#ifndef TRANSISTION_TABLE_H
#define TRANSISTION_TABLE_H
#include <iostream>
#include <iomanip>
#include <fstream>
#include <string>
#include "scanner.h"

#include "unistd.h"
#include "stdlib.h"

using namespace std;

class TransitionTable {	
public:
	int debug;
	int debug1;
	int count;

	TransitionTable() {
		for (int r=0; r<100; r++) {
			for (int c=0; c<256; c++)  {
				T[r][c] = -1;
			}
			actions[r] = "";
		}
		debug=0;
		debug1=0;
		count=0;
	}

	void initFromFile(string filename) {
		std::ifstream inFile;
		inFile.open(filename.c_str());
		if (debug) cout << " opened file..." << endl;
		readNonTerminalStates(inFile);
		readTerminalStates(inFile);
	}

	int getNextState(int state, char c) {
		if (T[state][c]==-99) {
			cout << "bad state transistion. state: " << state << " character: " << c << endl;
			exit(1);
		}
		return T[state][c];
	}
	string getAction(int state) {
		return actions[state];
	}
	string getToken(int state) {
		return tokens[state];
	}
	
	void print2() {
		for (int i=0; i<100; i++) {
			printRow(i);
		}
	}
private:
	void readTerminalStates(std::ifstream &inFile) {
		if (debug) cout << "reading terminal states..." << endl;
		string action;
		int row_num;
		string tokenName;
		while (inFile && !inFile.eof() ) {
			inFile >> action;
			inFile >> row_num;
			inFile >> tokenName;
			if (row_num < 0)
				row_num = -row_num;
			actions[row_num] = action;
			tokens[row_num] = tokenName;
			if (debug) {
				cout << "-->" << row_num << " " << action << "," 
			 		 << tokenName << endl;
			}
		}
	}
		
	void readNonTerminalStates(std::ifstream &inFile) {
		if (debug) cout << "reading non-terminal states..." << endl;
		string action="";
		while (!inFile.eof() && (action != "TERMINALSTATES")) {
			count++;
			int row_num, n;
			inFile >> action;	
			inFile >> row_num; 
			if (debug1) cout << row_num  << " " << action << " ";
		
			if (row_num > -1) {
				actions[row_num] = action;

				inFile >> n; 
				if (debug1) cout << "read: " << n << endl;	
				setLetter(row_num, n);

				inFile >> n; 
				if (debug1) cout << "read: " << n << endl;	
				setDigit(row_num, n); 

				inFile >> n; 
				if (debug1) cout << "read: " << n << endl;	
				setWhiteSpace(row_num, n);

				inFile >> n; 
				if (debug1) cout << "read: " << n << endl;	
				setChar(row_num, '+', n);

				inFile >> n; 
				if (debug1) cout << "read: " << n << endl;	
				setChar(row_num, '-', n);

				inFile >> n; 
				if (debug1) cout << "read: " << n << endl;	
				setChar(row_num, '*', n);

				inFile >> n; 
				if (debug1) cout << "read: " << n << endl;	
				setChar(row_num, '/', n);

				inFile >> n; 
				if (debug1) cout << "read: " << n << endl;	
				setChar(row_num, '=', n);

				inFile >> n; 
				if (debug1) cout << "read: " << n << endl;	
				setChar(row_num, ':', n);

				inFile >> n; 
				if (debug1) cout << "read: " << n << endl;	
				setChar(row_num, ',', n);

				inFile >> n; 
				if (debug1) cout << "read: " << n << endl;	
				setChar(row_num, ';', n);

				inFile >> n; 
				if (debug1) cout << "read: " << n << endl;	
				setChar(row_num, '(', n);

				inFile >> n; 
				if (debug1) cout << "read: " << n << endl;	
				setChar(row_num, ')', n);

				inFile >> n; 
				if (debug1) cout << "read: " << n << endl;	
				setOther(row_num, n);

				inFile >> n; 
				if (debug1) cout << "read: " << n << endl;	
				setChar(row_num, '\n', n);
			}
			else if (row_num == -99) {
				// cment
				inFile.ignore(250, '\n');
			}
		}
	}

	
	void setLetter(int row, int nextState) {
		// 'A'..'Z' and 'a'..'z'
		// 65..90, 97..122
		for (int i=65; i<=90; i++) 
			T[row][i] = nextState; 
		for (int i=97; i<=122; i++) 
			T[row][i] = nextState;
		T[row]['_'] = nextState;
	}
	int checkDigit(int row, int nextState) {
		for (int i=65; i<=90; i++) 	
			if (T[row][i] != nextState) 
				return 0; 
		for (int i=97; i<=122; i++) 	
			if (T[row][i] != nextState) 
				return 0; 
		return 1;
	}
	void setDigit(int row, int nextState) {
		// '0'..'9'
		// 48..57
		for (int i=48; i<=57; i++) 
			T[row][i] = nextState; 
	}
	int checkLetter(int row, int nextState) {
		for (int i=48; i<=57; i++) 	
			if (T[row][i] != nextState) 
				return 0; 
		return 1;
	}
	void setWhiteSpace(int row, int nextState) {
		// space, tab, carriage return, newline
		T[row][' '] = nextState; 
		T[row]['\t'] = nextState; 
		T[row]['\n'] = nextState; 
		T[row]['\r'] = nextState;  ///???
		T[row]['\12'] = nextState; // lf
	}
	void setChar(int row, int ascii, int nextState) {
		T[row][ascii] = nextState;
	}
	void setOther(int row, int nextState) {}
	void printRow2(int r) {
		for (int c=0; c<255; c++)
			cout << setw(5) << T[r][c];
		cout << endl;
	}
	void printRow(int r) {
		cout << setw(5) << r;
		cout << setw(5) << actions[r];
		cout << setw(5) << T[r][65];
		cout << setw(5) << T[r][48];
		cout << setw(5) << T[r][' '];
		cout << setw(5) << T[r]['+'];
		cout << setw(5) << T[r]['-'];
		cout << setw(5) << T[r]['*'];
		cout << setw(5) << T[r]['/'];
		cout << setw(5) << T[r]['='];
		cout << setw(5) << T[r][':'];
		cout << setw(5) << T[r][','];
		cout << setw(5) << T[r][';'];
		cout << setw(5) << T[r]['('];
		cout << setw(5) << T[r][')'];
		cout << endl;
	}	
	void print() {
		for (int i=0; i<100; i++) {
			for (int j=32; j<=127; j++) {
				cout << setw(2) << T[i][j];	
			}
			cout << endl;
		}
	}
	int T[100][256];
	string actions[100];
	string tokens[100];
};
#endif
