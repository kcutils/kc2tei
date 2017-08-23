#!/bin/sh

FILE_LIST=

GOOD_FILES_LIST="good_kcfiles.tmp"
BAD_FILES_LIST="bad_kcfiles.tmp"

CONVERTER="./kc2tei.sh"

usage () {
  cat << EOL

  $0 [-h|--help] FILE_LIST

  -h|--help    print this output

  FILE_LIST    list containing pathes to Kiel Corpus files

EOL
}

clean_exit () {
  wait
  echo
  echo "$ALL_COUNTER files processed."
  if [ -f "$GOOD_FILES_LIST" ]; then
    echo "files without conversion problems: $GOOD_FILES_LIST"
  fi
  if [ -f "$BAD_FILES_LIST" ]; then
    echo "files with conversion problems: $BAD_FILES_LIST"
  fi
  exit
}

trap clean_exit 2

while [ $# -gt 0 ]; do
  case $1 in
    -h|--help)
      usage
      exit
    ;;
    *)
      FILE_LIST=$1
    ;;
  esac
  shift
done

if [ "$FILE_LIST" = "" ] || [ ! -f "$FILE_LIST" ]; then
  echo "Error: Please specify path to file list!"
  usage
  exit 1
fi

FILES=$( cat "$FILE_LIST" )

# do not check files we already checked
#
# with this option we can run this script multiple times
# without needing to start from the very beginning each time
#

if [ -f "$BAD_FILES_LIST" ]; then
  BAD_FILES=$( cat "$BAD_FILES_LIST" )

  for BAD_FILE in $BAD_FILES; do
    FILES=$( echo "$FILES" | grep -v "$BAD_FILE" )
  done
fi

if [ -f "$GOOD_FILES_LIST" ]; then
  GOOD_FILES=$( cat "$GOOD_FILES_LIST" )

  for GOOD_FILE in $GOOD_FILES; do
    FILES=$( echo "$FILES" | grep -v "$GOOD_FILE" )
  done
fi

# we want to see some progress during processing files

FILE_AMOUNT=$( echo "$FILES" | wc -l | awk '{print $1}' )

MSG_ALL_NR_FILES=50

COUNTER=0
ALL_COUNTER=0

echo "Processing $FILE_AMOUNT Kiel Corpus files ..."
echo -n "... "

for FILE in $FILES; do

  # progress message
  if [ $COUNTER -eq $MSG_ALL_NR_FILES ]; then
    ALL_COUNTER=$(( $ALL_COUNTER + $MSG_ALL_NR_FILES ))
    echo -n " $ALL_COUNTER ..."
    COUNTER=0
  fi

  OUT=$( $CONVERTER -i "$FILE" 2>&1 )

  if [ $? -ne 0 ]; then
    echo "$FILE" >> $BAD_FILES_LIST
  else
    echo "$FILE" >> $GOOD_FILES_LIST
  fi

  COUNTER=$(( COUNTER + 1 ))
done

clean_exit

