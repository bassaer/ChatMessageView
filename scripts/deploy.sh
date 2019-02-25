#!/bin/sh

set -e

if [ "$TRAVIS_PULL_REQUEST" != "false" ]; then
    echo "pull request build."
    exit 0
fi

curr_ver=$(git describe --tags --abbrev=0)
next_ver=$(scripts/changelog.sh -v)
if [ $curr_ver = $next_ver ]; then
    echo 'skip deploy'
    exit 0
fi

# AAR build
./gradlew chatmessageview:assembleRelease

# create maven repo
./gradlew chatmessageview:uploadArchives

# deploy to bintray
./gradlew chatmessageview:build
./gradlew chatmessageview:bintrayUpload
