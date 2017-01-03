#!/bin/bash
EUREKA_URL=http://localhost:5000/eureka/
PORT=8080
java -Djalgoarena.apiGatewayUrl=http://localhost:5001/ -classpath "lib/*" -jar jalgoarena-judge-*.jar
