package com.spike.security.config;

import com.spike.security.repository.DocumentRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity // 开启Web安全
@EnableMethodSecurity // 开启方法安全: 也可以开启@RolesAllowed, @Secured
public class SecurityConfig {

    // filters: org.springframework.security.web.DefaultSecurityFilterChain.getFilters
    // example:
    //0 = {DisableEncodeUrlFilter@11054}
    //1 = {WebAsyncManagerIntegrationFilter@11055}
    //2 = {SecurityContextHolderFilter@11056}
    //3 = {HeaderWriterFilter@11057}
    //4 = {CorsFilter@11058}
    //5 = {LogoutFilter@11059}
    //6 = {BasicAuthenticationFilter@11060}
    //7 = {RequestCacheAwareFilter@11061}
    //8 = {SecurityContextHolderAwareRequestFilter@11062}
    //9 = {AnonymousAuthenticationFilter@11063}
    //10 = {ExceptionTranslationFilter@11064}
    //11 = {AuthorizationFilter@11065}
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // HTTP Basic身份认证
        http.httpBasic(Customizer.withDefaults());

        // 身份验证提供者
        http.authenticationProvider(new CustomAuthenticationProvider(this.userDetailsService(), this.passwordEncoder()));

        // 禁用CSRF
        http.csrf(AbstractHttpConfigurer::disable);

        // 开启CORS
        final CorsConfigurationSource corsSource = request -> {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowedOrigins(List.of("*"));
            config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
            config.setAllowedHeaders(List.of("*"));
            return config;
        };
        http.cors(cors -> cors.configurationSource(corsSource));

        // 授权
        final String expression = """
                hasAuthority('READ') and
                !hasAuthority('DELETE')
                """;
        http.authorizeHttpRequests(c -> {
//                    c.anyRequest().hasAuthority("WRITE");
//                    c.anyRequest().hasAnyAuthority("READ", "WRITE");
//                    c.anyRequest().access(AuthorityAuthorizationManager.hasAuthority("WRITE"));
                    // 使用SpEL
//                    c.anyRequest().access(new WebExpressionAuthorizationManager(expression));

                    // 使用角色
//                    c.anyRequest().hasRole("ADMIN");

                    // 拒绝所有
//                    c.anyRequest().denyAll();
                    // 允许所有
//                    c.anyRequest().permitAll();

                    // 具体路径上的授权约束
//                    c.requestMatchers("/hello").hasRole("ADMIN");

                    // 需要身份验证过
                    c.anyRequest().authenticated();
                }
        );

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        // 使用内存中用户详情管理器
        InMemoryUserDetailsManager result = new InMemoryUserDetailsManager();

        result.createUser(User.withUsername("john")
                .password("123456")
                .authorities("READ", "ROLE_ADMIN") // 权限, 角色(ROLE_)
                .build());
        result.createUser(User.withUsername("jane")
                .password("123456")
                .authorities("WRITE")
                .roles("MANAGER")
                .build());

        // 方法层授权用
        result.createUser(User.withUsername("natalie")
                .password("123456")
                .authorities("read")
                .build());
        result.createUser(User.withUsername("emma")
                .password("123456")
                .authorities("write")
                .build());

        // 方法层过滤用
        result.createUser(User.withUsername("nikolai")
                .password("123456")
                .authorities("read")
                .build());
        result.createUser(User.withUsername("julien")
                .password("123456")
                .authorities("write")
                .build());

        return result;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // 密码不加密, 调试用
        return NoOpPasswordEncoder.getInstance();
    }

    // 方法层安全表达式处理器
    @Bean
    public MethodSecurityExpressionHandler expressionHandler(DocumentRepository documentRepository) {
        var result = new DefaultMethodSecurityExpressionHandler();
        result.setPermissionEvaluator(new DocumentsPermissionEvaluator(documentRepository));
        return result;
    }
}
