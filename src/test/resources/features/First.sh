#!/bin/bash
#TO RUN THIS SH FILE YOU NEED TO SETUP BELOW TO COMMENT IN .BASHRC FILE
#DESTINATION=/c/Users/$(whoami)/Desktop/Pranav/PostOffice/post-office-agent-rd-automation/src/test/resources/features
#alias run='source '$DESTINATION'/First.sh'
export term=cygwin
echo $PWD
DESTINATION=/c/Users/$(whoami)/Desktop/Pranav/PostOffice/post-office-agent-rd-automation/src/test/resources/features
cd $DESTINATION
cd "$(git rev-parse --show-toplevel)"
./gradlew cucumber -P tags=@first