#!/bin/bash

javac Shutdown.java
java Shutdown &
echo "should be running now:"
ps -aef | grep java | grep Shutdown

sleep 10

PID=`ps -aef | grep java | grep Shutdown | awk '{print $2}'`
kill $PID
echo "shouldn't be found now"
ps -aef | grep java | grep Shutdown
