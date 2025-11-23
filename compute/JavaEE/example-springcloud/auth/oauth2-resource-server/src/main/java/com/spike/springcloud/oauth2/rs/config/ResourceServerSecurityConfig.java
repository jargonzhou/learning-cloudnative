package com.spike.springcloud.oauth2.rs.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class ResourceServerSecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 资源服务器配置
        http.oauth2ResourceServer(rs -> rs
                        // Jwt Bearer token
                        .jwt(Customizer.withDefaults())
                // 身份验证管理器解析器: 支持多个授权服务器
                // .authenticationManagerResolver(JwtIssuerAuthenticationManagerResolver.fromTrustedIssuers(...))
        );

        http.authorizeHttpRequests((authorize) -> authorize
                .requestMatchers(
                        AntPathRequestMatcher.antMatcher("/rs/public"),
                        // /oauth2/authorization/my-oidc-client
                        // /login/oauth2/code/my-oidc-client
                        AntPathRequestMatcher.antMatcher("/favicon.ico"),
                        AntPathRequestMatcher.antMatcher("/error")
                ).permitAll()
                // authority设置: rs.private
                .requestMatchers("/rs/private").hasAuthority("SCOPE_rs.private")
                .anyRequest().authenticated());
        return http.build();
    }
}
