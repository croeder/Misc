#include <string>
#include <iostream>
#include <fstream>
//#include "transitionTable.h"
#include "scanner.h"

#include "unistd.h"
#include "stdlib.h"

using namespace std;
/* 
 * part of work for CSC 5815 Universal Compiler: Theory and Construction, Boris Stilman
 * Fall 2005
 * by  Chris Roeder August-November 2005
 */



  Scanner::Scanner(const string &filename) {
	string scanner_def_file = "table.txt"; 
    t.initFromFile(scanner_def_file);
    currentToken = (Token)-1;
    inFile.open(filename.c_str()); 
    currentToken = (Token)-1;
    debug_switch=0;
  }

  Token Scanner::getNextToken() {
    if ((int)currentToken == -1)	{
      currentToken = parseNextToken();
    }
    if (debug_switch) {
		cout << "Token: current:" <<  currentToken << " decode:" << decodeTokenType(currentToken) << " buffer:" << tokenBuffer << endl;
    }
    return currentToken;
  }

  void Scanner::advance() {
    currentToken = (Token) -1;
  }

  Token Scanner::parseNextToken() {
	// uses iostream's putback fun. instead of a peek/consume pair

	tokenBuffer = "";
    currentToken = (Token) -1;
    char c;
	string action;
	int state=0;
    if (inFile.eof())
      return EofTok;
  
    while (!inFile.eof()) { 
      inFile.get(c); 
	  state = t.getNextState(state, c);
	  action = t.getAction(state);
	  if (action == "ma") { // Move Append
	  	/////state = t.getNextState(state, c);
		tokenBuffer = tokenBuffer + c;
	  }
	  else if (action == "mna") { // Move No Append
	  	//state = t.getNextState(state, c);
	  }
	  else if (action == "ha") { // Halt Append
		tokenBuffer = tokenBuffer + c;
		Token tok = getTokenFromString(t.getToken(state));
		if (debug_switch) cout << "scanner halting with \"" << tokenBuffer << "\"" <<endl;
		if (tok == IdTok)
			return  checkReserved();
		else
			return tok;
	  }
	  else if (action == "hr") { // Halt Reuse
		inFile.putback(c);
		Token tok = getTokenFromString(t.getToken(state));
		if (debug_switch) cout << "scanner halting with " << tokenBuffer << endl;
		if (tok == IdTok)
			return  checkReserved();
		else
			return tok;
	  }
	  else if (action == "hna")  { // Halt No Append
		Token tok = getTokenFromString(t.getToken(state));
		if (debug_switch) cout << "scanner halting with " << tokenBuffer << endl;
		if (tok == IdTok)
			return  checkReserved();
		else
			return tok;
	  }
	  else if (action == "x") { // Error
		cout << "error in scanner:" << state << " " << (int) c << " \"" 
	         << (char) c << "\" " <<  tokenBuffer << endl;
		t.print2();
		exit(1);
	  }
	}
    return EofTok;
	//return (Token) -999;
  }

  Token Scanner::checkReserved() {
    if (tokenBuffer == "BEGIN" || tokenBuffer == "Begin") return BeginTok;
    if (tokenBuffer == "END"   || tokenBuffer == "End") return EndTok;
    if (tokenBuffer == "READ"  || tokenBuffer == "Read") return ReadTok;
    if (tokenBuffer == "WRITE" || tokenBuffer == "Write") return WriteTok;
    return IdTok;
  }

  void Scanner::lexicalError(char c) {
    cout << endl << "LEXICAL ERROR at char:\"" << c << "\"" << endl;
  }

  const string& Scanner::getTokenBuffer() const {
    return tokenBuffer;
  }

  Scanner::~Scanner() {
    inFile.close();
  }
  string Scanner::decodeTokenType(Token t) {
    if ((int) t >= 0)
      return TokenStrings[t];
    else
      return "NULL TOKEN";
  }

  Token Scanner::getTokenFromString(const string &s) {
	for (int i=0; i<sizeof(TokenStrings)/4; i++) {
		if (TokenStrings[i] == s) {
			return static_cast<Token>(i);
		}
	}	
	return static_cast<Token>(0);
  }
