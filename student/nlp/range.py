#!/usr/bin/python

a =  []
print a
a = range(1,5)
b = range(7,9)
print a
print b
a.extend(b)
print a


# no FP?!!!
#print a.append(80)

c=[2,4]
print a
for thing in c:
	a.remove(thing)

print a
