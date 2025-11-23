FROM eclipse-temurin:17

RUN useradd spring
USER spring

WORKDIR workspace

ARG JAR_FILE=target/docker-0.0.1-SNAPSHOT.jar

COPY ${JAR_FILE} docker.jar

ENTRYPOINT ["java", "-mx128m", "-jar", "docker.jar"]