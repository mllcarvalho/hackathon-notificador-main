FROM maven:3.9.9-eclipse-temurin-21-alpine AS builder

WORKDIR /app
COPY . .

RUN mvn clean install -DskipTests

FROM openjdk:21-slim
WORKDIR /opt/app

COPY --from=builder /app/target/*.jar /hackathon-notificador.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/hackathon-notificador.jar"]
