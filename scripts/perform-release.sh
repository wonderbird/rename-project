#!/bin/sh
#
# Create a release on GitHub

echo =====
echo Parsing Project Version
echo =====

# Transform the current version to the release tag name by truncating the
# "-SNAPSHOT" suffix and prepending the artifact id
CURRENT_VERSION=$(mvn org.apache.maven.plugins:maven-help-plugin:2.1.1:evaluate -Dexpression=project.version | grep -Ev '(^\[|Download\w+:)')
RELEASE_VERSION=$(echo $CURRENT_VERSION | sed s/-SNAPSHOT//)

ARTIFACT_ID=$(mvn org.apache.maven.plugins:maven-help-plugin:2.1.1:evaluate -Dexpression=project.artifactId | grep -Ev '(^\[|Download\w+:)')
RELEASE_TAG=$ARTIFACT_ID-$RELEASE_VERSION

echo Release Tag in GitHub: $RELEASE_TAG

echo
echo =====
echo Preparing Release
echo =====

# Use the Maven Release Plugin to create a release non-interactively
# See also: http://maven.apache.org/maven-release/maven-release-plugin/usage.html
mvn -DdryRun=true -DscmCommentPrefix="[skip ci] " --batch-mode release:prepare -Dresume=false
RELEASE_PREPARE_SUCCESS=$?
if [ $RELEASE_PREPARE_SUCCESS -ne 0 ]; then
    mvn -DdryRun=true -DscmCommentPrefix="[skip ci] " release:clean

    echo
    echo "ERROR: Failed to prepare the release '$RELEASE_TAG.'"
    echo "       The release has been cleaned, please fix the error(s) and try again."

    exit 1
fi

echo
echo =====
echo Performing Release
echo =====

mvn -DdryRun=true -DscmCommentPrefix="[skip ci] " release:perform

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
