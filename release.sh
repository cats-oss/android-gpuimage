#!/bin/bash
if [ -z "$1" ]
then
  echo "Usage: sh release.sh 1.0"
  exit 0
fi
VERSION=$1
echo "New version is: $VERSION"

# Write version into VERSION file
echo $VERSION > VERSION

#################
# Android Related
#################

# Replace version in AndroidManifest.xml
function updateManifest {
  perl -pi -e 's#(android:versionCode=")([0-9]+)(")#"$1" . ($2 + 1) . "$3"#e;' $1
  perl -pi -e "s/android:versionName=\"(.*)\"/android:versionName=\"$VERSION\"/g" $1
}
updateManifest "sample/AndroidManifest.xml"
updateManifest "library/AndroidManifest.xml"

git status
git diff

echo -n "Commit changes and continue? "
while true
do
  echo -n " (y or n) : "
  read CONFIRM
  case $CONFIRM in
    y|Y|YES|yes|Yes) break ;;
    n|N|no|NO|No)
    echo "Aborting..."
    exit
    ;;
    *) echo Please enter only y or n
  esac
done

git commit -a -m "Update version to v$VERSION"

mvn release:clean
mvn release:prepare
mvn release:perform