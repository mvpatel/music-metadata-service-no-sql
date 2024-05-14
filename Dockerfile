# Build stage using the official Maven image
FROM maven:latest AS builder
COPY pom.xml /app/
COPY src /app/src/
WORKDIR /app
RUN mvn clean install

# Package stage using a smaller JDK runtime
FROM openjdk:21-jdk
COPY --from=builder /app/target/*.jar /app/app.jar
WORKDIR /app
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
