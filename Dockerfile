FROM openjdk:8-alpine

MAINTAINER Jacek Spolnik <jacek.spolnik@gmail.com>

WORKDIR /app
ADD build/libs/jalgoarena-judge-*.jar /app/
ADD lib/* /app/lib/

RUN mkdir -p /app/build/classes/kotlin/main/com/jalgoarena/type
ADD build/classes/kotlin/main/com/jalgoarena/type/* /app/build/classes/kotlin/main/com/jalgoarena/type/

RUN mkdir -p /app/build/resources/main/extensions
ADD build/resources/main/extensions/* /app/build/resources/main/extensions/

EXPOSE 5008

CMD java -Xms1g -Xmx2g -classpath "/app/lib/*" -jar /app/jalgoarena-judge-*.jar