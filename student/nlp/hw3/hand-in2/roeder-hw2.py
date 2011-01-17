#!/usr/bin/python

# hw3
# Chris Roeder, 2010-03-01
#
# Start with CKY on p.440
# then modify to hold many parses as described later on p.440
# - augment the entries in the table so each non-terminal is
#   paired with pointers to the table entries from which it was derived
# - permit multiple versions of the same non-terminal to be entered
#   into the table

# The table is an (N+1)x(N+1) table initialized with the 
# input words on the diagonal. Each cell  holds a symbol.

import sys

#### read data #########

def init_table(words):
	n = len(words)
	table = []
	for i in range(0,n+1):
		table.append([])
		for j in range(0,n+1):
			table[i].append([])
			table[i][j]=[]
	for i in range(0,n):
		table[i+1][i+1]=[(words[i].strip(),(0,0),(0,0))]
	return table



# produces a hash from string --> list
# the key is the head, the list is any of the non-terminals that may replace it
def parseTerminalRules(rulesTextArray) :
	rules = {}
	for rule in rulesTextArray:
		(head, tail ) = rule.split("-->")
		tails=tail.split("|")
		rules[head] = tails
	return  rules


# produces a hash-like list from string -->  list
# It is a list of lists of lists.
# ...a list of these: ['key', ['v1','v2']]
def parseNonTerminalRules(rulesTextArray) :
	rules = []
	for rule in rulesTextArray:
		(head, tail ) = rule.split("-->")
		tails=tail.split(" ")
		rule=[head.strip(), tails]
		rules.append(rule)
		#print rule
	return  rules


# For terminals, this produces a hash from  the terminal 
# to a list of non-terminals that may produce it:
#   VP --> ['book', 'include', 'prefer']
#   Proper-noun --> ['NWA']
#   S --> ['book', 'include', 'prefer']
#   Nominal --> ['book', 'flight', 'meal', 'money']
#   Verb --> ['book', 'include', 'prefer']
# produce:
#   book --> ['Noun', 'VP', 'S', 'Nominal', 'Verb']
#
def createInverseHash(hash):
	inverse_hash = {}
 	for key in hash.keys():
		for value in hash[key]:
			if inverse_hash.has_key(value) :
				inverse_hash[value].append(key)
			else:
				inverse_hash[value]=[key]
	return inverse_hash


##### core functions ######

def crossProduct(a_list, b_list):
	cross_product_list = []
	for  a in a_list:
		for b in b_list:
			cross_product_list.append([a,b])
	return cross_product_list


# returns a list of non-terminal symbols based on the word
# ...that generate the word
def findHeadsFromTerminalWord(word, t_inverse): 
	rules=[]
	if t_inverse.has_key(word):
		ruleHeads = t_inverse[word]
		for nt in ruleHeads:
			rules.append(nt)
	return rules



# given a list of triples like [('a',(1,2),(2,4)),('b',(2,3),(4,5))]
# return a list of the symbols like ['a', 'b']
def flattenTriples(list) :
	retval = []
	for t in list:	
		(a,b,c) = t
		#retval.append(a[0])
		retval.append(a)

	return retval


# Given a list of possible B symbols, and C symbols
# find the combination bc, b element of B, c element of C
# where a rule like A-->BC exists, return the A.XX return the whole rule(s).
#
# This is more fun when you remember that while a hash would
# be a really cool data structure to store the rules in,
# since you can have  more than one rule with the same head,
# it won't work. This doesn't just mean  you can't represent
# the list as a hash, but that  you can't just return the
# head of the apropriate rule...there may be more than one
# so you have to return the whole rule.
def findAHeadsWithTailBC(b_list_triples ,c_list_triples, nt_list):
	b_list = flattenTriples(b_list_triples)
	c_list = flattenTriples(c_list_triples)
	##print "LISTS:", b_list, c_list
	combinations = crossProduct(b_list, c_list)
	rules=[]
	for pair in combinations:	
		for rule in nt_list:
			body = rule[1]
			if body == pair:
				rules.append(rule)
				##print "    matched", body, rule ### DEBUG
	#print
	return rules

	
# function CKY-Parse(words, grammar) returns table
# 	for j in [1:len(words)]:
#		table[j-1, j] = list of heads of productions that produce word[j]
#		for i in [j-2:0]:
#			for k in [i+1:j-1] :
#				table[i,j] = table[i,j].append list of non-terminals that are on the right of rules in table[i,k], and table[k,j]

