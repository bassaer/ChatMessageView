#!/bin/sh

# AAR build
./gradlew chatmessageview:assembleRelease

# create maven repo
./gradlew chatmessageview:uploadArchives

# deploy to bintray
./gradlew chatmessageview:build
./gradlew chatmessageview:bintrayUpload