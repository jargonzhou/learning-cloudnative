package com.spike.springcloud.oauth2.rc.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
@RequestMapping("/rc")
public class ResourceAccessController {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebClient webClient;

    @Autowired
    private OAuth2AuthorizedClientManager authorizedClientManager;

    @GetMapping
    public Object home(OAuth2AuthenticationToken authentication) {
        return authentication;
    }

    @GetMapping("/token")
    public Object token() {
        // 授权请求
        OAuth2AuthorizeRequest request = OAuth2AuthorizeRequest
                .withClientRegistrationId("another_client")
                .principal("resource-client")
                .build();
        // 授权
        OAuth2AuthorizedClient client = authorizedClientManager.authorize(request);
        // 返回access token
        return client.getAccessToken();
    }

    @GetMapping("/authinfo")
    public Object getAuthentication(
            // 身份认证信息
            OAuth2AuthenticationToken authentication) {
        return authentication;
    }

    @GetMapping("/private")
    public Object getRSPrivate(@RegisteredOAuth2AuthorizedClient("my_authorization_server")
                               OAuth2AuthorizedClient authorizedClient) {
        // 调用资源服务上资源
        return webClient.get()
                .uri("http://127.0.0.1:18000/rs/private")
                .attributes(ServerOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient(authorizedClient))
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
