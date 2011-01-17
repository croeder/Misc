#!/usr/bin/python
#
# part1.py is part of hw1 in Jim Martin's NLP class.
# Given a hashtag from twitter, try to identify the
# constituent words using the MaxMatch algorithm. 
# The words come from the top of the google corpus.
#
# Chris Roeder, 2010-02-01, Python 2.6


wordsFilename = "top100k.txt"
#wordsFilename = "top10k.txt"
#wordsFilename = "top1k.txt"
#wordsFilename = "top100.txt"
#wordsFilename = "top20.txt"
#wordsFilename = "test.txt"
#wordsFilename = "test_vocabulary.txt"

#tagsFilename="hashtags-dev.txt"
#tagsFilename="hashtags-test.txt"
tagsFilename="skip_hashtags-dev.txt"

def load_words(filename):
	hash={}
	for line in open(filename):
		word=line[:-1]
		hash[word]=''	
	return hash

# Returns all words that start where this word starts.
# The last one would be the max match. 
# Ex. thesis would return [the, thesis]	
def get_hashtag_words(hashtag, words_hash):
	matches=[]
	growing_string=""
	for c in hashtag:
		growing_string +=c
		if words_hash.has_key(growing_string) and words_hash[growing_string] != None :
			matches.append(growing_string)
	return matches

def test():
	words_hash = load_words(wordsFilename)
	for line in open(tagsFilename):
		if line[0] == "#":
			print line[:-1], "   ",
			words=get_hashtag_words(line[:-1], words_hash)
			for word in words:
				print word,
			print

def max_match(hashtag, words_hash):
	matches=[]
	remaining_tag = hashtag
	while len(remaining_tag) > 0:
		word_list = get_hashtag_words(remaining_tag, words_hash)
	
		if (len(word_list) < 1):
			break;

		this_part = word_list[-1]	
		l=len(this_part)
		matches.append(this_part)	
		remaining_tag=remaining_tag[(l):]
	return matches

def main():
	words_hash = load_words(wordsFilename)
	for line in open(tagsFilename):
		if line[0]=="#":
			print line[:-1],
			parts=max_match(line[1:-1], words_hash)
			reconstituted=""
			for word in parts:
				print word + ", ",
				reconstituted+=word
			if reconstituted != line[1:-1] :
				print "failure:",
			print

#test()
main()
