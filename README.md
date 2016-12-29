# JAlgoArena Judge Agent [![Build Status](https://travis-ci.org/spolnik/JAlgoArena-Judge.svg?branch=master)](https://travis-ci.org/spolnik/JAlgoArena-Judge) [![codecov](https://codecov.io/gh/spolnik/JAlgoArena-Judge/branch/master/graph/badge.svg)](https://codecov.io/gh/spolnik/JAlgoArena-Judge)

JAlgoArena Judge Agent is heart of JAlgoArena platform. It is responsible for generation of skeleton code as well as judging submissions based on requirements specified in problem definiton.

Demo: https://jalgoarena-ui.herokuapp.com/

- [Introduction](#introduction)
- [Components](#components)
- [Continuous Delivery](#continuous-delivery)
- [Infrastructure](#infrastructure)
- [Notes](#notes)

## Introduction

- JAlgoArena Judge Agent can be easily scalable - it's stateless and together with Eureka and API using Ribbon load balancer gives unlimited way to scale judgement capability
- It supports Kotlin and Java
- JAlgoArena Judge Agent is generating skeleton code for particular problem - based on meta data received from Problems Service
- It juges correctnes of the solution based on the pre-specified test cases as well as time and memory limits
- It's using simple heuristic to calculate time and memory results - running in couple iteration and looking for best results makes the judgement itself more predictable and repeatable

![Component Diagram](https://github.com/spolnik/JAlgoArena/raw/master/design/component_diagram.png)

## Components

- [JAlgoArena UI](https://github.com/spolnik/JAlgoArena-UI)
- [JAlgoArena Judge](https://github.com/spolnik/JAlgoArena-Judge)
- [JAlgoArena Problems](https://github.com/spolnik/JAlgoArena-Problems)
- [JAlgoArena Eureka Server](https://github.com/spolnik/JAlgoArena-Eureka)
- [JAlgoArena API Gateway](https://github.com/spolnik/JAlgoArena-API)

## Continuous Delivery

- initially, developer push his changes to GitHub
- in next stage, GitHub notifies Travis CI about changes
- Travis CI runs whole continuous integration flow, running compilation, tests and generating reports
- coverage report is sent to Codecov
- application is deployed into Heroku machine

## Infrastructure

- Heroku (PaaS)
- Kotlin Compiler (K2JVMCompiler), MemoryClassLoader, MemoryJavaCompiler
- Spring Boot, Spring Cloud (Eureka Client)
- TravisCI - https://travis-ci.org/spolnik/JAlgoArena-Judge

## Notes
- [Running locally](https://github.com/spolnik/jalgoarena/wiki)
- [Travis Builds](https://travis-ci.org/spolnik)

![Component Diagram](https://github.com/spolnik/JAlgoArena/raw/master/design/JAlgoArena_Logo.png)
