#!/usr/bin/python

# python lambda's are not full-blow functions or blocks, just expressions!
# (not even a statement)

class Booger:
	def boog(cls):
		print "Booger.boog"
	classmethod(boog)

# Ex.1 Function version

def f1(x,y,z):
	return x + y + z

print "function ", f1(2,3,4)
print "function no argsl", f1

# Ex. 2 Lambda version
f2 = lambda x,y,z: x + y + z
print "lambda ", f2(2,3,4)
print "lambda no args", f2


# Ex. 3 Lambda version
print "lambda ", (lambda x,y,z: x + y + z) (2,3,4)


# String/Digit: make an integer out of a String
symbol="3"
Digit_sem = lambda x: int(x)
print "digit:", Digit_sem(symbol) + 0

