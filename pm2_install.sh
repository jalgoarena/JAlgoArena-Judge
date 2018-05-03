#!/usr/bin/env bash
pm2 stop judge
pm2 delete judge
./gradlew clean
./gradlew stage
pm2 start pm2.config.js