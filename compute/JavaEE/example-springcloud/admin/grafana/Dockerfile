FROM eclipse-temurin:17-otel

WORKDIR workspace

ARG JAR_FILE=target/grafana-0.0.1-SNAPSHOT.jar

COPY ${JAR_FILE} app.jar

ENV JAVA_TOOL_OPTIONS=-javaagent:/otel-agent/opentelemetry-javaagent-2.14.0.jar
ENTRYPOINT ["java", "-mx128m", "-jar", "app.jar", "-javaagent:/otel-agent/opentelemetry-javaagent-2.14.0.jar"]