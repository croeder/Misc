#!/usr/bin/python

import re
import copy

#
# line = head --> part1 part2 part3, semantic hoo-ha as yet undefined
#
# Convention in grammar is that POS values are Mixed Case.
# Higher level non-terminals are UPPER CASE.
#

class RuleState():
	def __init__(self, head, symbols, dot_pos, start, end, semantics, semantics_txt):
		self.head = copy.copy(head)
		self.symbols = copy.copy(symbols)
		self.dot_position = copy.copy(dot_pos)
		self.start_token_range = copy.copy(start)
		self.end_token_range = copy.copy(end)
		self.semantics = copy.copy(semantics)
		self.semantics_txt = copy.copy(semantics_txt)
		self.rules=[] # children rules

	def parse_RuleState(cls, line):
		head=""
		symbols_string=""
		symbols=[]	
		semantics= None
		semantics_txt= None
		
		try:	
			(head, symbols_string,semantics_txt) = line.split("-->")
			symbols = symbols_string.split(" ")
		except ValueError:
			print "ERROR caught:", line

		# dot_position: the dot comes after this index
		dot_position=-1

		start_token_range=0
		end_token_range=0

		try:
			semantics = eval(semantics_txt)
		except:
			print "ERROR: no semantics: ", semantics_txt, "<---------------------------ERROR"

		return RuleState(head, symbols, dot_position, start_token_range, end_token_range, semantics, semantics_txt)
	parse_RuleState = classmethod(parse_RuleState)

	def print_rule_tree(self):
		self.print_rule_tree_impl("    ", "")

	def print_rule_tree_impl(self, indent_incr, indent):
		i=0
		for r in self.rules:
			r.print_rule_tree_impl(indent_incr, indent + indent_incr)	
		print indent, self.head,
		if re.match("^[A-Z]+$", self.head):
			# Non-Terminal
			if (self.semantics == None):
				print "non-terminal: 1 ", self.semantics , self.semantics_txt
			else:
				print "non-terminal 2: ",  self.semantics(self.rules) , self.semantics_txt
		else:
			# TERMINAL
			print "terminal: ", self.semantics(self.symbols[0])

	def debug(self, message):
		if (False):
			print "ddddddd:  " , message	

	def __eq__(self, rhs):
		if self is rhs:
			self.debug(" same pointers")
			return True
		if self is None and rhs is None:
			self.debug("  same None")
			return True

		if self is not None and rhs is None:
			self.debug(" different None")
			return False
		if  self.head != rhs.head :
			self.debug(" head")
			return False
		if self.dot_position != rhs.dot_position :
			self.debug(" dot pos")
			return False
		if self.start_token_range != rhs.start_token_range :
			self.debug(" start range")
			return False
		if self.end_token_range != rhs.end_token_range :
			self.debug(" end range")
			return False
		if len(self.symbols) != len(rhs.symbols ) :
			self.debug(" num symbols is different: " + str(self.symbols) + " " + str(rhs.symbols)  )
			return False
		if self.semantics != rhs.semantics :
			self.debug(" end range")
			return False
		i=0
		while i<len(self.symbols):
			if self.symbols[i]!=rhs.symbols[i]:
				self.debug(" this symbol is different" + str(i) + str(self.symbols[i]) +  str(rhs.symbols[i]) )
				return False
			i+=1

		self.debug(" __eq__ default true" )
		return True

	def advance(self, end):
		self.dot_position += 1
		self.end_token_range = end

	# returns next non-terminal category: is POS or not?
	def next_cat(self):
		next_symbol = self.get_next_symbol()
		if next_symbol is not None:
			if (self.incomplete()):
				if re.match("^[A-Z]+$", next_symbol):
					return "not POS"
				else:
					return "POS"
		return None

	def get_next_symbol(self):
		# returns the symbol to the right of the dot
		if (self.incomplete() and len(self.symbols) > self.dot_position + 1):
			return self.symbols[self.dot_position + 1]
		else:
			return None

	def get_head(self):
		return self.head;

	def get_semantics(self):
		return self.semantics
	
	def get_semantics_txt(self):
		return self.semantics_txt
	
	def incomplete(self):
		# a rule is complete if the dot_position is past/at the last symbol
		# the last symbole is at position len(s) -1
		# so if len = pos + 1, it's complete, or len - 1 = pos
		# then if  pos < len -1 its incomplete, or pos + 1 < len
		return (self.dot_position + 1) < len(self.symbols)

	def pad_string(self, string, length):
		more  = 0
		if string is None:
			more = length
		else:	
			more = length - len(string)
		padding = ""
		i=0
		while i<more:
			i+=1
			padding += " "

		if string is None:
			return padding
		else:
			return string + padding

	def make_rule_string(self):
		symbol_string = self.pad_string(self.head, 7) +  "-->"

		if (self.dot_position == -1):
			symbol_string += " * "
		i=0
		while (i < len(self.symbols)):
			symbol_string += "\"" + self.symbols[i] + "\" "
			if (self.dot_position == i) :
				symbol_string += " * "
			i = i + 1
		if (self.dot_position == i):
			symbol_string += " * "

		return symbol_string

	def make_range_string(self):
		range_string =  "[" + str(self.start_token_range) +  "," +  str(self.end_token_range) +  "]"
		return range_string

	def print_rule(self):	
		print self.make_rule_string()


	# "print" here is an adjective, not a verb
	# this function returns a nested list representation of 
	# of the tree of rules...suitable for printing
	def print_tree(self):
		retval=[]
		if re.match("^[A-Z]+$", self.head):
			# TERMINALS
			retval.append(self.head)
		else:
			# NON-TERMINALS
			retval.append(self.head)
			retval.append(self.symbols[0])
		for r in self.rules:
			retval.append(r.print_tree())	
		return retval


	def print_rules(self):
		# returns a list of string representations of self and children
		retval = [self.make_rule_string()]
		for r in self.rules:
			retval.append(r.print_rules())	
		return retval

	def print_rules_old(self):
		# returns a list of string representations of self and children
		children_rules=[]
		for r in self.rules:
			children_rules.append(r.print_rules_2())	
		retval = [self.make_rule_string(), children_rules]
		return retval

	def printme(self):
			self.printme_pad("")

	def printme_pad(self, pad):
		symbol_string = self.make_rule_string()
		range_string =  self.make_range_string()

		print pad + self.pad_string(symbol_string, 30) +  self.pad_string(range_string, 10) + self.semantics 

	def set_dot(self, i):
		if (i < len(self.symbols)):
			self.dot_position = i

	def Test(self):
		self.test_parser()
		RuleState.test_ctor()

	def test_ctor(cls):
		## def __init__(self, head, symbols, dot_pos, start, end, semantics):
		print "test_ctor"

		q = RuleState.parse_RuleState("A-->b c,x")
		r = RuleState( "A", ["b", "c"], -1, 0, 0, "x")
		RuleState.test_same(q,r)


		r1 = RuleState( "A", ["a", "b", "c"], 0, 0, 1, "x")
		r2 = RuleState( "A", ["a", "b", "c"], 0, 0, 1, "x")
		RuleState.test_same(r1,r2)

		s = RuleState( "A", ["a", "b", "c"], 1, 0, 1, "x")
		RuleState.test_diff(s,r2)
		 
		t = RuleState( "A", ["a", "b", "c"], 0, 1, 1, "x")
		RuleState.test_diff(t,r2)
		 
		u = RuleState( "A", ["a", "b", "c"], 0, 0, 2, "x")
		RuleState.test_diff(u,r2)
		
		v = RuleState( "A", ["a", "b", "c"], 0, 0, 1, "y")
		RuleState.test_diff(v,r2)
		 

	test_ctor = classmethod(test_ctor)

	def test_same(cls, a, b):
		if a == b :
			print "same"
		else :
			print "-->different x"
			a.printme()
			b.printme()
			print "<--"

	test_same = classmethod(test_same)

	def test_diff(cls, a, b):
		if a == b :
			print "-->same  x"
			a.printme()
			b.printme()
			print "<--"
		else :
			print "different "
	test_diff = classmethod(test_diff)

	def test_parser(self):
		print "test_parser"
		r = RuleState.parse_RuleState("a-->b c,x")
		r.printme()
		print "incomplete", r.incomplete() # true
		print "next", r.next_cat() # a
		r.set_dot(0)
		r.printme()
		print "incomplete", r.incomplete() # true
		print "next", r.next_cat() # b
		r.set_dot(1)
		r.printme()
		print "incomplete", r.incomplete() # false
		print "next", r.next_cat() # None
		
		r = RuleState.parse_RuleState("a-->FOO BAR,x")
		r.printme()
		print "incomplete", r.incomplete() # true
		print "next", r.next_cat() # a
		r.set_dot(0)
		r.printme()
		print "incomplete", r.incomplete() # true
		print "next", r.next_cat() # b
		r.set_dot(1)
		r.printme()
		print "incomplete", r.incomplete() # false
		print "next", r.next_cat() # None
		
		q = RuleState.parse_RuleState("a-->b c,x")
		r = RuleState.parse_RuleState("a-->b c,x")

		print
		if r == q :
			print "same ?"
		else :
			print "different x 1"
		print



		rr = RuleState.parse_RuleState("a-->b c d,x")
		rrr = RuleState.parse_RuleState("a-->b c e,x")
		s = RuleState.parse_RuleState("x-->y z,x")


		if r == r :
			print "same"
		else :
			print "different x"
		print

		if r == q :
			print "same"
		else :
			print "different x 2"
		print

		if r == rr :
			print "same x" 
		else : 
			print "different "
		print

		if rr == rrr :
			print "same x"
		else :
			print "different "
		print

		if r == r :
			print "same"
		else :
			print "different x"
		print
		
		if r == s:
			print "same x"
		else :
			print "different "
		print

