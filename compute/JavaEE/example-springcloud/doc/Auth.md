# Authentication, Authorization

# oauth2-authorization-server

- OpenID配置端点

```
curl --request GET \
  --url http://authz.com:19000/.well-known/openid-configuration
```

- 授权码模式

```shell
# 1. authorize
# 浏览器中
# http://authz.com:19000/oauth2/authorize?response_type=code&client_id=resource-client&scope=openid%20profile%20rs.private&redirect_uri=http%3A%2F%2F127.0.0.1%3A8080%2Fauthorized&code_challenge=bL1nmJNEXeT7XRbwK8AvMA0VbgpZTNMXS6UPNhPzBhY&code_challenge_method=S256
http://authz.com:19000/oauth2/authorize?
response_type=code
&client_id=resource-client
&scope=openid+profile+rs.private
&redirect_uri=http%3A%2F%2F127.0.0.1%3A8080%2Fauthorized
&code_challenge=bL1nmJNEXeT7XRbwK8AvMA0VbgpZTNMXS6UPNhPzBhY # code验证器的SHA256消息摘要
&code_challenge_method=S256

# 2. submit consent
# 3. return code: <code>
http://127.0.0.1:8080/authorized?code=M-FbcFkDXpP8SYp0WjrPKxnVrTJGoDxS4DvVu0WpJ49uKFtHfRmgWiHQK-Dp2yCWkPM5rZ-WNsy7O-Iz7t7ep2ONoZax3dQQqPnLHqBYqJE589OvCSs6UCslUE4KLwRl

# 4. get token
curl --request POST \
  --url http://authz.com:19000/oauth2/token \
  --header 'Authorization: Basic cmVzb3VyY2UtY2xpZW50OnNlY3JldA==' \
  --header 'Content-Type: multipart/form-data' \
  --form code=<code> \
  --form grant_type=authorization_code \
  --form redirect_uri=http://127.0.0.1:8080/authorized \
  --form code_verifier=yIUCX7PqcqNwT7jI_ueY2b_rKSGs1IQ1JAbOPe75Fj8 # code验证器
# 响应:  
{
  # <access_token>
  "access_token": "eyJraWQiOiJiYTQwNjdiYi04NDkyLTRhODEtYjY4ZC0xMDE2MzAxZDRkMWMiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiYXVkIjoicmVzb3VyY2UtY2xpZW50IiwibmJmIjoxNzQwNjIyOTA0LCJzY29wZSI6WyJvcGVuaWQiLCJwcm9maWxlIiwicnMucHJpdmF0ZSJdLCJpc3MiOiJodHRwOi8vMTI3LjAuMC4xOjE5MDAwIiwiZXhwIjoxNzQwNjIzMjA0LCJpYXQiOjE3NDA2MjI5MDQsImp0aSI6IjE4ODU5MjdmLWUxYjgtNDgxNi1iNWZlLTQxYTBlYjEyNDZjNiJ9.GcOV9MZS7IlfsM4FQclSiquRvsvMPVtKnBy1iuJYZcZX-LBkbJK7uK7p2MVgtpwBJyaj3uKL8MWHuWz3VbJDvcRswnTRMFuQIWA12-QO3xp8jvW8P4MwM6l9ajmOSUmMcHOSIKERl4KULQUDEQHDNpmkhK3lFjOJZFIujdNIm7YezFNNnPceANDTyqjHa8ntNid9v92XBTPEsaeEKkpLwrVehwpo24SvvxcLSxG1x82WDmYL8tyywmDmbW5BxrdhF8IUjT-KbnnhcTiYzGDxPAZTAecVcxATO3rtD01qZhJMNmLHCBARgwIuL7-Pq8RLE7rWXR37xVI1AC26GrAdGA",
  # <refresh_token>
  "refresh_token": "DyKb3qTr66x4ZRVn2biWQkuMGUKe0jszpIUB-sl8bb7oDi7CodQOqtc1O8XUYQHuxPwwNZVoQis4XjkIwtxWHsLMGzIR5WSfqsVfwkBhNBigX5jwvtXhT1AfGy8Afll5",
  "scope": "openid profile rs.private",
  "id_token": "eyJraWQiOiJiYTQwNjdiYi04NDkyLTRhODEtYjY4ZC0xMDE2MzAxZDRkMWMiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiYXVkIjoicmVzb3VyY2UtY2xpZW50IiwiYXpwIjoicmVzb3VyY2UtY2xpZW50IiwiYXV0aF90aW1lIjoxNzQwNjIyODcwLCJpc3MiOiJodHRwOi8vMTI3LjAuMC4xOjE5MDAwIiwiZXhwIjoxNzQwNjI0NzA0LCJpYXQiOjE3NDA2MjI5MDQsImp0aSI6IjA4Njc4ZmExLWZmMTUtNDZiZS04NTA0LTA4ZGMyOGZmMWY1YSIsInNpZCI6IjZkQmRMWHRydng2bm1taXB4a3YxYk5MbXR6RDlFM05KdUdFTHFnWUVPRTgifQ.hYJZJICWjil9f7WD8tJ_3fn5d-5AyUYa6IjBMAZ5LiG1sffkYUUdYPhNc46W6qVwUOYpywjXH_o1XCclQvp18_8zo8k9_S2p5noLKy55WM91USusrQtY2wjdDDonc7h6hO8LrzzR6OeiiGhgWXaZZByu8uD_TYzm-6g0lUpDYhxQwWxUlHmdvHRFS-nl8bXiTQvZj8BYBVpYXjy4dlv0rifWOzjpENGUpYp5s1ivmGESaiKKSUzirq4L-rg6EvTpoWm_J820GSowo746daWsVdzTkGZLk3fMoA4oZmSbfGJXhy97SpKP7yJToZZVATPN-98_ikw9iDqXkdHIN9nF6A",
  "token_type": "Bearer",
  "expires_in": 300
}
```

