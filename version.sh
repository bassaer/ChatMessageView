#!/bin/sh

VERSION=`cat version.properties | cut -d'=' -f2`

sed -i "/compile/s/[0-9]*\.[0-9]*\.[0-9]*/$VERSION/" ./README.md

REPOSITORY="git@github.com:${CIRCLE_PROJECT_USERNAME}/${CIRCLE_PROJECT_REPONAME}.git"

git config --global user.name "CircleCI"
git config --global user.email "app.nakayama@gmail.com"

git add ./README.md
git commit -m "update version [ci skip]"

git tag -a $VERSION -m "version $VERSION"

git push origin master

git push origin --tags