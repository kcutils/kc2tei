#!/usr/bin/env sh

JAVA=$( which java )

if [ $? -ne 0 ]; then
  echo "Java not found in PATH! Exiting ..."
  exit 1
fi

JAR="target/kc2tei-0.1.jar"

if [ ! -f "$JAR" ]; then
  echo "kc2tei JAR file not found! Exiting ..."
  exit 1
fi

$JAVA -jar "$JAR" $@

