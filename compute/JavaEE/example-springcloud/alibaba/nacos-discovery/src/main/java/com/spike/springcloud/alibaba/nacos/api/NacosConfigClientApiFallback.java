package com.spike.springcloud.alibaba.nacos.api;

import java.util.Map;

public class NacosConfigClientApiFallback implements NacosConfigClientApi {
    @Override
    public Map<String, Object> get() {
        return Map.of();
    }
}
