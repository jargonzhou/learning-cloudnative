# Security

actions:
* [spring-security-oauth2](./spring-security-oauth2/README.md)

# Cryptography
* [Cryptography](./Cryptography.md)

# Specifications

## Authentication, Authorization

* [OAuth 2.0](https://oauth.net/2/): RFC 6749
* [OpenID Connect](https://openid.net/developers/specs/)

# Certification
## certbot
* [home](https://certbot.eff.org/): My HTTP website is running 'Software' on 'System'
* [code](https://github.com/certbot/certbot): Python

> Certbot is part of EFF’s effort to encrypt the entire Internet. Secure communication over the Web relies on HTTPS, which requires the use of a digital certificate that lets browsers verify the identity of web servers (e.g., is that really google.com?). Web servers obtain their certificates from trusted third parties called certificate authorities (CAs). Certbot is an easy-to-use client that fetches a certificate from Let’s Encrypt—an open certificate authority launched by the EFF, Mozilla, and others—and deploys it to a web server.
>
> Anyone who has gone through the trouble of setting up a secure website knows what a hassle getting and maintaining a certificate is. Certbot and Let’s Encrypt can automate away the pain and let you turn on and manage HTTPS with simple commands. Using Certbot and Let's Encrypt is free.

example:
* [SSL 免费证书申请 - Certbot](https://www.runoob.com/http/ssl-certbot.html)
* [使用Certbot申请免费 HTTPS 证书及自动续期](https://juejin.cn/post/7205839782381928508): with Nginx


concepts:
* Let's Encrypt: 一个由互联网安全研究小组（ISRG）运营的证书颁发机构（CA），它提供了一个自动化的流程来生成和更新证书.
* certbot: 一个开源的自动化工具，用于获取和续订由 Let's Encrypt 提供的免费 SSL/TLS 证书.
* 域名: 主域名, 泛域名
* 证书申请
* DNS TXT记录
* 证书续期: 手动续期, 自动续期

# IAM
books:
* Wilson, Yvonne / Hingnikar, Abhishek. **Solving Identity Management in Modern Applications: Demystifying OAuth 2, OpenID Connect, and SAML 2**. 2019. Apress.

# Tools
* [Falco](./Falco.md)
* [Keycloak](./Keycloak.md)

<!--
XSS: Cross Site Script, 跨站脚本攻击

CSRF: Cross Site Request Forgery, 跨站请求伪造

# CORS: Cross-Origin Resource Sharing 跨域资源共享

跨域资源共享 CORS 详解
https://www.ruanyifeng.com/blog/2016/04/cors.html

CORS是一个W3C标准，全称是"跨域资源共享"（Cross-origin resource sharing）。
它允许浏览器向跨源服务器，发出XMLHttpRequest请求，从而克服了AJAX只能同源使用的限制。

CORS需要浏览器和服务器同时支持。

对于简单请求，浏览器直接发出CORS请求。具体来说，就是在头信息之中，增加一个`Origin`字段。
如果Origin指定的源，*不在许可范围内*，服务器会返回一个正常的HTTP回应。浏览器发现，这个回应的头信息没有包含Access-Control-Allow-Origin字段，就知道出错了，从而抛出一个错误，被XMLHttpRequest的onerror回调函数捕获

如果Origin指定的域名*在许可范围内*，服务器返回的响应，会多出几个头信息字段。
Access-Control-Allow-Origin: http://api.bob.com
Access-Control-Allow-Credentials: true
Access-Control-Expose-Headers: FooBar
Content-Type: text/html; charset=utf-8

# JWT: JSON Web Token

JWT ， 全写JSON Web Token, 是开放的行业标准RFC7591，用来实现*端到端安全验证*.

JWT加密JSON，保存在客户端，*不需要在服务端保存会话信息*，可以应用在*前后端分离*的用户验证上，后端对前端输入的用户信息进行*加密*产生一个*令牌字符串*， 前端再次请求时附加此字符串，后端再使用算法解密。

*密钥保存在服务端*，服务端根据密钥进行解密验证。

JSON Web Token 入门教程
https://www.ruanyifeng.com/blog/2018/07/json_web_token-tutorial.html

JWT 的三个部分依次如下。
1. Header（头部）
{
  "alg": "HS256",
  "typ": "JWT"
}
将上面的 JSON 对象使用 Base64URL 算法转成字符串
2. Payload（负载）
JWT 规定了7个官方字段，供选用。
iss (issuer)：签发人
exp (expiration time)：过期时间
sub (subject)：主题
aud (audience)：受众
nbf (Not Before)：生效时间
iat (Issued At)：签发时间
jti (JWT ID)：编号
除了官方字段，你还可以在这个部分定义私有字段，下面就是一个例子。
{
  "sub": "1234567890",
  "name": "John Doe",
  "admin": true
}
这个 JSON 对象也要使用 Base64URL 算法转成字符串

3. Signature（签名）
HMACSHA256(
  base64UrlEncode(header) + "." +
  base64UrlEncode(payload),
  secret)

算出签名以后，把 Header、Payload、Signature 三个部分拼成一个字符串，每个部分之间用"点"（.）分隔，就可以返回给用户。

Base64 有三个字符+、/和=，在 URL 里面有特殊含义，所以要被替换掉：=被省略、+替换成-，/替换成_ 。这就是 Base64URL 算法。
-->