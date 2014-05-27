#!/bin/bash

ant clean
ant release

lVersion=`grep android.versionName *.xml | sed -e 's/^.*="//' | sed -e 's/".*//'`

mkdir -p RELEASES/$lVersion
cp -p bin/AndroidAppFCC-release.apk RELEASES/$lVersion/FCC.apk
git add RELEASES/$lVersion/FCC.apk
#git push
