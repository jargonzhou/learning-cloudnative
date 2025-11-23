FROM eclipse-temurin:17-skywalking

WORKDIR workspace

ARG JAR_FILE=target/skywalking-0.0.1-SNAPSHOT.jar

COPY ${JAR_FILE} app.jar

# exec form does NOT support variable substitution
# https://docs.docker.com/reference/dockerfile/#variable-substitution
#ENTRYPOINT ["java", "-javaagent:/skywalking-agent/skywalking-agent.jar", "-mx128m", "-jar", "app.jar"]

ENV JAVA_TOOL_OPTIONS=-javaagent:/skywalking-agent/skywalking-agent.jar
ENTRYPOINT ["java", "-mx128m", "-jar", "app.jar"]
