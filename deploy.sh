#!/bin/sh

# AAR build
./gradlew chatmessageview:assembleRelease

# create maven repo
./gradlew chatmessageview:uploadArchives

# deploy to bintray
./gradlew chatmessageview:build
./gradlew chatmessageview:bintrayUpload

# Publish with Bintray's REST API
VERSION=`cat version.properties | cut -d'=' -f2`
curl -I -X POST -u$BINTRAY_USER:$BINTRAY_API_KEY https://api.bintray.com/content/tnakayama/ChatMessageView/chatmessageview/$VERSION/publish