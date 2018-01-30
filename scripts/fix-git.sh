#!/usr/bin/env bash

# Force Travis CI to non-shallow git cloning
# https://github.com/sbt/sbt-release/issues/210
function fix_git {
    echo "Fixing git setup for $TRAVIS_BRANCH"
    git checkout ${TRAVIS_BRANCH}
    git branch -u origin/${TRAVIS_BRANCH}
    git config branch.${TRAVIS_BRANCH}.remote origin
    git config branch.${TRAVIS_BRANCH}.merge refs/heads/${TRAVIS_BRANCH}
}
