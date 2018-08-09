# JAlgoArena Judge Agent [![Build Status](https://travis-ci.org/jalgoarena/JAlgoArena-Judge.svg?branch=master)](https://travis-ci.org/jalgoarena/JAlgoArena-Judge) [![codecov](https://codecov.io/gh/spolnik/JAlgoArena-Judge/branch/master/graph/badge.svg)](https://codecov.io/gh/spolnik/JAlgoArena-Judge) [![GitHub release](https://img.shields.io/github/release/spolnik/jalgoarena-judge.svg)]()

JAlgoArena Judge Agent is heart of JAlgoArena platform. It is responsible for generation of skeleton code as well as judging submissions based on requirements specified in problem definition.

- [Introduction](#introduction)
- [API](#api)
- [Running Locally](#running-locally)
- [Notes](#notes)

## Introduction

- JAlgoArena Judge Agent can be easily scalable - it's stateless and together with Consul and API using Ribbon load balancer gives unlimited way to scale judgement capability
- It supports Java 8
- JAlgoArena Judge Agent is generating skeleton code for particular problem - based on meta data received from Problems Service
- It judges correctness of the solution based on the pre-specified test cases as well as time and memory limits
- It's using simple heuristic to calculate time and memory results - running in couple iteration and looking for best results makes the judgement itself more predictable and repeatable

![Component Diagram](https://github.com/spolnik/JAlgoArena-Judge/raw/master/design/component_diagram.png)

# REST API

| Endpoint | Description |
| ---- | --------------- |
| [GET /problems](#rest-api) | Get problems list |
| [GET /problems/:id](#rest-api) | Get problem by id |

## API

#### Get all problems

  _Request definition of all problems_

|URL|Method|
|---|------|
|_/problems_|`GET`|

* **Success Response:**

  _List of available problems_

  * **Code:** 200 <br />
    **Content:** `[{"id":"fib","title":"Fibonacci","description":"<description>","timeLimit":1,"skeletonCode":"<skeleton code>","level":1},{...},...]`

* **Sample Call:**

  ```bash
  curl --header "Accept: application/json" \
       http://localhost:5008/problems
  ```

#### Get specific problem

  _Request definition of requested problem_

|URL|Method|
|---|------|
|_/problems/:id_|`GET`|

* **Success Response:**

  _Details of requested problem_

  * **Code:** 200 <br />
    **Content:** `{"id":"fib","title":"Fibonacci","description":"<description>","timeLimit":1,"skeletonCode":"<skeleton code>","level":1}`

* **Sample Call:**

  ```bash
  curl --header "Accept: application/json" \
       http://localhost:5008/problems/fib
  ```

## Running locally

There are two ways to run it - from sources or from binaries.
- Default port: `8080`

### Running from binaries
- go to [releases page](https://github.com/spolnik/JAlgoArena-Judge/releases) and download last app package (JAlgoArena-Judge-[version_number].zip)
- after unpacking it, go to folder and run `./run.sh`
- you can modify port in run.sh script, depending on your infrastructure settings. The script itself can be found in here: [run.sh](run.sh)

### Running from sources
- run `git clone https://github.com/spolnik/JAlgoArena-Judge` to clone locally the sources
- now, you can build project with command `./gradlew clean bootRepackage` which will create runnable jar package with app sources. Next, run `java -Dserver.port=8080 -classpath "lib/*" -jar build/libs/jalgoarena-auth-*.jar` which will start application
- there is second way to run app with gradle. Instead of running above, you can just run `./gradlew clean bootRun`

## Notes
- [Travis Builds](https://travis-ci.org/jalgoarena)

![Component Diagram](https://github.com/spolnik/JAlgoArena/raw/master/design/JAlgoArena_Logo.png)
