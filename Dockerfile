# base image
FROM openjdk:18-alpine3.15

# metadata
LABEL MAINTAINER="sumit@gaurs.net"

# workspace
WORKDIR /opt/app/docker-sb-demo

# add the jar
ADD ./target/sb-mongo-db-0.0.1-SNAPSHOT.jar app.jar

# expose port
EXPOSE 8080

# execute the jar
ENTRYPOINT java -Xms1G -Xmx1536m -jar /opt/app/docker-sb-demo/app.jar
