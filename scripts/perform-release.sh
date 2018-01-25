#!/bin/sh
#
# Create a release on GitHub

DRY_RUN=true
GITHUB_AUTH_TOKEN=

echo
echo =====
echo Publishing Release on GitHub
echo =====

UPLOAD_URL="https://uploads.github.com/repos/wonderbird/rename-project/releases/9379309/assets{?name,label}"
UPLOAD_URL=$(echo $UPLOAD_URL | sed 's/{?name,label}/?name=rename-project-1.0,label=Mac%20OS%20Disk%20Image/')
echo Uploading DMG file to $UPLOAD_URL ...

exit 1

echo Creating draft on GitHub ...
POST_CREATE_RELEASE_RESPONSE=$(curl --data '{"tag_name":"rename-project-1.0","target_commitish":"master","name":"Test Release","body":"I am using this release to test scripted release making","draft":true,"prerelease":true}' --header "authorization: bearer $GITHUB_AUTH_TOKEN" https://api.github.com/repos/wonderbird/rename-project/releases)
POST_CREATE_RELEASE_SUCCESS=$?
UPLOAD_URL=$(echo $POST_CREATE_RELEASE_RESPONSE | jq '.upload_url')

if [ $POST_CREATE_RELEASE_SUCCESS -ne 0 -o "xUPLOAD_URL" == "x" ]; then
    echo
    echo "Response from GitHub:"
    echo $POST_CREATE_RELEASE_RESPONSE | jq '.'
    echo
    echo "ERROR: Failed to draft the release on GitHub."
    exit 1
fi

exit 1

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
mvn -DdryRun=$DRY_RUN -DscmCommentPrefix="[skip ci] " --batch-mode release:prepare -Dresume=false
RELEASE_PREPARE_SUCCESS=$?
if [ $RELEASE_PREPARE_SUCCESS -ne 0 ]; then
    mvn -DdryRun=$DRY_RUN -DscmCommentPrefix="[skip ci] " release:clean

    echo
    echo "ERROR: Failed to prepare the release '$RELEASE_TAG.'"
    echo "       The release has been cleaned, please fix the error(s) and try again."

    exit 1
fi

echo
echo =====
echo Performing Release
echo =====

mvn -DdryRun=$DRY_RUN -DscmCommentPrefix="[skip ci] " release:perform
RELEASE_SUCCESS=$?
if [ $RELEASE_SUCCESS -ne 0 ]; then
    mvn -DdryRun=$DRY_RUN -DscmCommentPrefix="[skip ci] " release:clean

    echo
    echo "ERROR: Failed to prepare the release '$RELEASE_TAG.'"
    echo "       The release has been cleaned, please fix the error(s) and try again."

    exit 1
fi

# TODO: Publish the .dmg file to the GitHub release page
# GraphQL 4 API: https://developer.github.com/v4/
#                https://developer.github.com/v4/guides/using-the-explorer/
# Read: https://developer.github.com/v3/repos/releases/#create-a-release

# In order to test the release, run the following command
#
#mvn -DdryRun=$DRY_RUN release:prepare
#
# Then check the outputs and clean up
#
#mvn release:clean