- 刷新token模式

```shell
curl --request POST \
  --url http://authz.com:19000/oauth2/token \
  --header 'Authorization: Basic cmVzb3VyY2UtY2xpZW50OnNlY3JldA==' \
  --header 'Content-Type: multipart/form-data' \
  --form refresh_token=<refresh_token> \
  --form grant_type=refresh_token
# 响应:
{
  # <access_token> 
  "access_token": "eyJraWQiOiJiYTQwNjdiYi04NDkyLTRhODEtYjY4ZC0xMDE2MzAxZDRkMWMiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiYXVkIjoicmVzb3VyY2UtY2xpZW50IiwibmJmIjoxNzQwNjIyOTQ4LCJzY29wZSI6WyJvcGVuaWQiLCJwcm9maWxlIiwicnMucHJpdmF0ZSJdLCJpc3MiOiJodHRwOi8vMTI3LjAuMC4xOjE5MDAwIiwiZXhwIjoxNzQwNjIzMjQ4LCJpYXQiOjE3NDA2MjI5NDgsImp0aSI6IjA1MzU3MmE1LTI3NzYtNDIwMS1iZjc4LWNjOGM1ZWNiN2NiZCJ9.a8A0Dgrbs-xp5H6XGCWcWxDIiazh8E1K7WKY0WVIS50EfBq_n4WUPFgSBTJsJ-w86s6ZEkdJHyUHUko_oiGtL-ev9VIwnprPHm8cVevQsd1Q93liB3zrhXLz5IZMVfYSujPr3XsBCSkfIf7PSvd3ttzhoq4-9Diz64TEh1K8_bYax6F5IE7dUIcz_v2XzbUWDJEvIFWBr0AaoGqkatUfBpWlXqPe_sVNKQNQE2mVf6EZ8zcJKfe7ljasvSy-286R0D_yVUasuuVDMc98qxjJ-ADpC2qJ6cUt5umylNKsVAjap-8HvcNG2faDIO22ROGvPbhktAd4wb_SMcQAphub_A",
  "refresh_token": "DyKb3qTr66x4ZRVn2biWQkuMGUKe0jszpIUB-sl8bb7oDi7CodQOqtc1O8XUYQHuxPwwNZVoQis4XjkIwtxWHsLMGzIR5WSfqsVfwkBhNBigX5jwvtXhT1AfGy8Afll5",
  "scope": "openid profile rs.private",
  "id_token": "eyJraWQiOiJiYTQwNjdiYi04NDkyLTRhODEtYjY4ZC0xMDE2MzAxZDRkMWMiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiYXVkIjoicmVzb3VyY2UtY2xpZW50IiwiYXpwIjoicmVzb3VyY2UtY2xpZW50IiwiYXV0aF90aW1lIjoxNzQwNjIyODcwLCJpc3MiOiJodHRwOi8vMTI3LjAuMC4xOjE5MDAwIiwiZXhwIjoxNzQwNjI0NzQ4LCJpYXQiOjE3NDA2MjI5NDgsImp0aSI6IjE5ZDU3ZDRkLWExN2EtNGJkMi1hMjA3LTRmOGYzMDIyN2E1YyIsInNpZCI6IjZkQmRMWHRydng2bm1taXB4a3YxYk5MbXR6RDlFM05KdUdFTHFnWUVPRTgifQ.W5kgBUXbMNJGmKlS-Ji7C_FMXuYg3BS2A280KwqvVu_uL11eZSYa_45fEhlv9QTpQ87bwRgRh-zz1OdQacYLACkfEpJYrh9qXFBS6u6EyPK6duXV0nvJO-Tkinlh7tEpIzhpjxS8TLhrLySJ1P_EdOEYaldPX62sed4Z3Fp9oHicXTpBVVdkz8cUnz4fTZsqFhKvnq9uiSZvEZmBVfxP8zZmHIJ6nD30qkmR3IZBLUCTIdmWDQLM71N8Odw2ERbVGhf9ebA3QxJff3vnX6ojiyJD_Ar3gTd8uIsBA0uKMchahgW3n94z3BKes3XDQMBaTZdufRMHMMURcwzE81LEhQ",
  "token_type": "Bearer",
  "expires_in": 300
}
```

