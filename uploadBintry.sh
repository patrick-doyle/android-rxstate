#!/usr/bin/env bash
./gradlew clean :rx-savestate:build :rx-savestate:bintrayUpload
./gradlew clean :rx2-savestate:build :rx2-savestate:bintrayUpload