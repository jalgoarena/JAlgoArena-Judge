# Algohub Judge Engine [![Build Status](https://travis-ci.org/spolnik/judge-engine.svg?branch=master)](https://travis-ci.org/spolnik/judge-engine) [![codecov](https://codecov.io/gh/spolnik/judge-engine/branch/master/graph/badge.svg)](https://codecov.io/gh/spolnik/judge-engine)


A modern versatile online judge engine, supporting Java.

## Prerequisites

### 1. JDK 8

These commands should be available in `$PAHT`: `java`, `javac`

### 2. Compile

    ./gradlew

## Run (it starts HTTP Server hosting REST API for accessing judge engine)

    java -jar build/lib/judge-engine-1.0.jar
    
/problems - get all the problems id
/problems/{id} - get the problem details
/problems/{id}/skeletonCode - get the skeleton code for problem
/problems/{id}/solution (POST) - post the solution

