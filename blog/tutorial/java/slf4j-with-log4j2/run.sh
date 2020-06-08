#!/bin/sh
export CLASSPATH=/home/henriette/.m2/repository/org/apache/logging/log4j/log4j-api/2.13.3/log4j-api-2.13.3.jar:/home/henriette/.m2/repository/org/apache/logging/log4j/log4j-core/2.13.3/log4j-core-2.13.3.jar:/home/henriette/.m2/repository/org/apache/logging/log4j/log4j-slf4j-impl/2.13.3/log4j-slf4j-impl-2.13.3.jar:/home/henriette/.m2/repository/org/slf4j/slf4j-api/1.7.25/slf4j-api-1.7.25.jar:./target/tutorial-slf4j-with-log4j2-0.0.1-SNAPSHOT.jar

#java -classpath $CLASSPATH -Dlog4j.debug=true org.henrietteharmse.tutorial.SimpleLoggingTest
java -classpath $CLASSPATH org.henrietteharmse.tutorial.SimpleLoggingTest