- token查看

```
curl --request POST \
  --url http://authz.com:19000/oauth2/introspect \
  --header 'Authorization: Basic cmVzb3VyY2UtY2xpZW50OnNlY3JldA==' \
  --header 'Content-Type: application/x-www-form-urlencoded' \
  --data token=<access_token>
# 响应:
{
  "active": true,
  "sub": "user",
  "aud": [
    "resource-client"
  ],
  "nbf": 1740622948,
  "scope": "openid profile rs.private",
  "iss": "http://authz.com:19000",
  "exp": 1740623248,
  "iat": 1740622948,
  "jti": "053572a5-2776-4201-bf78-cc8c5ecb7cbd",
  "client_id": "resource-client",
  "token_type": "Bearer"
}
```

- 撤销token

```shell
curl --request POST \
  --url http://authz.com:19000/oauth2/revoke \
  --header 'Authorization: Basic cmVzb3VyY2UtY2xpZW50OnNlY3JldA==' \
  --header 'Content-Type: application/x-www-form-urlencoded' \
  --data token=<access_token>
# 响应
200
```

- 客户端凭证模式

```shell
curl --request POST \
  --url http://authz.com:19000/oauth2/token \
  --header 'Authorization: Basic cmVzb3VyY2UtY2xpZW50OnNlY3JldA==' \
  --header 'Content-Type: multipart/form-data' \
  --form grant_type=client_credentials \
  --form redirect_uri=http://127.0.0.1:8080/authorized
# 响应
{
  "access_token": "eyJraWQiOiJiYTQwNjdiYi04NDkyLTRhODEtYjY4ZC0xMDE2MzAxZDRkMWMiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJyZXNvdXJjZS1jbGllbnQiLCJhdWQiOiJyZXNvdXJjZS1jbGllbnQiLCJuYmYiOjE3NDA2MjQwNzUsImlzcyI6Imh0dHA6Ly8xMjcuMC4wLjE6MTkwMDAiLCJleHAiOjE3NDA2MjQzNzUsImlhdCI6MTc0MDYyNDA3NSwianRpIjoiZDZhNWI4Y2EtZDJlYi00NmJjLWE5MGItNzYzYzIxMzUxZDYwIn0.Rkp_Nj7CtIhrMHYlI4LRgBcj49c1wmXZKbAC8mgeC4ySuMdWH2oVXTvu8d3vgV3GZ-OWsyV5EamJ3rlfngN53JLMGzD3Nl5268wtLCz06FPLT9bxFv9Ve6n2rzZBQJA7tKY00fpMPkbPiS4Z52XtikI4QKFizNagcMiuO8e8RNVDIQje5JanvYfjthcWBKL15rt6O5HnrvuYwOTcgyZyOvnsbUXHSuJigLbGI4UdnxMWnj4Dcdh5UgsDvqqPbzsn1KJwI2L00jE6kvK5-wKMafwBhd1M4BjYC0Jv5U2cSp1sM_bnbZBd69f12tFPS0qFH0NjzRewdvxUeqtjO9vSIw",
  "token_type": "Bearer",
  "expires_in": 300
}
## opaque token(OAuth2TokenFormat.REFERENCE)响应
{
  "access_token": "w2Q8i9WEYB5ZF03XZAjmcXEUD0rAwr_3l-W4usVAYCaSEUOHA8gG2aP5zocePHSqMOPrtfKxqB8F37w4FB8b8TRDqggxOb9tcq0mb3wOEYGFd-vt-YfabmWps9XzTIHA",
  "token_type": "Bearer",
  "expires_in": 300
}
```

