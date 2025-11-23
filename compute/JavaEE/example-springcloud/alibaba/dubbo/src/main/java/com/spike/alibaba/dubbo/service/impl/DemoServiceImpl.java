package com.spike.alibaba.dubbo.service.impl;

import com.spike.alibaba.dubbo.service.DemoService;

public class DemoServiceImpl implements DemoService {
    @Override
    public String sayHello(String name) {
        return "Hello " + name + ", response from provider.";
    }
}
