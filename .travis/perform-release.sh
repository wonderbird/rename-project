#!/bin/sh
#
# Create a release on GitHub
# adopted from https://synyx.de/blog/2018-01-24-travisci-github-releases/
# and from https://github.com/jutzig/github-release-plugin

SETTINGS=.travis/settings.xml
MVN="mvn --settings $SETTINGS -Drevision=${TRAVIS_BUILD_NUMBER} -DskipTests=true --batch-mode"

echo =====
echo Creating the release artifact
echo =====

$MVN package

echo =====
echo Renaming the release artifact
echo =====

PROJECT_VERSION=$($MVN org.apache.maven.plugins:maven-help-plugin:2.1.1:evaluate -Dexpression=project.version | grep -E '^[^\[]')
PROJECT=javafx-ui
mv -v $PROJECT/target/$PROJECT-$PROJECT_VERSION.dmg $PROJECT/target/rename-project-$PROJECT_VERSION.dmg
rm -v $PROJECT/target/$PROJECT-$PROJECT_VERSION.jar

echo =====
echo Releasing to GitHub
echo =====

$MVN github-release:release --projects javafx-ui
