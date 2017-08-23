#!/bin/sh
#
# take files with prosodic annotation if they exist (.s2) otherwise
# take file without prosodic annotation (.s1)
#
# take only long/unsplitted files (*_l.s? or *_r.s?)
#

KC_ROOT=

usage () {
cat << EOF

  $0 [-h|--help] KC_ROOT

  -h|--help    print this output

  KC_ROOT      the root directory of Kiel Corpus

EOF
}

while [ $# -gt 0 ]; do
  case $1 in
    -h|--help)
      usage
      exit
    ;;
    *)
      KC_ROOT=$1
    ;;
  esac
  shift
done

if [ "$KC_ROOT" = "" ] || [ ! -d "$KC_ROOT" ]; then
  echo "Error: Please specify the root directory of Kiel Corpus!"
  usage
  exit 1
fi

ALL_FILES=$( find $KC_ROOT -type f -name \*\.s[12] | rev | cut -d '.' -f 2- | rev | sort -u )
UNSPLITTED_FILES=$( echo "$ALL_FILES" | grep -E "_[lr]{1}$" )

FILE_PREFIXES=$ALL_FILES

# remove all splitted files from all files list

for FILE in $UNSPLITTED_FILES; do
  FILE_PREFIX=$( echo "$FILE" | rev | cut -d _ -f 2- | rev )

  OUT=$( echo "$FILE_PREFIXES" | grep -vE "^${FILE_PREFIX}[[:alnum:]]+$" )

  FILE_PREFIXES="$OUT
$FILE"
  
done

FILES=

# take .s2 file of remaining files if they exist
# .s1 file otherwise
for FILE in $FILE_PREFIXES; do
  if [ -f "${FILE}.s2" ]; then
    FILES="$FILES
${FILE}.s2"
  else
    FILES="$FILES
${FILE}.s1"
  fi
done

echo "$FILES" | grep -v "^$" | sort -u
