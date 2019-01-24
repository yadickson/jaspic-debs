#!/bin/bash

set -ex

PKG="${PACKAGE_NAME:-${1}}"
VERSION="${PACKAGE_VERSION:-${2}}"
ADD_PATCH="${3:-false}"
ZIPFILE="${PKG}-${VERSION}.zip"
ORIG_TARBALL="../${PKG}_${VERSION}.orig.tar.gz"

[ ! -f "${ORIG_TARBALL}" ] || exit 0

rm -rf jaspic-*
rm -rf "${PKG}-${VERSION}"
rm -f "${ZIPFILE}"

wget -c "https://github.com/eclipse-ee4j/jaspic/archive/${VERSION}-RELEASE.zip" -O "${ZIPFILE}" || exit 1

unzip "${ZIPFILE}" || exit 1

rm -f "${ZIPFILE}"

mv jaspic-* "${PKG}-${VERSION}"

rm -rf "${PKG}-${VERSION}"/.gitignore

cp debian/libjaspic-java.pom.xml "${PKG}-${VERSION}"/pom.xml

rm -rf "${PKG}-${VERSION}"/src/main/resources/

find "${PKG}-${VERSION}" -type f -name '*.html' -exec rm -f '{}' \;
find "${PKG}-${VERSION}" -type f -name '*.java' -or -name '*.xml' -exec iconv -f ISO-8859-1 -t UTF-8 '{}' -o '{}'.iconv \; -exec mv '{}'.iconv '{}' \; -exec dos2unix '{}' \;

if [ "${ADD_PATCH}" != "false" ]
then
   while read -r line
   do
      patch -d "${PKG}-${VERSION}" -p1 < "debian/patches/${line}"
   done < debian/patches/series
fi

tar -czf "${ORIG_TARBALL}" --exclude-vcs "${PKG}-${VERSION}" || exit 1

rm -rf "${PKG}-${VERSION}"
rm -f "${ZIPFILE}"

