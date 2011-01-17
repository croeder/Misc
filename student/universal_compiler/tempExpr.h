#ifndef TEMPEXPR_H 
#define TEMPEXPR_H
#include "baseExpr.h"
#include <string>
#include <iostream>
#include <sstream>
using namespace std;
/* 
 * part of work for CSC 5815 Universal Compiler: Theory and Construction, Boris Stilman
 * Fall 2005
 * by  Chris Roeder August-November 2005
 */


class TempExpression : public BaseExpression {
public:
	TempExpression() : BaseExpression(TempExpr) {
		stringstream s;
		s << "temp";
		s <<  tempCount++;
		name = s.str();
	}

	TempExpression(std::string s) : BaseExpression(TempExpr) {
		name = s ;
	}
	std::string extract() const { 
		return name; 
	}
private:
	std::string name;
	static int tempCount;
};
#endif
