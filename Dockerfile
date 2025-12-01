FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -q -Dmaven.test.skip=true package
FROM amazoncorretto:21-alpine-jdk
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 1010
ENTRYPOINT ["java", "-jar", "app.jar"]