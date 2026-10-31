FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src src
RUN mvn -q -DskipTests package
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/ratesrisk-1.0.0.jar app.jar
RUN useradd --system --uid 10001 appuser
USER 10001
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
