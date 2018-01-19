#!/bin/sh
#
# Increment the product version

echo =====
echo Bumping Version
echo =====

echo Version: `mvn org.apache.maven.plugins:maven-help-plugin:2.1.1:evaluate -Dexpression=project.version | grep -Ev '(^\[|Download\w+:)'`
