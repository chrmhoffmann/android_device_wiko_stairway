#!/bin/sh

VENDOR=wiko
DEVICE=stairway

BASE=../../../vendor/$VENDOR/$DEVICE/proprietary

echo "Pulling $DEVICE files..."
for FILE in `cat proprietary-files.txt | grep -v ^# | grep -v ^$`; do
DIR=`dirname $FILE`
    if [ ! -d $BASE/$DIR ]; then
mkdir -p $BASE/$DIR
    fi
#adb pull /system/$FILE $BASE/$FILE
cp /work/chris/android/wiko/research/test/system/system/$FILE $BASE/$FILE
done

./setup-makefiles.sh
