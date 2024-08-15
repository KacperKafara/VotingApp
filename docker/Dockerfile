FROM maven:3.9.8-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY ../pom.xml pom.xml
RUN mvn clean package -Dmaven.test.skip -Dmaven.main.skip && rm -rf target
COPY ../src src
RUN mvn clean package -Dmaven.test.skip

FROM eclipse-temurin:21.0.2_13-jdk
COPY --from=build /app/target/*.jar /app.jar
CMD ["java", "-jar", "/app.jar"]