FROM openjdk:8

WORKDIR /app
ADD build/libs/jalgoarena-judge-*.jar /app/
ADD lib/* /app/lib/

RUN mkdir -p /app/build/classes/kotlin/main/com/jalgoarena/type
ADD build/classes/kotlin/main/com/jalgoarena/type/* /app/build/classes/kotlin/main/com/jalgoarena/type/

RUN mkdir -p /app/build/resources/main
ADD build/resources/main/* /app/build/resources/main/

ENV EUREKA_URL=http://eureka:5000/eureka
ENV BOOTSTRAP_SERVERS=kafka1:9092,kafka2:9093,kafka3:9094
EXPOSE 5008

CMD java -Dserver.port=5008 -jar /app/jalgoarena-judge-*.jar