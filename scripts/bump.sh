#!/bin/bash

set -ef

if [ "$TRAVIS_PULL_REQUEST" != "false" ]; then
    echo "pull request build."
    exit 0
fi

curr_ver=$(git describe --tags --abbrev=0)
next_ver=$(scripts/changelog.sh -v)
if [ $curr_ver = $next_ver ]; then
    echo 'skip bump version'
    exit 0
fi

version=$(scripts/changelog.sh -v)
desc=$(scripts/changelog.sh -d)

openssl aes-256-cbc -K $encrypted_a3a257078b28_key -iv $encrypted_a3a257078b28_iv -in travis_key.enc -out ~/.ssh/id_rsa -d
chmod 600 ~/.ssh/id_rsa
echo -e "Host github.com\n\tStrictHostKeyChecking no\n" >> ~/.ssh/config

git config --global user.name "TravisCI"
git config --global user.email "app.nakayama@gmail.com"

git clone "git@github.com:$TRAVIS_REPO_SLUG.git"
cd $(basename $(git rev-parse --show-toplevel))

sed -i "/implementation/s/[0-9]*\.[0-9]*\.[0-9]*/$version/" README.md
git add README.md
git commit -m "bump version [ci skip]"
git push origin master

body=$(cat << EOF
{
  "tag_name": "$version",
  "target_commitish": "master",
  "name": "$version",
  "body": "$desc",
  "draft": false,
  "prerelease": false
}
EOF
)

curl -X POST -d "$body" -H "Authorization: token $GITHUB_TOKEN" \
    "https://api.github.com/repos/$TRAVIS_REPO_SLUG/releases" \
    > /dev/null 2>&1
