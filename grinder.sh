#!/bin/bash


TESTS=$(ls $PWD/target/*-tests.jar)
PROPERTIES=$PWD/target/grinder.properties
echo "
grinder.script $PWD/target/test-classes/grinder.py
grinder.processes 1
grinder.threads 20
grinder.runs 0
grinder.jvm.classpath $TESTS
grinder.logDirectory $PWD/target/logs
grinder.numberOfOldLogs 0
" > $PROPERTIES


(
cd $(dirname "$0")/grinder/
java -cp grinder.jar net.grinder.Console &
sleep 1
)
(
cd $(dirname "$0")/grinder/
java -cp grinder.jar net.grinder.Grinder $PROPERTIES
)