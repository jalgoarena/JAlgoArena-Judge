# Algohub Judge Engine [![Build Status](https://travis-ci.org/spolnik/judge-engine.svg?branch=master)](https://travis-ci.org/spolnik/judge-engine) [![codecov](https://codecov.io/gh/spolnik/judge-engine/branch/master/graph/badge.svg)](https://codecov.io/gh/spolnik/judge-engine)


A modern versatile online judge engine, supporting Java.

## Prerequisites

### 1. JDK 8

These commands should be available in `$PAHT`: `java`, `javac`

### 2. Compile

    gradle shadowJar

## Run

    java -jar build/lib/judge-engine-1.0.jar src/test/resources/problems/2-sum/2-sum.json src/test/resources/problems/2-sum/solution.java

