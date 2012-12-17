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

echo ""
echo "# Now run:"
echo ""
echo git commit -a -m \"Update version to v$VERSION\"
echo mvn release:clean
echo mvn release:prepare
echo mvn release:perform