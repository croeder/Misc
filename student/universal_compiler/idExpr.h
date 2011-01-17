#ifndef IDEXPR_H
#define  IDEXPR_H
/* 
 * part of work for CSC 5815 Universal Compiler: Theory and Construction, Boris Stilman
 * Fall 2005
 * by  Chris Roeder August-November 2005
 */

class IdExpression : public BaseExpression {
public:
	IdExpression() : BaseExpression(IdExpr) {}
	IdExpression(std::string n) : BaseExpression(IdExpr), name(n) {}
	std::string getName() const { return name; }
	std::string extract() const { return name; }
private:
	std::string name;
};
#endif
