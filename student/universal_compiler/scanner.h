#ifndef SCANNER_H
#define SCANNER_H
#include <iostream>
#include <fstream>
#include <string>
using namespace std;
#include "transitionTable.h"
/* 
 * part of work for CSC 5815 Universal Compiler: Theory and Construction, Boris Stilman
 * Fall 2005
 * by  Chris Roeder August-November 2005
 */


enum Token {
  BeginTok, EndTok, ReadTok, WriteTok, IdTok, IntTok, LParenTok, RParenTok, SemicolonTok, CommaTok,
  AssignOpTok, PlusOpTok, MinusOpTok, EofTok, EqOpTok,
  MultOpTok, DivOpTok, PowerOpTok, WhiteSpaceTok, CommentTok };

const  std::string TokenStrings[] = {  "Begin", "End", "Read", "Write", "Id", "Int", "LParen", "RParen", "Semicolon", "Comma",
  "AssignOp", "PlusOp", "MinusOp", "Eof", "EqOp", "MultOp", "DivOp", "PowerOp", "WhiteSpace", "Comment" };


class Scanner {
public:
  Scanner(const std::string &filename);
  ~Scanner();
  Token getNextToken();
  std::string decodeTokenType(Token);
  void advance();
  const std::string& getTokenBuffer() const;
  static Token getTokenFromString(const string &s);
private:
  Token parseNextToken();
  std::string tokenBuffer;
  std::ifstream inFile;
  Token checkReserved();
  void lexicalError(char);
  Token currentToken;
  int debug_switch;
  TransitionTable t;
};
#endif
