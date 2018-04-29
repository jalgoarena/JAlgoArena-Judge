#!/usr/bin/env bash
./gradlew clean
./gradlew stage
pm2 start pm2.config.js