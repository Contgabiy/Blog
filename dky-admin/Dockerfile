FROM openjdk:8-jdk-slim
ADD target/dky-admin-1.0.jar app.jar
EXPOSE 8989
ENTRYPOINT ["java","-jar","/app.jar"]
