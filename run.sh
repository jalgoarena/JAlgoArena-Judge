#!/bin/bash
EUREKA_URL=http://localhost:5000/eureka/ PORT=8080 nohup java -classpath "lib/*" -jar jalgoarena-judge-*.jar &
