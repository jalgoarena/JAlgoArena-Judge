#!/bin/bash
EUREKA_URL=http://localhost:5000/eureka/
java -Dserver.port=8080 -Djalgoarena.apiGatewayUrl=http://localhost:5001/ -classpath "lib/*" -jar jalgoarena-judge-*.jar
