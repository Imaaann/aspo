FROM openjdk:17-jdk-slim

RUN apt-get update && apt-get install -y --no-install-recommends git

WORKDIR /calculate

#COPY /calculate/GitClone.java .
COPY /calculate/Fetch.java .
COPY /calculate/URL.json .


CMD javac Fetch.java && java Fetch && cd /calculate/Repo && ls -l
