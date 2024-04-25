FROM eclipse-temurin:17-jdk-alpine as builder

LABEL backend=datsan-rest-api
MAINTAINER vuthehuyvnu@gmail.com

WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN chmod +x -R ./mvnw
RUN ./mvnw dependency:go-offline
COPY ./src ./src
RUN ./mvnw clean install -DskipTests

FROM eclipse-temurin:17-jre-alpine

# Create spring group and add user java to spring group
RUN addgroup -S spring; adduser -S java -G spring
USER java

WORKDIR /if
EXPOSE 8080
COPY --from=builder /app/target/*.jar datsan-rest-api.jar
ENTRYPOINT ["sh", "-c", "java -jar datsan-rest-api.jar"]