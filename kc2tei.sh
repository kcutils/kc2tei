#!/usr/bin/env sh

JAVA=$( which java )

if [ $? -ne 0 ]; then
  echo "Java not found in PATH! Exiting ..."
  exit 1
fi

SCRIPT=$( readlink -f "$0" )
SCRIPT_PATH=$( dirname "$SCRIPT" )

JAR="${SCRIPT_PATH}/target/kc2tei.jar"

if [ ! -f "$JAR" ]; then
  echo "kc2tei JAR file not found! Exiting ..."
  exit 1
fi

$JAVA -Dfile.encoding=UTF-8 -jar "$JAR" $@

