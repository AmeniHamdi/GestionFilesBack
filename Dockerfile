FROM openjdk:17-jdk-alpine
EXPOSE 8089
ADD target/spring-boot-docker.jar spring-boot-docker.jar
RUN curl -u admin:nexus -o spring-boot-docker.jar "http://192.168.56.3:8081/repository/maven-snapshots/com/example/csv/0.0.1-SNAPSHOT/csv-0.0.1-20230322.092913-1.jar" -L
ENTRYPOINT ["java","-jar","/spring-boot-docker.jar"]

