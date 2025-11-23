package com.spike.springcloud.oauth2.rc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableWebSecurity
public class ResourceClientSecurityConfig {
    // 使用YAML配置的客户端
    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // OAuth2登录
        http.oauth2Login(Customizer.withDefaults());
        // 登出
        http.logout(logout -> logout
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutSuccessHandler(this.oidcLogoutSuccessHandler()));

        // OAuth2客户端
        http.oauth2Client(Customizer.withDefaults());

        http.authorizeHttpRequests((authorize) -> authorize
                .requestMatchers(
                        // Home不需要身份验证
                        AntPathRequestMatcher.antMatcher("/"),
                        AntPathRequestMatcher.antMatcher("/index.html"),
                        AntPathRequestMatcher.antMatcher("/error"),
                        AntPathRequestMatcher.antMatcher("/favicon.ico"),
                        // 使用客户端凭证模式获取access token
                        AntPathRequestMatcher.antMatcher("/rc/token")
                ).permitAll()
                .anyRequest().authenticated());


        return http.build();
    }

    // HTTP reactive客户端
    @Bean
    public WebClient webClient(OAuth2AuthorizedClientManager authorizedClientManager) {
        ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2Client =
                new ServletOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);
        return WebClient.builder()
                .apply(oauth2Client.oauth2Configuration())
                .build();
    }

    // OAuth2授权的客户端管理器
    @Bean
    public OAuth2AuthorizedClientManager authorizedClientManager(
            // 客户端注册仓库
//            ClientRegistrationRepository clientRegistrationRepository,
            // 授权的客户端仓库
            OAuth2AuthorizedClientRepository authorizedClientRepository) {

        // 客户端provider
        OAuth2AuthorizedClientProvider authorizedClientProvider =
                OAuth2AuthorizedClientProviderBuilder.builder()
                        .authorizationCode() // 授权码模式
                        .refreshToken() // 刷新token模式
                        .clientCredentials() // 客户端凭证模式
                        .build();

        DefaultOAuth2AuthorizedClientManager authorizedClientManager = new DefaultOAuth2AuthorizedClientManager(
                clientRegistrationRepository, authorizedClientRepository);
        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);

        return authorizedClientManager;
    }

    // OpenID Connect 1.0 Client-Initiated Logout
    private LogoutSuccessHandler oidcLogoutSuccessHandler() {
        OidcClientInitiatedLogoutSuccessHandler handler =
                new OidcClientInitiatedLogoutSuccessHandler(this.clientRegistrationRepository);
        handler.setPostLogoutRedirectUri("http://127.0.0.1:18001/");
        return handler;
    }
}
