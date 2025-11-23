package com.spike.springcloud.oauth2.rs.web;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class ResourceController {
    @GetMapping("/rs/public")
    public Object getPublicResource() {
        return "public";
    }

    @GetMapping("/rs/private")
    public Object getPrivateResource(Authentication authentication) {
        // authentication: JwtAuthenticationToken
        Object principal = authentication.getPrincipal();
        System.out.println(principal);
        return "private";
    }
}
