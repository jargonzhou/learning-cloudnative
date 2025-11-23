package com.spike.springcloud.keycloak;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.keycloak.admin.client.Keycloak;

public class TestKeycloak {

    // 使用Keycloak管理客户端模拟获取accessToken
    @Test
    public void testGetAliceToken() {
        String user = "alice";
        String password = "alice";
        String accessToken = this.getToken(user, password);
        System.out.println(accessToken);

        user = "jdoe";
        password = "jdoe";
        accessToken = this.getToken(user, password);
        System.out.println(accessToken);
    }

    private String getToken(String user, String password) {
        Keycloak keycloak = Keycloak.getInstance("http://authz.com:19001",
                "myrealm",
                user,
                password,
                "authz-servlet",
                "rIrYFaKcJ8uemfikX8eoEIE2Vr6ZrD9M");
        String accessToken = keycloak.tokenManager().getAccessTokenString();
        Assertions.assertNotNull(accessToken);
        return accessToken;
    }
}
