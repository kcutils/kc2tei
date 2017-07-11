#!/usr/bin/env sh

JAVA=$( which java )

$JAVA -jar target/kc2tei-0.1.jar $@

