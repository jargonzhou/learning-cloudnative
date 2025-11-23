package com.spike.springcloud.oauth2.authz.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.boot.autoconfigure.security.oauth2.server.servlet.OAuth2AuthorizationServerAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

/**
 * <a href="https://docs.spring.io/spring-authorization-server/reference/configuration-model.html">Configuration Model</a>
 *
 * @see OAuth2AuthorizationServerAutoConfiguration
 */
@Configuration
@EnableWebSecurity
public class AuthorizationServerSecurityConfig {
    // 安全过滤器链: 协议端点
    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
                OAuth2AuthorizationServerConfigurer.authorizationServer();

        http.securityMatcher(authorizationServerConfigurer.getEndpointsMatcher()) // 授权端点
                .with(authorizationServerConfigurer, (authorizationServer) ->
                                authorizationServer
                                        // Enable OpenID Connect 1.0: OidcConfigurer
                                        .oidc(Customizer.withDefaults()) // Provider配置, 用户信息, 登出
                        // 元数据端点: authorizationServerMetadataEndpoint
                )
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/error")).permitAll()
                        .anyRequest().authenticated())
                // Redirect to the login page when not authenticated from the authorization endpoint
                .exceptionHandling((exceptions) -> exceptions.defaultAuthenticationEntryPointFor(
                        new LoginUrlAuthenticationEntryPoint("/login"),
                        new MediaTypeRequestMatcher(MediaType.TEXT_HTML)))
        ;

        return http
//                .formLogin(Customizer.withDefaults())
                .build();
    }

    // 安全过滤器链: 身份认证
    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((authorize) -> authorize
                        .anyRequest().authenticated())
                // Form login handles the redirect to the login page from the authorization server filter chain
                .formLogin(Customizer.withDefaults());

        return http.build();
    }

    // 用户服务: 用于身份认证
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails userDetails = User.withDefaultPasswordEncoder()
                .username("user")
                .password("password")
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(userDetails);
    }

    // OAuth2客户端仓库
    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        RegisteredClient oidcClient = RegisteredClient
                // 注册标识
                .withId(UUID.randomUUID().toString())
//                .withId("resource-client-oidc") // registrationId
                // 客户端标识和密钥
                .clientId("resource-client")
                .clientSecret("{noop}secret")
                // 客户端身份认证方法
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                // 授权类型
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE) // 授权码模式
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN) // 刷新token模式
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS) // 客户端凭证模式
                // token设置
//                .tokenSettings(TokenSettings.builder()
//                        // opaque token
//                        .accessTokenFormat(OAuth2TokenFormat.REFERENCE) // 默认: SELF_CONTAINED
//                        .accessTokenTimeToLive(Duration.ofMinutes(10)) // 默认: 5分钟
//                        .build())
                // 重定向URI
                .redirectUri("http://127.0.0.1:18001/login/oauth2/code/my_authorization_server")
                // 登出后跳转URI
                .postLogoutRedirectUri("http://127.0.0.1:18001/")
                // scope
                .scope(OidcScopes.OPENID)
                .scope(OidcScopes.PROFILE)
                .scope("rs.private")
                .clientSettings(ClientSettings.builder()
                        .requireAuthorizationConsent(true) // 需要授权同意
                        .requireProofKey(false) // 不需要PKCE(Proof Key for Code Exchange), RFC 7636
                        .build())
                .build();

        return new InMemoryRegisteredClientRepository(oidcClient);
    }

    // 在access token中添加额外claims
//    OAuth2TokenCustomizer<JwtEncodingContext>

    // access token签名
    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        KeyPair keyPair = generateRsaKey();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }

    // RSA密钥对
    private static KeyPair generateRsaKey() {
        KeyPair keyPair;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
        return keyPair;
    }

    // 解码签名的access token
    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    // 授权服务器设置: 协议端点
    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder()
//                .issuer("http://127.0.0.1:19000")
                .issuer("http://authz.com:19000")
                .multipleIssuersAllowed(false)
                // 授权端点
                .authorizationEndpoint("/oauth2/authorize")
                // 设备授权端点
                .deviceAuthorizationEndpoint("/oauth2/device_authorization")
                // 设备验证端点
                .deviceVerificationEndpoint("/oauth2/device_verification")
                // token端点
                .tokenEndpoint("/oauth2/token")
                // JWK(JSON Web Key) Set端点
                .jwkSetEndpoint("/oauth2/jwks")
                // token撤销端点
                .tokenRevocationEndpoint("/oauth2/revoke")
                // token查看端点
                .tokenIntrospectionEndpoint("/oauth2/introspect")
                // OpenID Connect 1.0 客户端注册端点
                .oidcClientRegistrationEndpoint("/connect/register")
                // OpenID Connect 1.0 用户信息端点
                .oidcUserInfoEndpoint("/userinfo")
                // OpenID Connect 1.0 登出端点
                .oidcLogoutEndpoint("/connect/logout")
                .build();
    }
}
