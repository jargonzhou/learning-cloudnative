package com.spike.security.service;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class NameService {
    private Map<String, List<String>> secretNames = Map.of(
            "natalie", List.of("Energico", "Perfecto"),
            "emma", List.of("Fantastico")
    );


    @PreAuthorize("hasAuthority('write'") // 方法层前置授权
    public String getName() {
        return "Fantastico";
    }

    @PreAuthorize("#name == authentication.principal.username") // 方法层前置授权: 使用方法参数
    public List<String> getSecretNames(String name) {
        return secretNames.get(name);
    }
}
