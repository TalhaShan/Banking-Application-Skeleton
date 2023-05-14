FROM eclipse-temurin:17.0.3_7-jdk-alpine
VOLUME /tmp
ARG JAR_FILE
COPY target/BankingApplication-*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]