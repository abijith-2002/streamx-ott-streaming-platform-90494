#!/bin/bash
cd /home/kavia/workspace/code-generation/streamx-ott-streaming-platform-90494/android_tv_frontend
./gradlew lint
LINT_EXIT_CODE=$?
if [ $LINT_EXIT_CODE -ne 0 ]; then
   exit 1
fi

