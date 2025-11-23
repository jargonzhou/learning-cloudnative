package com.spike.springcloud.admin.client.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminClientController {
    @GetMapping("/")
    public String home() {
        return "ok";
    }
}