# oauth2-resource-server

- `/rs/public`

```shell
curl --request GET \
  --url http://127.0.0.1:18000/rs/public
# 响应
public
```

- `/rs/private`

```shell
# 1. 客户端凭证模式
curl --request POST \
  --url http://authz.com:19000/oauth2/token \
  --header 'Authorization: Basic cmVzb3VyY2UtY2xpZW50OnNlY3JldA==' \
  --header 'Content-Type: multipart/form-data' \
  --form grant_type=client_credentials \
  --form redirect_uri=http://127.0.0.1:8080/authorized
# 响应
{
  # <access_token>
  "access_token": "eyJraWQiOiIwMjljYjNkZi0yMWJlLTQ3NjAtYTE2ZC1iNDkxMjY2OTBmNWUiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJyZXNvdXJjZS1jbGllbnQiLCJhdWQiOiJyZXNvdXJjZS1jbGllbnQiLCJuYmYiOjE3NDA2MjU4MjgsImlzcyI6Imh0dHA6Ly8xMjcuMC4wLjE6MTkwMDAiLCJleHAiOjE3NDA2MjYxMjgsImlhdCI6MTc0MDYyNTgyOCwianRpIjoiZTVmNDMyZTAtYzIyMC00NjdhLWI5NDUtYTAzNmExYjMxMWZkIn0.Da3ptQDpoNpmTXlK6zuIqPltaqNzAWYMrVO73ezyLZpZXaDe8zAgg2VqSrrI3r3jiyrQYs_1iUrn31u0yQkWMnfmT5XrFfSMvhlK0FBM4_e9bthG10WS1NOhTmamjOqhh2bgjBSlQr9nMcnoz_9VNsIA31QHEcJFIaWXcqNTkuVQAYyMWaeDiL6JsEuir9nPcqDTD9D3AChJLkcQb1ohcDAi5aqVjrYwv4Ney10s6sH33XyiRNekCMjU4CHVO4Rd8UiN9DOqfRF9rrrIuwrikG3o-p7yL-rPPWyPzBU4riOb1UQksVntomENfz0gR20C6MmURuCJv0H8knfEW_gMrw",
  "token_type": "Bearer",
  "expires_in": 300
}

curl --request GET \
  --url http://127.0.0.1:18000/rs/private \
  --header 'Authorization: Bearer <access_token>'
# 响应
private
```

# oauth2-resource-client

Fix `authorization_request_not_found`:

- `hosts`文件中添加`127.0.0.1 authz.com`
- `127.0.0.1:19000` => `authz.com:19000`


- 登录

