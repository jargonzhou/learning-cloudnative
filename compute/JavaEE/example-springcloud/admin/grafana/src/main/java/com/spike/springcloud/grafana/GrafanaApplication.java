package com.spike.springcloud.grafana;

import com.spike.springcloud.grafana.web.filter.RequestLoggingFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

// ops: https://github.com/jargonzhou/application-store/tree/main/ops/grafana
@SpringBootApplication
public class GrafanaApplication {
    public static void main(String[] args) {
        SpringApplication.run(GrafanaApplication.class, args);
    }

    // 记录请求日志
    // ContentCachingRequestWrapper does not cache the request body content.
    @Bean
    public RequestLoggingFilter logFilter() {
        RequestLoggingFilter filter = new RequestLoggingFilter();
        filter.setIncludeQueryString(true);
        filter.setIncludePayload(true);
        filter.setMaxPayloadLength(10000);
        filter.setIncludeHeaders(true);
        filter.setAfterMessagePrefix("REQUEST DATA: ");
        return filter;
    }
}
