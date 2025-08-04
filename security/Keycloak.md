# Keycloak
* https://www.keycloak.org/
* https://github.com/keycloak/keycloak
* ConceptMap: [keycloak.cmap](./keycloak.cmap)

> **Keycloak** is an [open source software](https://en.wikipedia.org/wiki/Open-source_software "Open-source software") product to allow [single sign-on](https://en.wikipedia.org/wiki/Single_sign-on "Single sign-on") with [identity and access management](https://en.wikipedia.org/wiki/Identity_management "Identity management") aimed at modern applications and services. As of March 2018 this [WildFly](https://en.wikipedia.org/wiki/WildFly "WildFly") community project is under the stewardship of [Red Hat](https://en.wikipedia.org/wiki/Red_Hat "Red Hat") who use it as the [upstream](https://en.wikipedia.org/wiki/Upstream_(software_development) "Upstream (software development)") project for their [Red Hat build of Keycloak](https://en.wikipedia.org/wiki/Red_Hat#Red_Hat_build_of_Keycloak "Red Hat").
>
> Keycloak supports various protocols such as [OpenID](https://en.wikipedia.org/wiki/OpenID "OpenID"), [OAuth](https://en.wikipedia.org/wiki/OAuth "OAuth") version 2.0 and [SAML](https://en.wikipedia.org/wiki/SAML "SAML") and provides features such as user management, [two-factor authentication](https://en.wikipedia.org/wiki/Two-factor_authentication "Two-factor authentication"), permissions and roles management, creating [token services](https://en.wikipedia.org/wiki/Security_token_service "Security token service"), etc.

> **Open Source Identity and Access Management (IAM)**
> Add authentication to applications and secure services with minimum effort.
> No need to deal with storing users or authenticating users.
> Keycloak provides user federation, strong authentication, user management, fine-grained authorization, and more.

Books:
- Thorgersen, Stian / Silva, IconPedro Igor. **Keycloak - Identity and Access Management for Modern Applications: Harness the power of Keycloak, OpenID Connect, and OAuth 2.0 to secure applications**. 2023, 2. edition. Packet.
  - Keycloak version: 22.0.0
  - Node.js示例
  - 标准简介: OAuth 2.0, OpenID Connect, JWT(JSON Web Tokens), SAML 2.0(Secueiry Assertion Markup Language)
  - 应用类型: 内部/外部应用, Web应用(服务端, SPA), native和移动端应用, REST服务
  - integration: Golang, Java(Quarkus, Spring Boot), JavaScript, Node.js, Apache HTTP Server
  - 授权策略: RBAC, GBAC, OAuth2 scopes, ABAC

actions:
- https://github.com/jargonzhou/application-store/tree/main/security/keycloak
- example-springcloud/keycloak-resource-client
- example-springcloud/keycloak-resource-server: 策略实施过滤器
- HTTPie: Archived > Keycloak

example:
- Quarkus Cookbook: Authentication and Authorization
- Spring
  - https://github.com/keycloak/keycloak-quickstarts/blob/latest/spring/rest-authz-resource-server/README.md
  - [A Quick Guide to OAuth2 With Spring Boot And Keycloak](https://www.baeldung.com/spring-boot-keycloak)
  - [Simple Single Sign-On with Spring Security OAuth2](https://www.baeldung.com/sso-spring-security-oauth2)
  - [Keycloak Integration – OAuth2 and OpenID with Swagger UI](https://www.baeldung.com/keycloak-oauth2-openid-swagger)

# Documentation
* https://www.keycloak.org/documentation
## Getting started
A **realm** in Keycloak is equivalent to a tenant. Each realm allows an administrator to create isolated groups of *applications* and *users*. Initially, Keycloak includes a single realm, called `master`. Use this realm only for managing Keycloak and not for managing any applications.

consoles:
- Admin: `master` realm, http://localhost:19001/admin
	- create realm `myrealm`
		- create user: `myuser`
			- Credentials: password `myuser`
		- create client/application: to secure the application
			- client type `OpenID Connect`
			- client id `myclient`
			- flow (capability config): Standard flow
			- login settings: 
  			- valid redirect URIs: `https://www.keycloak.org/app/*`
  			- web origin `https://www.keycloak.org`
- Account: `myrealm`, http://localhost:19001/realms/myrealm/account/#/
	- login with user `myuser`
- [the SPA testing application](https://www.keycloak.org/app/) - `myclient`
	- Keycloak URL: http://localhost:19001
	- Realm: `myrealm`
	- Client: `myclient`
	- Sign in => http://localhost:19001/realms/myrealm/protocol/openid-connect/auth?client_id=myclient&redirect_uri=https%3A%2F%2Fwww.keycloak.org%2Fapp%2F%23url%3Dhttp%3A%2F%2Flocalhost%3A18080%26realm%3Dmyrealm%26client%3Dmyclient&state=42706940-9ec7-401e-84a9-733576e1f64d&response_mode=fragment&response_type=code&scope=openid&nonce=b1d5f8f5-fd37-462f-80c9-2311313ea8df&code_challenge=tHkkuB3T2TDJ7Lv3sihyrGT104lKhrWpgjNwdarbIvk&code_challenge_method=S256
		- `myuser`
> ## Server Installation and Configuration
> ## Server Container Image
## Securing Applications and Services

Planning for securing applications and services
* [quickstarts repo](https://github.com/keycloak/keycloak-quickstarts)
  * OpenID Connect: Java, JavaScript, Node.js, C#, Ptyhon, Android, iOS, Apache HTTP Server
    * [Spring Boot](https://github.com/keycloak/keycloak-quickstarts/tree/latest/spring/rest-authz-resource-server)
  * SAML: Java, Apache HTTP Server
Secure applications and services with OpenID Connect
* Endpoints: /realms/{realm-name}/.well-known/openid-configuration
  * Authorization endpoint: /realms/{realm-name}/protocol/openid-connect/auth
  * Token endpoint: /realms/{realm-name}/protocol/openid-connect/token
  * Userinfo endpoint: /realms/{realm-name}/protocol/openid-connect/userinfo
  * Logout endpoint: /realms/{realm-name}/protocol/openid-connect/logout
  * Certificate endpoint: /realms/{realm-name}/protocol/openid-connect/certs
  * Introspection endpoint: /realms/{realm-name}/protocol/openid-connect/token/introspect
  * Dynamic Client Registration endpoint: /realms/{realm-name}/clients-registrations/openid-connect
  * Token Revocation endpoint: /realms/{realm-name}/protocol/openid-connect/revoke
  * Device Authorization endpoint: /realms/{realm-name}/protocol/openid-connect/auth/device
  * Backchannel Authentication endpoint: /realms/{realm-name}/protocol/openid-connect/ext/ciba/auth
* Grant Types:
  * Authorization code
  * Implicit
  * Resource Owner Password Credentials
  * Client credentials
  * Device Authorization Grant
  * Client Initiated Backchannel Authentication Grant
* 术语
  * clients: 与Keycloak交互以验证用户和获取token的实体. 通常是代表用户的应用和服务, 提供了单点登录用户体验, 使用Keycloak颁发的token访问其他服务. 也可以是获取token且代表自己访问其他服务的实体.
  * application: 各种应用
  * client adapters: 便于集成Keycloak的库.
  * creating/registering a client: 使用管理控制台创建客户端, 使用Keycloak客户端注册服务注册客户端
  * a service account: 可以获取代表自己的token的客户端

客户端注册:
- Client registration service
- Client registration CLI

token交换
- Using token exchange

客户端
- Keycloak admin client: ` org.keycloak.admin.client.Keycloak`
- Keycloak authorization client: 配置`keycloak.json`
- Keycloak policy enforcer: 授权上下文`org.keycloak.AuthorizationContext`

外部集成
- Apache APISIX
- KrakenD
- Quarus
- Traefik Hub
- WildFly
## Server Administration
- [Server Administration Guide](https://www.keycloak.org/docs/24.0.1/server_admin/)
> Basic Keycloak operations
> Keycloak is a separate server that you manage on your network. Applications are configured to point to and be secured by this server. Keycloak uses open protocol standards like OpenID Connect or SAML 2.0 to secure your applications. Browser applications redirect a user’s browser from the application to the Keycloak authentication server where they enter their credentials. This redirection is important because users are completely isolated from applications and applications never see a user’s credentials. Applications instead are given an identity token or assertion that is cryptographically signed. These tokens can have identity information like username, address, email, and other profile data. They can also hold permission data so that applications can make authorization decisions. These tokens can also be used to make secure invocations on REST-based services.

- [Configuring TLS](https://www.keycloak.org/server/enabletls)
	- option1: PEM formated certificate and private key files
	- option2: Java keystore file, keystore password
	- mTLS: use a truststore to validate client certificats, truststore password
	- secure credentials: using a vault / mounted secret





> ## Server Developer

## Authorization Services

细粒度的授权策略, 不同的访问控制机制:
- Attribute-based access control (ABAC)
- Role-based access control (RBAC)
- User-based access control (UBAC)
- Context-based access control (CBAC)
- Rule-based access control
  - Using JavaScript
- Time-based access control
- Support for custom access control mechanisms (ACMs) through a Service Provider Interface (SPI)

主要的授权过程:
- 资源管理(Resource Management)
- 权限和策略管理(Permission and Policy Management)
- 策略实施(Policy Enforcement)
## Upgrading

The client libraries are those artifacts: [link](https://www.keycloak.org/securing-apps/upgrading)
- Java admin client - Maven artifact `org.keycloak:keycloak-admin-client` 管理客户端
- Java authorization client - Maven artifact `org.keycloak:keycloak-authz-client` 授权客户端
- Java policy enforcer - Maven artifact `org.keycloak:keycloak-policy-enforcer` 策略实施器
- Java common classes used by other client libraries above - Maven artifact `org.keycloak:keycloak-client-common-synced`
## API
- JavaDoc
- [Administration REST API](https://www.keycloak.org/docs-api/latest/rest-api/index.html)

More:
- [Dynamic Client Registration in Keycloak](https://medium.com/keycloak/dynamic-client-registration-in-keycloak-4dd1c5cd5e69)
	- [Collections](https://medium.com/keycloak)

