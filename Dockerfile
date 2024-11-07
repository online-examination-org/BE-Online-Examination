FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
EXPOSE 8080
COPY target/online-examination-0.0.1-SNAPSHOT.jar online-examination.jar
ENTRYPOINT ["java","-jar","/online-examination.jar"]