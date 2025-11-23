package com.spike.springcloud.alibaba.nacos.web;

import com.spike.springcloud.alibaba.nacos.api.NacosConfigClientApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/config")
public class NacosDiscoveryController {

    @Autowired
    private NacosConfigClientApi nacosConfigClientApi;

    @GetMapping("/get")
    public Map<String, Object> get() {
        return nacosConfigClientApi.get();
    }
}
