#!/usr/bin/python
#
# trie.py, 
# A trie. (Not optimised for character space.)
#
# Chris Roeder, 2010-02-01, Python 2.6

import node

substringsFilename = "substrings.txt"
wordsFilename = "top100k.txt"
#wordsFilename = "top10k.txt"
#wordsFilename = "top1k.txt"
#wordsFilename = "top100.txt"
#wordsFilename = "top20.txt"
#wordsFilename = "test.txt"

class trie:
	def __init__(self):
		self.root=None

	def printme(self):
		self.root.printme("  ")

	def insert_file(self,filename):
		count=0
		f = open(filename)
		self.root = node.node('0')
		for line in f:
			pointer = self.root
			line = line[:-1]
			charcount=0
			for c in line:
				charcount+=1
				pointer = pointer.add(c,(charcount == len(line)))
	
	def is_word(self, word):
		pointer=self.root	
		for c in word:
			pointer = pointer.get(c)
			if not pointer:
				return 0
		return pointer.is_end == 1

	def is_part_of_word(self, word):
		pointer=self.root	
		for c in word:
			pointer = pointer.get(c)
			if not pointer:
				return 0
		return 1

	def return_matching_substrings(self,word):
		matches=[]
		building_word=""
		pointer=self.root	

		for c in word:
			print c
			building_word += c
			pointer = pointer.get(c)
			if not pointer:
				return matches
			if pointer.is_end:
				matches.append(building_word)
	
		return matches

	:q




def test_substrings():
	t=trie()
	t.insert_file(substringsFilename)
	t.printme()
	print "-------------"
	my_matches = t.return_matching_substrings("gustavos")
	print "-------------"
	for m in my_matches:
		print m	

def test():
	t = trie()
	t.insert_file(wordsFilename)
	t.printme()

	if (t.is_word("the")):
		print "the is a word"

	if (t.is_word("xxx")):
		print "the is a word"
	else:
		print "xxx is not a word"


	if (t.is_part_of_word("their")):
		print "their is a word"
	else:
		print "their is not a word"

	if (t.is_part_of_word("thei")):
		print "thei is a word"
	else:
		print "thei is not a word"

	if (t.is_part_of_word("they")):
		print "they is a word"
	else:
		print "they is not a word"

	if (t.is_part_of_word("th")):
		print "th is a word"
	else:
		print "th is not a word"

	if (t.is_part_of_word("t")):
		print "t is a word"
	else:
		print "th is not a word"


##test()	
test_substrings();
