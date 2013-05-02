#!/bin/bash

swig -java example.i

gcc -c example.c example_wrap.c 
gcc -shared example.o example_wrap.o  -o libexample.dylib
cat main.java
javac main.java
java main


