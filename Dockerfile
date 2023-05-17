FROM openjdk:17-jdk-alpine
RUN apk add curl
EXPOSE 8087
ADD target/spring-boot-docker.jar spring-boot-docker.jar

ADD dd-java-agent.jar dd-java-agent.jar
#RUN curl -u admin:imen12345678 -o csv-0.0.1.jar "http://192.168.33.10:8081/repository/maven-releases/com/example/csv/csv-0.0.1.jar" -L
ENTRYPOINT ["java","-javaagent","/dd-java-agent.jar","-jar","/spring-boot-docker.jar"]



