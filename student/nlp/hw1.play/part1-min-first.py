#!/usr/bin/python
#
# Python 2.6

import trie

#wordsFilename = "top100k.txt"
#wordsFilename = "top10k.txt"
#wordsFilename = "top1k.txt"
wordsFilename = "top100.txt"
#wordsFilename = "top20.txt"
#wordsFilename = "test.txt"

tagsFilename="hashtags_dev.txt"
tagsFilename="skip_hashtags-dev.txt"

t = trie.trie()
t.insert_file(wordsFilename)

for line in open(tagsFilename):
	hashtag = line[1:]
	print "----------------------" + hashtag
	part = ""
	index=0
	while index < len(hashtag) :
		part = part + hashtag[index]
		if t.is_word(part) :
			print part
			part=""	
		index+=1
	print "----------------------"
