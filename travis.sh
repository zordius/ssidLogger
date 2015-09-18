#!/bin/sh

if [ "${TRAVIS_PULL_REQUEST}" != "false" ]; then
  echo "This is a PR, skip deploy."
  exit 0
fi

# Skip deploy when not master
if [ "${TRAVIS_BRANCH}" != "master" ]; then
  echo skip deploy because now in branch ${TRAVIS_BRANCH}
  exit 0
fi

# Setup git
git config --global user.name "Travis-CI"
git config --global user.email "zordius@yahoo-inc.com"

cd app/build/outputs/apk
git init
git add .

# push document
git commit -m "Auto deployed to Github Pages from branch ${TRAVIS_BRANCH} @${TRAVIS_COMMIT} [ci skip]"
git push --force --quiet "https://${GHTK}@github.com/zordius/ssidLogger.git" master:gh-pages > /dev/null 2>&1
