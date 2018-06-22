FROM openjdk:8

MAINTAINER Jacek Spolnik <jacek.spolnik@gmail.com>

WORKDIR /app
ADD build/libs/jalgoarena-judge-*.jar /app/
ADD lib/* /app/lib/

RUN mkdir -p /app/build/classes/kotlin/main/com/jalgoarena/type
ADD build/classes/kotlin/main/com/jalgoarena/type/* /app/build/classes/kotlin/main/com/jalgoarena/type/

RUN mkdir -p /app/build/resources/main
ADD build/resources/main/* /app/build/resources/main/

EXPOSE 5008

CMD java -jar /app/jalgoarena-judge-*.jar