def cky_parse(words, grammar):
	(t_hash, t_inverse, nt_list) = grammar
	table =  init_table(words)
 	for j in range(1, len(words) +1):	
		heads=findHeadsFromTerminalWord(words[j-1], t_inverse)
		if len(heads) > 0:
			triple = (heads[0], (j,j), (j,j))
			table[j-1][j] = table[j-1][j] + [triple]
			for i in range(j-2+1, 0-1, -1):
				##print_table(table)
				##print "------------------------------"
				##print
				##print "row:", i, "col:",j , range(i,j+1)
				for k in range(i-1, j): 
					#print "K:", (i,k), table[i][k], (k,j),table[k][j]
					rules = findAHeadsWithTailBC(table[i][k],table[k][j], nt_list)
					if rules != [] :
						for r in rules:
							newNode = [(r[0], (i,k), (k,j))] 
							table[i][j] =  table[i][j] + newNode 

 	return table


######## printing ###########

# make_spaces for indented printing
def make_spaces(n):
	spaces=""
	for i in range(0,n):
		spaces += " "
	return spaces	

def print_table(t):				
	column_width=35

	# print table
	for row in t:
		for col in row[1:]:
			if 0:
				space = make_spaces(column_width - len(str(col)))
				printthis =  str(col) + space
			else:
				printthis=""
				if len(col) > 0:	
					letters = ""
					for l in col:
						letters = letters + str(l[0])
					space = make_spaces(column_width - len(letters))
					printthis =  letters + space
				else:
					space = make_spaces(column_width -1)
					printthis = "-" + space

			print printthis[:column_width],	
		print

	# check for error (no S at top)
	if t[0][-1] == []:
		print "ERROR null"
		print
		sys.exit(-1)
	else :
		foundGood=0
		for triple in t[0][-1]:
			(a,b,c) = triple
			if (a == 'S'):
				foundGood=1
		if not foundGood:
			print "ERROR xx", a, "--", t[0][-1]
			print
			sys.exit(-1)
	print

def print_table_nodes(t):				
	print "------nodes------------"
	rowNum=0
	for row in t:
		colNum=0
		for col in row:
			print (rowNum, colNum), str(col)
			colNum+=1
		print
		rowNum+=1
	print

def getAsList(table) :
	retval = []
	# start from the nth node in the upper-right
	topRow = table[0]	
	headNodes = topRow[-1]
	for node in headNodes :
		nList = getNodeList(table,node)
		if nList[0] == 'S' :
			retval.append(nList)

	return retval

def getNodeFromTable(table, rowNum, colNum):
	row = table[rowNum]
	col = row[colNum]
	if len(col) > 0:
		return col[0]
	else:
		return None

def printNodeList(table, node, space):
	retval = ""
	if node != None:
		retval = "["
		(letter, left, down) = node
		retval = space + retval + str(letter) + str(left) + str(down)
		(leftRow, leftCol) = left
		(downRow, downCol) = down
		space = space + "    "
		retval = retval + printNodeList(table, getNodeFromTable(table,leftRow, leftCol),space)
		if leftRow != downRow and leftCol != downCol:
			retval = retval + printNodeList(table, getNodeFromTable(table,downRow, downCol), space)
		retval = "\n" + retval + "\n" + space[4:] + "]"

	return retval

def getNodeList(table, node):
	retval = None

	if node != None:
		retval = []
		(letter, leftTuple, downTuple) = node
		retval.append(letter)
		###retval.append(left)
		###retval.append(down)

		(leftRow, leftCol) = leftTuple
		(downRow, downCol) = downTuple
		leftNode = getNodeList(table, getNodeFromTable(table,leftRow, leftCol))
		if leftNode != None:
			retval.append(leftNode)

		downNode=None
		if leftRow != downRow and leftCol != downCol:
			downNode = getNodeList(table, getNodeFromTable(table,downRow, downCol))
			if downNode != None:
				retval.append(downNode)

		# Fix for special case that leaves are not lists.
		# Make it so the output looks like ['S' ['A', 'a']] not ['S' ['A', ['a']]] 
		# This is a leaf node if both left and down are None
		# In this case, return the thing, not a list containing it
		if leftNode == None and downNode == None :
			return letter	
		else:	
			return retval

	else:
		return None

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

######## main ############

if (len(sys.argv) < 3) :
	print "need 2 arguments for  pair of files for grammar and data"
	exit

grammar_filename=sys.argv[1]
data_filename=sys.argv[2]

grammar = read_file(grammar_filename)
###print grammar
(terminal, non_terminal) = eval(grammar)

data=[]
if data_filename[-5:] ==  ".data":
	data = eval(read_file(data_filename))
if data_filename[-4:] ==  ".txt":
	data = eval(read_txt_file(data_filename))

print data

t_hash = parseTerminalRules(terminal)
t_inverse =  createInverseHash(t_hash)
nt_list =  parseNonTerminalRules(non_terminal)

grammar = (t_hash, t_inverse, nt_list)	
for line in data[:-1]:
	words = line.split(" ");
	#print line
	parse = cky_parse(words, grammar)
	#print_table(parse)
	##print_table_nodes(parse)

	print  getAsList(parse)


