#!/bin/sh
#
# Create a release on GitHub

echo =====
echo Creating a release on GitHub
echo =====

# Use the Maven Release Plugin to create a release non-interactively
# See also: http://maven.apache.org/maven-release/maven-release-plugin/usage.html
mvn -DdryRun=true -DscmCommentPrefix="[skip ci] " release:prepare

# TODO: Publish the .dmg file to the GitHub release page
# GraphQL 4 API: https://developer.github.com/v4/
#                https://developer.github.com/v4/guides/using-the-explorer/
# Read: https://developer.github.com/v3/repos/releases/#create-a-release

# In order to test the release, run the following command
#
#mvn -DdryRun=true release:prepare
#
# Then check the outputs and clean up
#
#mvn release:clean
