#!/usr/bin/python
#
# node.py implements a node used in trie.py.
# 
# Chris Roeder, 2010-02-01, Python 2.6


def dumplist(foo):
	print
	word=""
	for c in foo:
		word = word + c
	print word
	print

class node:
	def __init__(self, letter):
		self.letter = letter
		self.children = {}
		self.is_end = 0

	def add(self,letter, is_end):
		if (not self.children.has_key(letter)):
			self.children[letter] = node(letter)
		self.children[letter].is_end=is_end or self.children[letter].is_end
		return self.children[letter] 

	def printme(self,indent):
		if (self.is_end):
			print indent + self.letter + "*"
		else:
			print indent + self.letter
		indent = indent + "  "
		for child_key in self.children:
			self.children[child_key].printme(indent)

	def get(self, char):
		if self.children.has_key(char):
			return self.children[char]
		else:
			return None

def test():
	chars = ('a', 'b', 'c', 'd')
	root = node('0')
	pointer = root
	for c in chars:
		print "adding " + c
		n = node(c)
		pointer.add(n)
		pointer = n
	
	root.printme("  ")	
	
	
	pointer = root
	pointer = pointer.get('a')
	n = node('r')
	pointer.add(n)
	pointer = n
	
	n = node('c')
	pointer.add(n)
	pointer = n
	
	n = node('h')
	pointer.add(n)
	pointer = n
	save = n
	save.printme("                 ")
	
	n = node('e')
	pointer.add(n)
	pointer = n
	
	
	root.printme("-")
	
	
	n = node('r')
	pointer.add(n)
	pointer = n
	
	root.printme("+")
	
	n = node('I')
	save.add(n)
	save = n
	
	root.printme("?")
	n = node('E')
	save.add(n)
	
	root.printme(" ")	


###test()	
