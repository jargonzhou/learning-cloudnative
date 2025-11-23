FROM eclipse-temurin:17

# 下载例: https://repo.maven.apache.org/maven2/io/opentelemetry/javaagent/opentelemetry-javaagent/2.14.0/opentelemetry-javaagent-2.14.0.jar
COPY ./opentelemetry-javaagent-2.14.0.jar /otel-agent/