FROM maven:3.9.6-eclipse-temurin-21 as Builder

ARG VERSION=0.0.1-SNAPSHOT
WORKDIR .
COPY . .

RUN mvn clean package

FROM eclipse-temurin:21-jre-jammy
WORKDIR .
COPY --from=Builder /./target/*.jar /./application.jar
CMD java -jar /./application.jar
