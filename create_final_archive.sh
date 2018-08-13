#!/bin/sh
#
# This script creates a archive file containing all relevant kc2tei
# program files and some data for testing, etc.
#

SCRIPT=$( readlink -f "$0" )
SCRIPT_PATH=$( dirname "$SCRIPT" )

MVN_ARCHIVE_FILE="target/kc2tei-0.2-bin.zip"
MVN_ARCHIVE_CONTENT_DIR="kc2tei-0.2/"

ARCHIVE_FILE="target/kc2tei.zip"

TMP_ARCHIVE_DIR="target/zip_archive/"


usage () {
cat << EOF

  $0 [-h|--help]

  This script creates a archive file containing all relevant kc2tei
  program files and some data for testing, etc.

  -h|--help    print this output

EOF
}

while [ $# -gt 0 ]; do
  case $1 in
    -h|--help)
      usage
      exit
    ;;
    *)
    ;;
  esac
  shift
done

cd $SCRIPT_PATH

# clean up
rm -f $MVN_ARCHIVE_FILE
rm -f $ARCHIVE_FILE
rm -rf $TMP_ARCHIVE_DIR

# create archive using maven commands

echo "Creating archive ..."
OUT=$( mvn verify 2>&1 )

if [ $? -ne 0 ]; then
  echo "Error while creating final archive!"
  echo "$OUT"
  echo "Exitting ..."
  exit 1
fi

#
# maven creates target/kc2tei-$VERSION-bin.zip file
# containing a directory kc2tei-0.2/ with all relevant files
#
# we want target/kc2tei.zip with the containing directory
# kc2tei/
#
# so we need to extract the archive, an reassemble it
# properly
#

OUT=$( mkdir $TMP_ARCHIVE_DIR 2>&1 )
if [ $? -ne 0 ]; then
  echo "Error while creating tmp-dir!"
  echo "$OUT"
  echo "Exitting ..."
  exit 1
fi

OUT=$( unzip -d $TMP_ARCHIVE_DIR $MVN_ARCHIVE_FILE 2>&1 )
if [ $? -ne 0 ]; then
  echo "Error while unzipping archive!"
  echo "$OUT"
  echo "Exitting ..."
  exit 1
fi

OUT=$( mv ${TMP_ARCHIVE_DIR}/${MVN_ARCHIVE_CONTENT_DIR} ${TMP_ARCHIVE_DIR}/kc2tei 2>&1 )
if [ $? -ne 0 ]; then
  echo "Error while renaming folder in archive!"
  echo "$OUT"
  echo "Exitting ..."
  exit 1
fi

cd ${TMP_ARCHIVE_DIR}

OUT=$( zip -r ${SCRIPT_PATH}/${ARCHIVE_FILE} kc2tei/ 2>&1 )
if [ $? -ne 0 ]; then
  echo "Error while creating final archive!"
  echo "$OUT"
  echo "Exitting ..."
  exit 1
fi

unzip -l ${SCRIPT_PATH}/${ARCHIVE_FILE}

