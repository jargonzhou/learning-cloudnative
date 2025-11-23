package com.spike.springcloud.alibaba.sentinel.web;

import com.spike.springcloud.alibaba.sentinel.domain.ExampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExampleController {
    @Autowired
    private ExampleService exampleService;

    @GetMapping("/hello/{name}")
    public String sayHello(@PathVariable("name") String name) {
        return exampleService.sayHello(name);
    }

    @GetMapping("/world/{world}")
    public String sayWorld(@PathVariable("world") String world) {
        return exampleService.sayWorld(world);
    }
}
