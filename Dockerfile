#FROM openjdk:21-slim
#COPY build/libs/spring-auth-1.0.jar app.jar
#
#EXPOSE 20020
#ENTRYPOINT ["java", "-jar", "app.jar"]

FROM gradle:8.6.0-jdk21-alpine AS build
WORKDIR /app
COPY . .
RUN gradle clean build -x test

# Runtime Stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/build/libs/spring-auth-1.0.jar app.jar
EXPOSE 20020
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]