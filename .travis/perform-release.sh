#!/bin/sh
#
# Create a release on GitHub
# adopted from https://synyx.de/blog/2018-01-24-travisci-github-releases/
# and from https://github.com/jutzig/github-release-plugin

SETTINGS=.travis/settings.xml

echo =====
echo Creating the release artifact
echo =====

mvn package --settings "$SETTINGS" -Drevision=${TRAVIS_BUILD_NUMBER} -DskipTests=true --batch-mode

echo =====
echo Releasing to GitHub
echo =====

mvn github-release:release -Dgithub.draft=true --projects javafx-ui --settings "$SETTINGS" -Drevision=${TRAVIS_BUILD_NUMBER} -DskipTests=true --batch-mode
