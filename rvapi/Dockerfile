FROM eclipse-temurin:21-jdk-alpine-3.21 AS builder
WORKDIR /app

COPY ./mvnw ./pom.xml ./
COPY ./.mvn/ .mvn/
RUN ./mvnw dependency:go-offline -B

COPY ./src ./src
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine-3.21
WORKDIR /app

RUN apk update

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
EXPOSE 8080

COPY --from=builder /app/target/*.jar app.jar

CMD ["java", "-jar", "app.jar"]