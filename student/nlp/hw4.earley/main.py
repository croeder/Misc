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


if (len(sys.argv) < 2):
	print "need an argument for the grammar file name:"
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

words="book that flight".split(" ")
e = Early(words, rules, terminals) 
e.early()

