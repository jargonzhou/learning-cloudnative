package com.spike.springcloud.grafana.web;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class AppController {
    private static final Logger LOG = LoggerFactory.getLogger(AppController.class);

    @GetMapping
    public String ok() {
        // https://logback.qos.ch/manual/mdc.html
        MDC.put("X-Request-ID", UUID.randomUUID().toString());
        LOG.info("Incoming GET request.");
        return "ok";
    }

    @PostMapping
    public String post() {
        LOG.info("Incoming POST request.");
        return "ok";
    }

    @GetMapping("/ex")
    public String ex() {
        LOG.info("Incoming ex request.");
        try {
            throw new RuntimeException("Something bad happened.");
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return "ok";
    }
}
