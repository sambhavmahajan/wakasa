FROM maven:4.0.0-rc-7-eclipse-temurin-25-alpine as builder
WORKDIR /app
COPY . /app
RUN mvn clean install -DskipTests

FROM eclipse-temurin:25-jre-ubi10-minimal as final
WORKDIR /app
COPY --from=builder /app/target/Wakasa-1.0.jar /app/app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]