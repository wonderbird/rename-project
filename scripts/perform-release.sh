#!/bin/sh
#
# Create a release on GitHub

echo =====
echo Creating a release on GitHub
echo =====

# Use the Maven Release Plugin to create a release non-interactively
# See also: http://maven.apache.org/maven-release/maven-release-plugin/usage.html
mvn -DdryRun=true -Dusername="Travis CI <stefan.boos@gmx.de>" release:prepare

# In order to test the release, run the following command
#
#mvn -DdryRund=true release:prepare
#
# Then check the outputs and clean up
#
#mvn release:clean
