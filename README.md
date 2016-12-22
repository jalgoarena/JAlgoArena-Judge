# JAlgoArena [![Build Status](https://travis-ci.org/spolnik/JAlgoArena.svg?branch=master)](https://travis-ci.org/spolnik/JAlgoArena) [![codecov](https://codecov.io/gh/spolnik/JAlgoArena/branch/master/graph/badge.svg)](https://codecov.io/gh/spolnik/JAlgoArena)

A modern versatile Java online judge engine.

## Prerequisites

### 1. JDK 8

These commands should be available in `$PAHT`: `java`, `javac`

### 2. Compile

    ./gradlew

## Run (it starts HTTP Server hosting REST API for accessing judge engine)

    java -jar build/lib/judge-engine-1.0.jar
    or
    ./gradlew bootRun
