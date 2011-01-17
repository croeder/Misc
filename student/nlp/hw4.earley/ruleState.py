#!/usr/bin/python

import re

#
# line = head --> part1 part2 part3, semantic hoo-ha as yet undefined
#
# Convention in grammar is that POS values are Mixed Case.
# Higher level non-terminals are UPPER CASE.
#

class RuleState():
	def parse_RuleState(cls, line):
		head=""
		symbols=[]	
		
		(junk, semantics) = line.split(",")				
		(head, symbols_string) = junk.split("-->")
		symbols = symbols_string.split(" ")

		# dot_position: the dot comes after this index
		dot_position=-1

		start_token_range=0
		end_token_range=0

		return RuleState(head, symbols, dot_position, start_token_range, end_token_range, semantics)
	parse_RuleState = classmethod(parse_RuleState)

	def __init__(self, head, symbols, dot_pos, start, end, semantics):
		self.head = head
		self.symbols = symbols
		self.dot_position = dot_pos
		self.start_token_range = start
		self.end_token_range = end
		self.semantics = semantics

	def __cmp__(self, rhs):
		# returns True if the ARE different!!
		if self is rhs:
			return False
		if self is None and rhs is None:
			return False
		if self is not None and rhs is None:
			return True
		if  self.head != rhs.head or self.dot_position != rhs.dot_position and self.start_token_range != rhs.start_token_range and self.end_token_range != rhs.end_token_range :
			return True
		if len(self.symbols) != len(rhs.symbols ) :
			return True
		i=0
		while i<len(self.symbols):
			if self.symbols[i]!=rhs.symbols[i]:
				return True
			i+=1
		return False

	def advance(self, end):
		self.dot_position += 1
		self.end_token_range = end

	def next_cat(self):
		next_symbol = self.get_next_symbol()
		if next_symbol is not None:
			if (self.incomplete()):
				if re.match("^[A-Z]+$", self.get_next_symbol()):
					return "not POS"
				else:
					return "POS"
		return None

	def get_next_symbol(self):
		if (self.incomplete() and len(self.symbols) > self.dot_position + 1):
			return self.symbols[self.dot_position + 1]
		else:
			return None

	def get_head(self):
		return self.head;

	def get_semantics(self):
		return self.semantics
	
	def incomplete(self):
		# a rule is complete if the dot_position is past/at the last symbol
		# the last symbole is at position len(s) -1
		# so if len = pos + 1, it's complete, or len - 1 = pos
		# then if  pos < len -1 its incomplete, or pos + 1 < len
		return (self.dot_position + 1) < len(self.symbols)
		#return len(self.symbols)  != (self.dot_position )
		#return len(self.symbols) != (self.dot_position + 1)
		#return len(self.symbols) <= (self.dot_position + 1)

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
			symbol_string += self.symbols[i] + " "
			if (self.dot_position == i) :
				symbol_string += " * "
			i = i + 1
		if (self.dot_position == i):
			symbol_string += " * "

		return symbol_string

	def print_rule(self):	
		print self.make_rule_string()

	def printme(self):
		symbol_string = self.pad_string(self.make_rule_string(), 30)

		range_string =  "[" + str(self.start_token_range) +  "," +  str(self.end_token_range) +  "]"
		print self.pad_string(symbol_string, 30) +  self.pad_string(range_string, 10) + self.semantics
		
		#print " incomplete?", self.incomplete(),
		#print " next_cat:", self.next_cat(),
		#print " len(s):", len(self.symbols),
		#print " dot pos:", self.dot_position  

	def set_dot(self, i):
		if (i < len(self.symbols)):
			self.dot_position = i

	def Test(self):
		r = RuleState("a-->b c,x")
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
		
		r = RuleState("a-->FOO BAR,x")
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
		
		r = RuleState("a-->b c,x")
		rr = RuleState("a-->b c d,x")
		rrr = RuleState("a-->b c e,x")
		q = RuleState("a-->b c,x")
		s = RuleState("x-->y z,x")
		if r == q :
			print "same"
		else :
			print "different x"
		if r == rr :
			print "same x"
		else :
			print "different "
		if rr == rrr :
			print "same x"
		else :
			print "different "
		if r == r :
			print "same"
		else :
			print "different x"
		
		if r == s:
			print "same x"
		else :
			print "different "

