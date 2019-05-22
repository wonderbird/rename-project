#!/bin/sh
#
# Create a release on GitHub
# adopted from https://synyx.de/blog/2018-01-24-travisci-github-releases/
# and from https://github.com/jutzig/github-release-plugin

DRY_RUN=false
SCRIPTNAME=$(basename "$0")

echo =====
echo Releasing to GitHub
echo =====

mvn verify --settings .travis/settings.xml -Drevision=${TRAVIS_BUILD_NUMBER} -DskipTests=true --batch-mode --update-snapshots -Prelease
