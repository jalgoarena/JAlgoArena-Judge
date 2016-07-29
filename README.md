# JAlgoArena [![Build Status](https://travis-ci.org/spolnik/JAlgoArena.svg?branch=master)](https://travis-ci.org/spolnik/JAlgoArena) [![codecov](https://codecov.io/gh/spolnik/JAlgoArena/branch/master/graph/badge.svg)](https://codecov.io/gh/spolnik/JAlgoArena) [![Quality Gate](https://sonarqube.com/api/badges/gate?key=JAlgoArena)](https://sonarqube.com/dashboard/index/JAlgoArena)

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
    
* /problems - get all the problems id
* /problems/{id} - get the problem details
* /problems/{id}/skeletonCode - get the skeleton code for problem
* /problems/{id}/solution (POST) - post the solution

