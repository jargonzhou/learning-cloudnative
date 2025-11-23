package com.spike.springcloud.jib;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class JibApplication {
    @GetMapping
    public String hello() {
        return "Hello, Jib!";
    }

    public static void main(String[] args) {
        SpringApplication.run(JibApplication.class, args);
    }
}
