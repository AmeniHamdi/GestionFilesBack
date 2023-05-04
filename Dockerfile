FROM openjdk:17-jdk-alpine
RUN apk add curl
EXPOSE 8089
ADD target/spring-boot-docker.jar spring-boot-docker.jar
RUN curl -u admin:imen12345678 -o csv-0.0.1.jar "http://192.168.56.3:8081/repository/maven-releases/com/example/csv/csv-0.0.1.jar" -L
ENTRYPOINT ["java","-jar","/spring-boot-docker.jar"]

