#!/bin/bash

javac HelloWorld.java

javah -jni HelloWorld

g++ -I/System/Library/Frameworks/JavaVM.framework/Versions/CurrentJDK/Headers \
    -I/Developer/SDKs/MacOS10.6.sdk/System/Library/Frameworks/JavaVM.framework/Versions/A/Headers \
    -c HelloWorld.cpp

g++ -dynamiclib -o libhelloworld.jnilib HelloWorld.o

java HelloWorld