```shell
# 浏览器中
http://127.0.0.1:18001/login
# 选择客户端后重定向到:
http://authz.com:19000/login
# 登录
# submit consent
# 访问身份认证信息
http://127.0.0.1:18001/rc/authinfo
```

- 登出

```shell
# 浏览器中
http://127.0.0.1:18001/logout
# 重定向到:
http://127.0.0.1:18001/
```

- 使用客户端凭证模式获取access token

```shell
curl --request GET \
  --url http://127.0.0.1:18001/rc/token
# 响应
{
  "tokenValue": "eyJraWQiOiJlMWMwNTdiOC03Yzk0LTQ2MWMtOWQxYy0wYzExYzc0OTcyYWUiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJyZXNvdXJjZS1jbGllbnQiLCJhdWQiOiJyZXNvdXJjZS1jbGllbnQiLCJuYmYiOjE3NDA3MDQ4NTAsInNjb3BlIjpbIm9wZW5pZCJdLCJpc3MiOiJodHRwOi8vYXV0aHouY29tOjE5MDAwIiwiZXhwIjoxNzQwNzA1MTUwLCJpYXQiOjE3NDA3MDQ4NTAsImp0aSI6ImIxMGFiOWU2LWM1ZWUtNDJjMy1hNjRlLTJhZDRiNmYzMTc2ZiJ9.H6BEyQ0kbYJXWt9alN9d_pnd0JGFjjAZKXgzIgExxhmENFITmQH-cCcEo8g2IS8Icvd_F8bxEbluxPdJebWWCQXZ_IQCbv3oc79yAPU77dwt-gyTszsGOkCTE26PCuMhIHheKTXLxB-YVcv1AIMLyJ7riXhxuRqajTjvc2JTFvga_WwAFyoQmHmLnOyIeWuNfNIZZTyZVvZ6INCPLgKChb1MeZiJIoshznyH-eAfp4ftp3lBYLu8CjzJEabbMi92bnesj0PmEi2S8muPQxJ_CLmTfCJdpp5Lse710gerU0XUANzYrjAwR28TXp8yGJucnldqPVl0xl77YBAP95kQIA",
  "issuedAt": "2025-02-28T01:07:30.605092Z",
  "expiresAt": "2025-02-28T01:12:30.605092Z",
  "tokenType": {
    "value": "Bearer"
  },
  "scopes": [
    "openid"
  ]
}
```

- 访问资源服务器资源

```shell
# 浏览器中
http://127.0.0.1:18001/rc/private
# 登录
# 重定向到:
http://127.0.0.1:18001/rc/private?continue
# 响应
private
```

# keycloak-resource-server

* Keycloak realm [authz-servlet](https://github.com/keycloak/keycloak-quickstarts/blob/latest/spring/rest-authz-resource-server/README.md)
* 使用Keycloak管理客户端获取access token: `TestKeycloak`

- 访问`/`
```shell
# alice
curl --request GET \
  --url http://127.0.0.1:18002/protected/premium \
  --header 'Authorization: Bearer <access-token>'
# 响应
Hello, alice!
```
- 访问`/protected/premium`
```shell
# alice
curl --request GET \
  --url http://127.0.0.1:18002/protected/premium \
  --header 'Authorization: Bearer <alice-access-token>'
# 响应: 403
{
  "timestamp": "2025-03-01T09:26:57.775+00:00",
  "status": 403,
  "error": "Forbidden",
  "message": "No message available",
  "path": "/protected/premium"
}
# jdoe
curl --request GET \
  --url http://127.0.0.1:18002/protected/premium \
  --header 'Authorization: Bearer <jdoe-access-token>'
# 响应
Hello, jdoe!
```

# keycloak-resource-client

- 访问私有资源
```shell
http://192.168.0.105:18003/private
# 重定向到:
http://authz.com:19001/realms/myrealm/protocol/openid-connect/auth?...
# 登录后重定向到:
http://192.168.0.105:18003/private?continue
# 响应
private
```

- 登出
```shell
http://192.168.0.105:18003/logout
# 点击Log Out
# 重定向到:
http://192.168.0.105:18003
```