#!/bin/zsh
cd "$(dirname "$0")"
java -cp "bin:lib/*" DailyCatFactCommit
