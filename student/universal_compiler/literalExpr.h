#ifndef LITERALEXPR_H
#define LITERALEXPR_H

#include <string>
#include <sstream>
/* 
 * part of work for CSC 5815 Universal Compiler: Theory and Construction, Boris Stilman
 * Fall 2005
 * by  Chris Roeder August-November 2005
 */

using namespace std;

class LiteralExpression : public BaseExpression {
public:
	LiteralExpression(string s) : BaseExpression(LiteralExpr) {
		value = s;
	}
	string extract() const { 
		return value;
	}
private:
	LiteralExpression() : BaseExpression(LiteralExpr) {}
	string value;
};
#endif
