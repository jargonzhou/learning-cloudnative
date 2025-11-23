FROM eclipse-temurin:17

# 下载例: https://dlcdn.apache.org/skywalking/java-agent/9.4.0/apache-skywalking-java-agent-9.4.0.tgz
# 配置agent后
COPY ./skywalking-agent /skywalking-agent