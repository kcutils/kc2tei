#!/usr/bin/env sh

JAVA=$( which java )

if [ $? -ne 0 ]; then
  echo "Java not found in PATH! Exiting ..."
  exit 1
fi

SCRIPT=$( readlink -f "$0" )
SCRIPT_PATH=$( dirname "$SCRIPT" )

JAR="${SCRIPT_PATH}/lib/kc2tei.jar"

if echo "$OSTYPE" | grep "cygwin" 2>&1 > /dev/null; then
  OUT=$( cygpath -w $JAR )
  if [ $? -eq 0 ]; then
    JAR=$OUT
  fi
fi

if [ ! -f "$JAR" ]; then
  echo "kc2tei JAR file not found! Exiting ..."
  exit 1
fi

"$JAVA" -Dfile.encoding=UTF-8 -jar "$JAR" $@

