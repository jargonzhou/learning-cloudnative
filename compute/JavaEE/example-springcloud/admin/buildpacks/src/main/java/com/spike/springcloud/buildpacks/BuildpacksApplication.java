package com.spike.springcloud.buildpacks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class BuildpacksApplication {
    @GetMapping
    public String hello() {
        return "Hello, Buildpacks!";
    }

    public static void main(String[] args) {
        SpringApplication.run(BuildpacksApplication.class, args);
    }
}
