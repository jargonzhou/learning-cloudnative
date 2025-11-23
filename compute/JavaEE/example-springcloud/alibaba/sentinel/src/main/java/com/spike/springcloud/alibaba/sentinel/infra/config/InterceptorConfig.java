package com.spike.springcloud.alibaba.sentinel.infra.config;

import com.alibaba.csp.sentinel.adapter.spring.webmvc_v6x.SentinelWebInterceptor;
import com.alibaba.csp.sentinel.adapter.spring.webmvc_v6x.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.adapter.spring.webmvc_v6x.config.SentinelWebMvcConfig;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.PrintWriter;

// sentinel-spring-webmvc-v6x-adapter
// https://github.com/alibaba/Sentinel/blob/1.8/sentinel-adapter/sentinel-spring-webmvc-v6x-adapter/README.md
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Sentinel configuration
        SentinelWebMvcConfig config = new SentinelWebMvcConfig();
        // block exception handler: default DefaultBlockExceptionHandler
        config.setBlockExceptionHandler(new BlockExceptionHandler() {
            @Override
            public void handle(HttpServletRequest request, HttpServletResponse response,
                               String resourceName, BlockException e) throws Exception {
                // Return 429 (Too Many Requests) by default.
                response.setStatus(429);

                PrintWriter out = response.getWriter();
                out.print("Blocked by Sentinel (flow limiting)");
                out.flush();
                out.close();
            }
        });

        // Enable the HTTP method prefix.
        config.setHttpMethodSpecify(true);
        // Add to the interceptor list.
        registry.addInterceptor(new SentinelWebInterceptor(config)).addPathPatterns("/**");
    }
}
