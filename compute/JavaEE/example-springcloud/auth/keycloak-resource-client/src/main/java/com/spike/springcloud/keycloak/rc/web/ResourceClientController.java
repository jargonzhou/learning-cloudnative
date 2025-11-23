package com.spike.springcloud.keycloak.rc.web;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ResourceClientController {
    @GetMapping
    public String home() {
        return "home";
    }

    @GetMapping("private")
    public String privateResource() {
        return "private";
    }

    @GetMapping("/authinfo")
    public Object getAuthentication(
            // 身份认证信息
            OAuth2AuthenticationToken authentication) {
        return authentication;
    }
}
