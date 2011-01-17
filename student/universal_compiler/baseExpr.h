#ifndef BASEEXPR_H
#define BASEEXPR_H
#include <string>
/* 
 * part of work for CSC 5815 Universal Compiler: Theory and Construction, Boris Stilman
 * Fall 2005
 * by  Chris Roeder August-November 2005
 */


enum ExpressionKind { IdExpr, LiteralExpr, TempExpr };

class BaseExpression {
public:
	BaseExpression(ExpressionKind k) : kind(k) {}
	virtual ExpressionKind getKind() const { return kind; }
	virtual std::string extract()const =0;
private:
	ExpressionKind kind;
};
#endif
