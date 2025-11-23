package com.spike.security;

import com.spike.security.custom.WithCustomUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest // Spring Boot托管测试Spring上下文
@AutoConfigureMockMvc // 自动配置MockMvc
public class SecurityApplicationTests {
    @Autowired
    private MockMvc mvc;

    @DisplayName("/hello 未身份认证")
    @Test
    public void helloUnauthenticated() throws Exception {
        mvc.perform(
                        // MockMvcRequestBuilders
                        get("/hello"))
                .andExpect(
                        // MockMvcResultMatchers
                        status().isUnauthorized());
    }

    @DisplayName("/hello 身份认证")
    @Test
    @WithMockUser // 模拟已身份验证过的用户
    public void helloAuthenticated() throws Exception {
        mvc.perform(get("/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string("hello"));
    }

    @DisplayName("/hello 身份认证 with user")
    @Test
    public void helloAuthenticatedWithUser() throws Exception {
        mvc.perform(get("/hello").with(
                        // SecurityMockMvcRequestPostProcessors
                        user("mary")))
                .andExpect(status().isOk())
                .andExpect(content().string("hello"));
    }

    @DisplayName("/hello 身份认证 with john")
    @Test
    @WithUserDetails("john") // 使用UserDetailsService中的用户
    public void helloAuthenticatedWithJohn() throws Exception {
        mvc.perform(get("/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string("hello"));
    }

    @DisplayName("/hello 身份认证 with custom user")
    @Test
    @WithCustomUser(username = "mary") // 使用自定义注解
    public void helloAuthenticatedWithCustomUser() throws Exception {
        mvc.perform(get("/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string("hello"));
    }
}
