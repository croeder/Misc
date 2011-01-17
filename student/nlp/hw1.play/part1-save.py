#!/usr/bin/python

import trie

#wordsFilename = "top100k.txt"
#wordsFilename = "top10k.txt"
#wordsFilename = "top1k.txt"
#wordsFilename = "top100.txt"
#wordsFilename = "top20.txt"
#wordsFilename = "test.txt"
wordsFilename = "test_vocabulary.txt"

#tagsFilename="hashtags-dev.txt"
#tagsFilename="skip_hashtags-dev.txt"
tagsFilename="hashtags-test.txt"

t = trie.trie()
t.insert_file(wordsFilename)

for line in open(tagsFilename):
	if (line[0] == "#"):
		hashtag = line[1:-1] # strip leading pound and newline
		print "----------------------" + hashtag
		part = ""
		last_word = ""
		last_index=0
		index=0
		backup_hack = -1
		while (index < len(hashtag)):
			c = hashtag[index]
			part = part + c
			#print  c, part
			if t.is_word(part) :
				last_word = part
				last_index = index
			if not t.is_part_of_word(part) :
				#print "is not part of word:", part, index
				print last_word, index
				# went too far, backup
				index = last_index + 1
				if backup_hack == index:
					# been here before, and going back didn't work the first time
					index = len(hashtag)
				backup_hack = index
				if index < len(hashtag) :
					part=hashtag[last_index+1]
					if not t.is_part_of_word(part):
						# backing up didn't help, we're done
						#print "XXXXXXXXXXXXXXXXXXXXXX  should quit here..."
						index = len(hashtag)
					#else:
						#print "this is part a word: " + part
				else:
					part=""
			index+=1
		if t.is_word(last_word):
			print "?", last_word
	
	
	
	
	
	
