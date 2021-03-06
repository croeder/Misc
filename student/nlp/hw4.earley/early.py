# Early
#
# ref: http://en.wikipedia.org/wiki/Earley_parser

from ruleState import RuleState
import copy

class Early:
	def __init__(self, words, rules, terminals):
		self.words = words
		self.terminals = terminals
		self.rules = rules
		self.chart=[]
		for w in words:
			self.chart.append([])
		self.chart.append([])
		startState = RuleState.parse_RuleState("gamma-->S,x")
		self.chart.append([])
		self.chart[0].append(startState)

	def print_chart(self):
		print
		print "WORDS:", self.words
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

	def early(self):
		i=0
		while (i <= len(self.words) ) :
			print "Chart[" + str(i) + "]"
			###self.print_chart()
			for state in self.chart[i]: 
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
					self.completer(state,i)
			i=i+1
			
		return self.chart

	def is_rule_in_state(self, in_rule, rule_list):
		for r in rule_list:
			if in_rule == r:
				return True
		return False

	def get_word_pos(self, word, pos):
		# this really asks if there is a terminal rule with a head
		# value pos, that has word in its list of symbols	
		for r in self.terminals:
			if r.next_cat() == "POS" and word in r.symbols and r.get_head() == pos:
				return r
		return None

	def predictor(self, state, chart_index):
		#  take the symbol immediately to the right of the dot
		# if it is a non-terminal and not a POS, find rules that 
		# expand that non-terminal
		# After setting the token range, add it to the current chart.
		nonT = state.get_next_symbol()
		#state.printme()
		for r in self.rules:
			if r.get_head() == nonT :
				if not self.is_rule_in_state(r, self.chart[chart_index]):
					r.start_token_range = state.end_token_range
					r.end_token_range = state.end_token_range
					self.chart[chart_index].append(r)	
					print "predictor :", 
					r.printme()
				#else:
					#print "Not adding because it's already there:", r.printme()
			#else:
				#print "no applicable rule:", r.get_head(), nonT

	def scanner(self, state, chart_index):
		# given a POS rule, see if the current word is that POS
		# then add it to the next chart: Verb-->book * [0,1]
		# advancing the dot
		pos=state.get_next_symbol()
		if chart_index < len(self.words):
			word=self.words[chart_index]
			rule=self.get_word_pos(word, pos)
			if rule != None:
				if not self.is_rule_in_state(rule, self.chart[chart_index + 1]):
					new_rule = RuleState(pos, [word], 1, chart_index, chart_index + 1, rule.get_semantics())
					print "scanner   :",
					new_rule.printme()
					self.chart[chart_index + 1].append(new_rule)
		
	def completer(self, state, chart_index):
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

		#print "======================"
		#state.printme()
		#print "======================"
		completed_symbol = state.get_head()
		#for rule in self.chart[chart_index -1]:
		#print "LOOKING IN state:", state.start_token_range
		##self.print_chart(state.start_token_range)	
		for rule in self.chart[state.start_token_range]:
			if rule.get_next_symbol() == completed_symbol and rule.end_token_range == state.start_token_range:
				print "completer :",

				# GAAACK! don't modify the old rule, need to clone it!!!
				newRule = copy.copy(rule)
				newRule.advance(state.end_token_range)


				newRule.printme()
				if not self.is_rule_in_state(newRule, self.chart[chart_index]):
					self.chart[chart_index].append(newRule)
				#self.chart[state.end_token_range].append(rule)
			#else:
				#print "wtf:", rule.get_head(), "[", rule.start_token_range, rule.end_token_range, "]", "[", state.start_token_range,  state.end_token_range , "]"
