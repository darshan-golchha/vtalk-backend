FROM maven:3.8.3-openjdk-17 AS build
COPY . .
RUN maven clean package -DskipTests

FROM openjdk:17.0.1-jdk-slim
COPY --from=build /target/vtalk-0.0.1-SNAPSHOT.jar vtalk.jar
EXPOSE 8080
ENTRYPOINT [ "java", "-jar", "vtalk.jar" ]
