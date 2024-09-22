ARG MAVEN_IMAGE=maven:3.8.6-openjdk-11
ARG JAVA_IMAGE=openjdk:11-jre-slim

#
# Build stage
#
FROM ${MAVEN_IMAGE} AS build

ENV PROJECT_DIR=/home/app
RUN mkdir -p $PROJECT_DIR
WORKDIR $PROJECT_DIR

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

#
# Package stage
#
FROM ${JAVA_IMAGE}

ENV PROJECT_DIR=/home/app

COPY --from=build $PROJECT_DIR/target/vacation-pay-0.0.1-SNAPSHOT.jar /usr/local/lib/vacation-pay.jar

CMD java -jar /usr/local/lib/vacation-pay.jar