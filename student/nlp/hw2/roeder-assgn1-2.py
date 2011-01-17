#!/usr/bin/python
#
# This  is part of hw1 in Jim Martin's NLP class.
# It implements some improvements over the first attempt of improvements.
# This version implements a recursive function that builds all possible
# matche combinations from the dictionary for a hashtag. Then it evaluates
# the list, looking for segmenations that concatenate back to the original
# tag and has the fewest number of segments.
# This code does much better with the large, raw set of twitter hashtags,
# than the previous version.
#
# A detailed explanation is down with the function:  "all_matches()"
#
#
# Chris Roeder, 2010-02-07, Python 2.6


wordsFilename = "top100k.txt"
#tagsFilename="hashtags-dev.txt"
#tagsFilename="skip_hashtags-dev.txt"
tagsFilename="hashtags-test.txt"

#wordsFilename = "christest.txt"
#tagsFilename="christags.txt"

def load_words(filename):
	hash={}
	for line in open(filename):
		word=line[:-1].lower()
		#word=line[:-1]
		hash[word]=''	
	return hash


# Returns all words that start where this word starts.
# The last one would be the max match. 
# Ex. thesis would return [the, thesis]	
def get_prefixes(hashtag, words_hash):
	matches=[]

	# try a word from the vocabulary
	growing_string=""
	for c in hashtag:
		growing_string +=c
		if words_hash.has_key(growing_string) and words_hash[growing_string] != None :
			matches.append(growing_string)
	if len(matches) >0:
		return matches

	# try a number, a sequence of digits
	growing_string=""
	for c in hashtag:
		if c.isdigit() :
			growing_string +=c
			matches.append(growing_string)
		else :
			break
	if len(matches) >0:
		return matches
	
	# try a sequence of seperators
	growing_string=""
	for c in hashtag:
		if c in ['=','^','*','%','$','@','-','_','+','&',' ','|','#', '!','?',')',']','[','(','/']:
			growing_string +=c
			matches.append(growing_string)
		else:
			break
	if len(matches) >0:
		return matches
	

	return matches


# start by calling 
#   all_matches("thetabledown", lexicon) 
# which will work with get_prefixes("thetabledown", lexicon))
#                or    ["the","theta"]
# and subsequently call:
#   all_matches("tabledown", lexicon))
#   all_matches("bledown", lexicon)
# repeating until there is no string left to continue on
# The return values are lists of lists of segments and get assembled
# on the way back up. 

def all_matches(hashtag, lexicon,prefix):
	prefix = prefix + "  "
	if len(prefix) > 20:
		return ["XXX"]
	choices = get_prefixes(hashtag,lexicon)
	segmentation_strings=[]
	for choice in choices:
		if len(choice) > 0:
			shorter_tag = hashtag[len(choice):]
			list = all_matches(shorter_tag, lexicon, prefix)
			if (len(list) > 0):
				for match in list:
					segmentation_string=choice + " " + match
					segmentation_strings.append(segmentation_string)
			else:
				return [choice]				
	return segmentation_strings



def main():
	words_hash = load_words(wordsFilename)
	#words_hash = load_words("testwords.txt")
	######print words_hash
	for line in open(tagsFilename):
		if line[0]=="#":
			trimmed_tag = line[1:-1].lower()
			#print trimmed_tag,

			segment_list=all_matches(trimmed_tag, words_hash, "")
			good_segment_list=[]	
			for segmentation in segment_list:
				recreated_hashtag=""
				minimum_number_parts=99
				parts = segmentation.split()
				if len(parts) < minimum_number_parts:
					minimum_number_parts = len(parts)
				# I should read the regexp package doc.s and subsitute out whitespace
				for p in parts:
					recreated_hashtag+=p
				if recreated_hashtag == trimmed_tag:
					good_segment_list.append(segmentation)

			# with no other criteria than they have to catenate to the tag,
			# and have the minimum number, the first is as good as any:
			good_flag = 0
			for good in good_segment_list:
				if len(good.split()) == minimum_number_parts:
					good_flag = 1
					print good
					break
				#else:
				#	print ""
			#print ""
			if not good_flag:
				print "NO GOOD"
			
main()



