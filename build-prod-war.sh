#!/bin/bash

TMP_DIR=/tmp/bblorganizer
PROJECT_DIR=$(dirname `which $0`)

rm -rf $TMP_DIR

cd $PROJECT_DIR
git clone ./ $TMP_DIR

cd $TMP_DIR/bblorganizer-parent
mvn package -Denv=prod
mv $TMP_DIR/bblorganizer-web/target/bblorganizer-*.war $TMP_DIR/
