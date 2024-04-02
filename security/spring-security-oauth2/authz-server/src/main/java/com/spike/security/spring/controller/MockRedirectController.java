package com.spike.security.spring.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class MockRedirectController {

    @GetMapping("/authorized")
    public Map<String, Object> authorized(
            @RequestParam("code") String code) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", code);
        return result;
    }
}
