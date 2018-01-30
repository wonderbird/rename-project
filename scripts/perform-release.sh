#!/bin/sh
#
# Create a release on GitHub

DRY_RUN=false
SCRIPTNAME=$(basename "$0")

git config --global user.email "stefan.boos@gmx.de"
git config --global user.name "Travis CI"

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

echo
echo =====
echo Publishing Release on GitHub
echo =====

# Regarding the GitHub Access Token inside Travis see
# https://gist.github.com/willprice/e07efd73fb7f13f917ea

# Regarding the GitHub API see
# https://developer.github.com/v3/repos/releases/#create-a-release

echo "Creating draft on GitHub ..."
POST_CREATE_RELEASE_RESPONSE=$(curl -s --data '{"tag_name":"$RELEASE_TAG","target_commitish":"master","name":"Test Release","body":"I am using this release to test scripted release making","draft":true,"prerelease":true}' --header "authorization: bearer $GITHUB_ACCESS_TOKEN" https://api.github.com/repos/wonderbird/rename-project/releases)
POST_CREATE_RELEASE_SUCCESS=$?
RELEASE_ID=$(echo $POST_CREATE_RELEASE_RESPONSE | jq '.id')
UPLOAD_URL=$(echo $POST_CREATE_RELEASE_RESPONSE | jq '.upload_url' | sed 's/"//g')
UPLOAD_URL=$(echo $UPLOAD_URL | sed 's/{?name,label}/?name=RELEASE_TAG.dmg\&label=Mac%20OS%20Disk%20Image/')

POST_CREATE_RELEASE_IS_ERROR=0
if [ $POST_CREATE_RELEASE_SUCCESS -ne 0 -o "$RELEASE_ID" == "null" -o "$UPLOAD_URL" == "null" ]; then
    POST_CREATE_RELEASE_IS_ERROR=1
    echo "Response from GitHub:"
    echo $POST_CREATE_RELEASE_RESPONSE | jq '.'
    echo
    echo "ERROR: Failed to draft the release on GitHub."
fi

echo "Created release with id $RELEASE_ID"

echo "Uploading release artifact to GitHub using $UPLOAD_URL ..."
POST_UPLOAD_RESPONSE=$(curl -s --data-binary '@./target/$RELEASE_TAG.dmg' --header "content-type: application/octet-stream" --header "authorization: bearer $GITHUB_ACCESS_TOKEN" $UPLOAD_URL)
POST_UPLOAD_STATE=$(echo $POST_UPLOAD_RESPONSE | jq '.state' | sed 's/"//g')
POST_UPLOAD_SUCCESS=$?

POST_UPLOAD_IS_ERROR=0
if [ $POST_UPLOAD_SUCCESS -ne 0 -o $POST_UPLOAD_STATE != "uploaded" ]; then
    POST_UPLOAD_IS_ERROR=1
    echo "Response from GitHub:"
    echo $POST_UPLOAD_RESPONSE | jq '.'
    echo
    echo "ERROR: Uploading the release artifact failed."
fi

DELETE_RELEASE_IS_ERROR=0
if [ $POST_CREATE_RELEASE_IS_ERROR -eq 1 -o $POST_UPLOAD_IS_ERROR -eq 1 -o "$DRY_RUN" == "true" ]; then
    echo "There were errors or we are performing a dry run."
    echo "Deleting draft from GitHub ..."
    DELETE_RELEASE_STATUS=$(curl -s -i --header "authorization: bearer $GITHUB_ACCESS_TOKEN" -X DELETE https://api.github.com/repos/wonderbird/rename-project/releases/$RELEASE_ID | grep -E '^Status: ' | sed -E 's/[^0-9]+([0-9]+).*/\1/')
    DELETE_RELEASE_SUCCESS=$?

    if [ $DELETE_RELEASE_SUCCESS -ne 0 -o $DELETE_RELEASE_STATUS != "204" ]; then
        DELETE_RELEASE_IS_ERROR=1
        echo "Status code: $DELETE_RELEASE_STATUS"
        echo
        echo "ERROR: Failed to delete the release draft from GitHub."
    fi
fi

if [ $POST_CREATE_RELEASE_IS_ERROR -eq 1 -o $POST_UPLOAD_IS_ERROR -eq 1 -o $DELETE_RELEASE_IS_ERROR -eq 1 ]; then
    echo "Aborting release process because of error(s)."
    exit 1
fi
