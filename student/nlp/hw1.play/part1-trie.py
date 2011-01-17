#!/usr/bin/python
#
# part1.py is part of hw1 in Jim Martin's NLP class.
# Given a hashtag from twitter, try to identify the
# constituent words. The words come from the top of 
# the google corpus.
#
# Chris Roeder, 2010-02-01, Python 2.6

import trie

#wordsFilename = "top100k.txt"
#wordsFilename = "top10k.txt"
#wordsFilename = "top1k.txt"
#wordsFilename = "top100.txt"
#wordsFilename = "top20.txt"
#wordsFilename = "test.txt"

#tagsFilename="hashtags-dev.txt"
#tagsFilename="skip_hashtags-dev.txt"



	
def max_match_hashtag(hashtag,t):
	part = ""
	last_word = ""
	last_index=0
	index=0
	backup_hack = -1
	matches=[]
	while (index < len(hashtag)):
		part = part + hashtag[index]

		if t.is_word(part) :
			# save word in case going further doesn't work
			last_word = part
			last_index = index

		if not t.is_part_of_word(part) :
			# went too far, backup
			index = last_index + 1

			if  backup_hack == index:
				# been here before, and going back didn't work the first time
				return matches;
			else:
				#print last_word, index
				matches.append(last_word)
				if index < len(hashtag) :
					# reset part so it starts after the last word
					part=hashtag[last_index+1]

					if not t.is_part_of_word(part):
						# backing up didn't help, we're done
						return matches;
				else:
					part=""
				backup_hack = index

		index+=1


	if t.is_word(last_word):
		matches.append(last_word)

	return matches

# I'm sure this is written somehwere...
def min(a,b):
	if (a < b):
		return a
	else:
		return b


def test_with_gs():
	wordsFilename = "test_vocabulary.txt"
	tagsFilename="hashtags-test.txt"
	t = trie.trie()
	t.insert_file(wordsFilename)

	gs=[]
	gs_line=""
	matches=[]
	hash_line=""
	for line in open(tagsFilename):
		if (line[0] == "$"):
			gs_line=line[1:-1]
			gs = gs_line.split(',')
		 
		if (line[0] == "#"):
			hashtag = line[1:-1] # strip leading pound and newline
			hash_line =  hashtag
			matches=max_match_hashtag(hashtag,t)

		if gs and matches:
			print "-->", hash_line
			print "GS>", gs_line
			n = min(len(matches), len(gs))
			for i in xrange(0,n):
				print "T:", i, " "  + matches[i] + " " +  gs[i]
				if (matches[i] != gs[i]):
					print "ERROR:"
			gs=[]
			gs_line=""
			matches=[]
			hash_line=""
				
		else:
			print hash_line
			if matches:
				for match in matches:
					print match

def test():
	wordsFilename = "top100k.txt"
	tagsFilename="skip_hashtags-dev.txt"
	t = trie.trie()
	t.insert_file(wordsFilename)

	matches=[]
	hash_line=""
	for line in open(tagsFilename):
		if (line[0] == "#"):
			hashtag = line[1:-1] # strip leading pound and newline
			hash_line =  hashtag
			matches=max_match_hashtag(hashtag,t)

			print hash_line
			if matches:
				for match in matches:
					print "   ", match

test_with_gs()
#test()
