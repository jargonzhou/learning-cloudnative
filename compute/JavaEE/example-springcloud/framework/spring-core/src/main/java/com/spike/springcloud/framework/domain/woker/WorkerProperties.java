package com.spike.springcloud.framework.domain.woker;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@Data
@ConfigurationProperties(prefix = IWorker.CONFIG_PREFIX)
public class WorkerProperties {

    private Map<String, WorkerConfig> configs;

    @Data
    public static class WorkerConfig {
        private String name;
        private String type;
        private Boolean enabled;
        private Map<String, Object> params;

    }
}
