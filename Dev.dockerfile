FROM eclipse-temurin:21-jdk

RUN apt-get update && \
    apt-get install -y git make maven