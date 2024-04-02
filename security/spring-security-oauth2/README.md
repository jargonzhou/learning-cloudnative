# Spring Security

- https://spring.io/projects/spring-security

- version: Spring Boot 3.2.4, Spring Cloud 2023.0.0.
- ref: 'Spring Security in Action'.

## kickoff

1. authorize
access [link](http://127.0.0.1:8080/oauth2/authorize?response_type=code&client_id=messaging-client&scope=message:read&redirect_uri=http://127.0.0.1:8080/authorized)

login with `user/password`

response:
```
http://127.0.0.1:8080/authorized?code=gDlMr7_CH6JYe-jbodWoE4TPPe9hTRC-JdR_w0ICyitcAuPhNXfYR4D-cr9baoOb7No55bcx9NFLwVNIG5r66uL7SSLiUtQpKChqGlNC6ILB5YLQJ9LbsPoUjZF5Hf4K
```

2. get access token
```shell
export code=fBRxbroSiK13qdCC9Z5k_gyCgP2TfDPoIdAcN-GCeJsDvfvV3Bsbl_BbxwPU060-0ks8xFrNa6IYANfJR4eIWxR0xCoAbbECbhOMB7ua5raKYx5-qJLdLg7A37bmoiWf

curl --request POST \
  --url http://127.0.0.1:8080/oauth2/token \
  --header 'Authorization: Basic bWVzc2FnaW5nLWNsaWVudDpzZWNyZXQ=' \
  --header 'Content-Type: multipart/form-data' \
  --form code=$code \
  --form grant_type=authorization_code \
  --form redirect_uri=http://127.0.0.1:8080/authorized

{
    "access_token": "eyJraWQiOiIwZTkyNmZhZS01YzRiLTQ2MzItODI3Yy03ZTM5OTMyYTFkZGUiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiYXVkIjoibWVzc2FnaW5nLWNsaWVudCIsIm5iZiI6MTcxMjA3MDM2Niwic2NvcGUiOlsibWVzc2FnZTpyZWFkIl0sImlzcyI6Imh0dHA6Ly8xMjcuMC4wLjE6ODA4MCIsImV4cCI6MTcxMjA3MDY2NiwiaWF0IjoxNzEyMDcwMzY2LCJqdGkiOiIyMjYwODFmZS1lNGU0LTQzZWUtYWQxYS05ZmFiMmM1ZTEzM2EifQ.QoWYpjk8LL9ZAsjseQ2735daTGdAohIeH4SIX6ebOFXZtTCUis7A55h1isQHDNVXH0UAcSJ1QeMokKKVG5zxWNp2_dgOK2aqOa5S3mtUMMo7cUMvWA4BNqcJhgrA1Dq01tYYsLy1IlXiY2HqJXpDDHwa0eL9sIKoNzTx09iv1QaAdGzPxOwhHEARSLbELTy8pLonRzXe-a-pGcGTYy-GBtZNYJvgJuOPjl94YiJv5-uY2lD5hr6K8KszwOZX9Zu7hjwOOr_dZgj9S-WetJu9w-_oSR5uDYaNAk5nvaGDnq076SbOsYSLOCrZOYZ6YHN0qgAeBBIDYh037We3EoT8bw",
    "refresh_token": "tr6w44vqkzFpFaQG-ZrJvNx7LVgrW6doiAbktzd_RL6hMsGCQjCsAWgEN4me4w4eyitSvjqTd_DckATp8M8ooAGNGJwXI9y6NG838ezBMGzh-3CbeXoTjM0X8cJt3aaS",
    "scope": "message:read",
    "token_type": "Bearer",
    "expires_in": 300
}
```

3. introspect access token

```shell
export access_token=eyJraWQiOiIwZTkyNmZhZS01YzRiLTQ2MzItODI3Yy03ZTM5OTMyYTFkZGUiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiYXVkIjoibWVzc2FnaW5nLWNsaWVudCIsIm5iZiI6MTcxMjA3MDM2Niwic2NvcGUiOlsibWVzc2FnZTpyZWFkIl0sImlzcyI6Imh0dHA6Ly8xMjcuMC4wLjE6ODA4MCIsImV4cCI6MTcxMjA3MDY2NiwiaWF0IjoxNzEyMDcwMzY2LCJqdGkiOiIyMjYwODFmZS1lNGU0LTQzZWUtYWQxYS05ZmFiMmM1ZTEzM2EifQ.QoWYpjk8LL9ZAsjseQ2735daTGdAohIeH4SIX6ebOFXZtTCUis7A55h1isQHDNVXH0UAcSJ1QeMokKKVG5zxWNp2_dgOK2aqOa5S3mtUMMo7cUMvWA4BNqcJhgrA1Dq01tYYsLy1IlXiY2HqJXpDDHwa0eL9sIKoNzTx09iv1QaAdGzPxOwhHEARSLbELTy8pLonRzXe-a-pGcGTYy-GBtZNYJvgJuOPjl94YiJv5-uY2lD5hr6K8KszwOZX9Zu7hjwOOr_dZgj9S-WetJu9w-_oSR5uDYaNAk5nvaGDnq076SbOsYSLOCrZOYZ6YHN0qgAeBBIDYh037We3EoT8bw

curl --request POST \
  --url http://127.0.0.1:8080/oauth2/introspect \
  --header 'Authorization: Basic bWVzc2FnaW5nLWNsaWVudDpzZWNyZXQ=' \
  --header 'Content-Type: application/x-www-form-urlencoded' \
  --data token=$access_token

{
    "active": true,
    "sub": "user",
    "aud": [
        "messaging-client"
    ],
    "nbf": 1712070366,
    "scope": "message:read",
    "iss": "http://127.0.0.1:8080",
    "exp": 1712070666,
    "iat": 1712070366,
    "jti": "226081fe-e4e4-43ee-ad1a-9fab2c5e133a",
    "client_id": "messaging-client",
    "token_type": "Bearer"
}
```

4. access protected resource

```shell
curl --request GET \
--url http://127.0.0.1:8081/ \
--header "Authorization: Bearer $access_token"

Hello, user!

curl --request GET \
--url http://127.0.0.1:8081/message \
--header "Authorization: Bearer $access_token"

secret message
```

