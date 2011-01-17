# Early
#
# ref: p. 443-448 Jurafsky & Martin
# ref: http://en.wikipedia.org/wiki/Earley_parser

from ruleState import RuleState
import copy

class Early:
	def __init__(self, words, rules, terminals):
		self.words = words
		self.terminals = terminals
		self.rules = rules
		self.trees=[] # completed parses
		self.chart=[]
		for w in words:
			self.chart.append([])
		self.chart.append([])
		self.chart.append([])

		startState = RuleState.parse_RuleState("gamma-->S-->None")
		self.chart.append([])
		self.chart[0].append(startState)


		self.debug=False #True


		if False: # self.debug:
			for t in  self.terminals:
				t.printme()
			for r in self.rules:	
				r.printme()

		#if self.debug:
		print "----", words

	def print_chart_all(self):
		print
		print "WORDS:", self.words
		print "TERMINAL RULES:"
		for t in self.terminals:
			t.printme()
		print "RULES:"
		for r in  self.rules:
			r.printme()
		print "CHART:"
		i=0
		while (i < len(self.chart)):
			print i, "----------"
			for r in self.chart[i]:
				r.printme()
			i+=1
		print "----------"

	def print_chart(self, index):
		print "----------"
		print "CHART:", index
		for r in self.chart[index]:
			r.printme()
		print "----------"

	def earley(self):
		i=0
		while (i <= len(self.words) ) :
			if (self.debug):
				print "Chart[" + str(i) + "]", 
				if  i < len(self.words):
					print	"\"" + self.words[i] + "\"" 
				else:
					print
		
			for state in self.chart[i]: 
				if False: #self.debug:
					print "    earley considering state: " ,
					state.printme()
				if state.incomplete() :
					if (state.next_cat() == "not POS"):
						self.predictor(state, i)
					else: 
						if state.next_cat() == "POS":
							self.scanner(state,i)
						else:
							print "impossible error #23. If a rule has no next_cat, it should be complete!"
							print "-->", 
							state.printme()	
				else:
					self.trees.extend(self.completer(state,i))
			i=i+1

		# print closing/ending state	
		if self.debug:
			print "====================================================================="	
			self.print_chart_all()
			print "====================================================================="	
		return self.trees

	def is_rule_in_state(self, in_rule, rule_list):
		for r in rule_list:
			if in_rule == r:
				#print "================================"
				#print " testing:",
				#in_rule.printme()
				#print " rule in state: offender is :" ,
				#r.printme()
				#print "==========^=================="
				return True
		return False

	def get_word_pos(self, word, pos):
		# this really asks if there is a terminal rule with a head
		# value pos, that has word in its list of symbols	
		for r in self.terminals:
			#if r.next_cat() == "POS" and word in r.symbols and r.get_head() == pos:
			if word in r.symbols and r.get_head() == pos:
				return r
		return None

	def predictor(self, state, chart_index):
		#  take the symbol immediately to the right of the dot
		# if it is a non-terminal and not a POS, find rules that 
		# expand that non-terminal
		# After setting the token range, add it to the current chart.
		# *** Add a *copy* to the current chart ***
		nonT = state.get_next_symbol()	
		#if self.debug:
		#	print "    predictor: ", nonT
		#state.printme()
		for r in self.rules:
			if r.get_head() == nonT :
				#if not self.is_rule_in_state(r, self.chart[chart_index]):
				new_r = copy.deepcopy(r)
				new_r.start_token_range = state.end_token_range
				new_r.end_token_range = state.end_token_range
				if not self.is_rule_in_state(new_r, self.chart[chart_index]):
					self.chart[chart_index].append(new_r)	
				if self.debug:
					print "predictor:", 
					new_r.printme()
				#else :
				#	if self.debug:
				#		print "  predictor rejecting:", chart_index, 
				#		r.printme()

	def scanner(self, state, chart_index):
		# given a POS rule, see if the current word is that POS
		# then add it to the next chart: Verb-->book * [0,1]
		# advancing the dot
		#if self.debug:
			#print "    scanner considering:" ,
			#state.printme()
		pos=state.get_next_symbol()
		if chart_index < len(self.words):
			word=self.words[chart_index]
			rule=self.get_word_pos(word, pos)
			##print "    scanner considering:" , word, rule
			if rule != None:
				if not self.is_rule_in_state(rule, self.chart[chart_index + 1]):
					new_rule = RuleState(pos, [word], 1, chart_index, chart_index + 1, rule.get_semantics(), rule.get_semantics_txt())
					if self.debug:
						print "scanner adding to: ", chart_index + 1
						print "scanner   :",
						new_rule.printme()
					self.chart[chart_index + 1].append(new_rule)
				else :
					if True: #self.debug:
						print "  canner not adding",
						state.printme()
		
	def completer(self, state, chart_index):
		# returns list of parse trees
		# Wiki: for every state in chart[chart_index] of the form X-->a * Y B,j
		# Wiki: find states in chart[j] of the form Y-->a * X B, i and add it to chart[chart_index]
		#
		# read the book more carefully: for a rule with [j,k], read from chart j, and put into chart k
		#
		# Here, the loop has found a complete rule and this result needs to bubble up the grammar
		# moving higher rules further along their process.
		# The sate/rule is the one that is completed. The chart at chart_index is where it was found.
		# rules to move forward a step are in chart_index -1. They need to be modified and
		# entered in chart_index.

		return_rules = []

		completed_symbol = state.get_head()
		for rule in self.chart[state.start_token_range]:
			if rule.get_next_symbol() == completed_symbol and rule.end_token_range == state.start_token_range:
				if self.debug:
					print "completer :",
				newRule = copy.deepcopy(rule)
				newRule.advance(state.end_token_range)
				newRule.rules.append(state)
				if self.debug:
					newRule.printme()
				if not self.is_rule_in_state(newRule, self.chart[chart_index]):
					self.chart[chart_index].append(newRule)
					## test for winner
					if newRule.start_token_range == 0 and newRule.head == "S" and newRule.end_token_range == len(self.words)  and newRule.dot_position == len(newRule.symbols) -1 :
						print newRule.print_tree()
						return_rules.append(newRule)

		return return_rules
