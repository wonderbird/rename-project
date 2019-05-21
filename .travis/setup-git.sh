#!/bin/bash

# Allow the maven release plugin to push the version tag to GitHub
#
# The following function fixes the error message
#   "fatal: ref HEAD is not a symbolic ref"
# reported by Travis CI.
#
# See also:
#   https://github.com/sbt/sbt-release/issues/210#issuecomment-348210828
function fix_git_head_not_symbolic_ref {
    echo "Fixing git setup for $TRAVIS_BRANCH"
    git checkout ${TRAVIS_BRANCH}
    git branch -u origin/${TRAVIS_BRANCH}
    git config branch.${TRAVIS_BRANCH}.remote origin
    git config branch.${TRAVIS_BRANCH}.merge refs/heads/${TRAVIS_BRANCH}
}

# Setup the git credential store for using the GitHub access token
#
# See also:
#   https://www.appveyor.com/docs/how-to/git-push/
function setup_github_access_token {
   echo "Setting up GitHub access token ..."
   git config --global credential.helper store
   echo "https://$GITHUB_ACCESS_TOKEN:x-oauth-basic@github.com" > $HOME/.git-credentials
   git config --global user.email "stefan.boos@gmx.de"
   git config --global user.name "Travis CI"
}

fix_git_head_not_symbolic_ref
setup_github_access_token