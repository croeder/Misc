
import copy 

class Semantics:
	def combine_range(cls,range1, prep_string, range2):
		if prep_string == "to":
			# makes most sense if from ranges are single-valued
			# if you have something like [1..4] to 5 or
			# 2 to [4..7] then you could kludge a larger range
			all_values = copy.copy(range1)
			all_values.extend(range2)
			start = min(all_values)
			end = max(all_values)
			return range(start, end+1)
		elif prep_string == "without":
			# start with range1 and remove items in range2 if present
			new_range=copy.copy(range1)
			for item in range2:
				new_range.remove(item)
			return new_range
		elif prep_string == "from":
			# [3..7] from 9, [3..7] from 4?
			return range1
	classmethod(combine_range)
