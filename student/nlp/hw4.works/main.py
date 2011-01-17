#!/usr/bin/python


from ruleState import RuleState
from early import Early
import sys
import re

def read_file(filename):
	F = open(filename)
	rules_string = ""
	line = "bs"
	while (F and line != ""):
		line  = F.readline().rstrip()
		if (len(line) > 0 and line[0] != "#") :
			rules_string = rules_string + line
	F.close
	return rules_string

# my code expects input in the form of a list I can "eval"
# this converts the raw form from Dr. Martin
# in: 'a b a' out: '("a","b","a")'
def read_txt_file(filename):
	F = open(filename)
	rules_string = "("
	line = "bs"
	while (F and line != ""):
		line  = F.readline().rstrip()
		words = line.split(" ");
		prettyLine="\""
		for w in words:
			prettyLine = prettyLine + w + " " 
		prettyLine = prettyLine[:-1] + "\""
		if (len(prettyLine) > 0 and prettyLine[0] != "#") :
			rules_string = rules_string + prettyLine + ","

	rules_string = rules_string[:-1] + ")"
	F.close
	return rules_string




### main ###

if (len(sys.argv) < 3):
	print "need an argument for the grammar filename and the data filename:"
	exit 

grammar_filename = sys.argv[1]
grammar_string = read_file(grammar_filename)
(terminal_strings, nonterminal_strings) = eval(grammar_string)
rules = []
for s in nonterminal_strings:
	rules.append(RuleState.parse_RuleState(s))

terminals = []
for s in terminal_strings:
	# the terminal rules have pipes, but the nonterminals have spaces
	s=re.sub("\|", " ", s) + ",x"	
	#print "---------->", s
	terminals.append(RuleState.parse_RuleState(s))

words_string = read_txt_file(sys.argv[2])
words_list = eval(words_string)

for w in words_list:
	##print w
	words = w.split()
	e = Early(words, rules, terminals) 
	e.earley()

