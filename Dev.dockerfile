FROM eclipse-temurin:24-jdk

RUN apt-get update && \
    apt-get install -y git make maven