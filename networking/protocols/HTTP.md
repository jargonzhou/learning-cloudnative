# HTTP: Hypertext Transfer Protocol

books:
- Pollard, Barry. **HTTP/2 in Action**. 2019. Manning.
# History

- 0.9: 1991
  - GET
- 1.0: 1996, RFC 1945
  - HEAD, POST
  - headers: request, response
    - `Expires`
  - 3-digit response code
    - 1xx: informational
    - 2xx: successful
    - 3xx: redirection
    - 4xx: client error
    - 5xx: server error
- 1.1: 1997
  - 2022 RFC 9110 'HTTP Semantics', RFC 9112 'HTTP/1.1'
  - PUT, OPTIONS, CONNECT, TRACE, DELETE
  - mandantory `Host` header 
  - persistent connections: 持久连接. `Connection: Keep-Alive`, `Connection: close`
    - pipelining: 管线化. 通过同一个持久连接发送多个请求, 按序获得响应. 大多数实现还是请求-响应协议.
  - `Cache-Control`, `ETag`
  - cookies
  - character sets
  - proxy support
  - authentication
  - new status code
  - trailing headers
  - chunked transfer: 分块传输
  - range requests: 断点续传
# HTTPS

HTTPS use SSL or TLS to procide encryption.
- SSL: Secure Socket Layer. owned by Netscape
- TLS: Transport Layer Security. standard


SSL/TLS协议运行机制的概述
https://www.ruanyifeng.com/blog/2014/02/ssl_tls.html

每一次对话 (session), 客户端和服务器端都生成一个"*对话密钥*" (session key), 用它来加密信息. 由于"对话密钥"是*对称加密*, 所以运算速度非常快, 而服务器公钥只用于加密"对话密钥"本身, 这样就减少了加密运算的消耗时间. 

SSL/TLS协议的基本过程是这样的：

- (1) 客户端向服务器端索要并验证公钥. 
- (2) 双方协商生成"对话密钥". 
- (3) 双方采用"对话密钥"进行加密通信. 

上面过程的前两步, 又称为"握手阶段" (handshake). 

握手阶段的详细过程:

4.1 客户端发出请求 (ClientHello)
- (1) 支持的协议版本, 比如TLS 1.0版. 
- (2) *一个客户端生成的随机数*, 稍后用于生成"对话密钥". 
- (3) 支持的加密方法, 比如RSA公钥加密. 
- (4) 支持的压缩方法. 

4.2 服务器回应 (SeverHello)
- (1) 确认使用的加密通信协议版本, 比如TLS 1.0版本. 如果浏览器与服务器支持的版本不一致, 服务器关闭加密通信. 
- (2) *一个服务器生成的随机数*, 稍后用于生成"对话密钥". 
- (3) 确认使用的加密方法, 比如RSA公钥加密. 
- (4) *服务器证书*. 

4.3 客户端回应
- (1) *一个随机数*. 该随机数*用服务器公钥加密*, 防止被窃听. 
- (2) *编码改变通知*, 表示随后的信息都将用双方商定的加密方法和密钥发送. 
- (3) *客户端握手结束通知*, 表示客户端的握手阶段已经结束. 这一项同时也是前面发送的所有内容的hash值, 用来供服务器校验. 

上面第一项的随机数, 是整个握手阶段出现的第三个随机数, 又称"pre-master key". 有了它以后, 客户端和服务器就同时有了三个随机数, 接着双方就用事先商定的加密方法, 各自生成本次会话所用的同一把"会话密钥". 

三个随机数:

不管是客户端还是服务器, 都需要随机数, 这样生成的密钥才不会每次都一样. 由于SSL协议中证书是静态的, 因此十分有必要引入一种随机因素来保证协商出来的密钥的随机性. 
对于RSA密钥交换算法来说, pre-master-key本身就是一个随机数, 再加上hello消息中的随机, **三个随机数通过一个密钥导出器最终导出一个对称密钥**. 

4.4 服务器的最后回应
- (1)*编码改变通知*, 表示随后的信息都将用双方商定的加密方法和密钥发送. 
- (2)*服务器握手结束通知*, 表示服务器的握手阶段已经结束. 这一项同时也是前面发送的所有内容的hash值, 用来供客户端校验. 

至此, 整个握手阶段全部结束. 接下来, 客户端与服务器进入加密通信, 就完全是使用普通的HTTP协议, 只不过用"会话密钥"加密内容. 
# SPDY

2009

new concepts to HTTP/1.1:
- multiplexed stream: 多路复用流
- request prioritization: 请求优先级
- HTTP header compression: 头压缩
# HTTP/2

2015, RFC 7450

new concepts to HTTP/1:
- binary rather than textual protocol: 二进制协议
- multiplexed rather than synchronous: use a single connection with streams. 多路复用, 一个连接中多个请求的流, 请求响应数据帧.
- flow control: 流层次的背压
- stream prioritization: 请求优先级
  - HTTP/1例: 先请求关键资源(HTML, CSS, 关键JavaScript), 再请求非阻塞项(图片, 异步JavaScript). 请求由浏览器排队, 并确定优先级.
  - 由服务端实现: 等待发送的帧队里中, 优先发送高优先级请求的响应帧. 
- header compression: 处理重复的头
  - 允许跨请求的头压缩, 避免了体压缩算法中的安全性问题.
- server push: 服务端推送, 主动发送与客户端请求相关的资源
## Create HTTP/2 Connection

4 ways:
- use HTTPS negotiation: HTTP/2支持作为HTTPS握手的一部分, 节省了升级重定向.
- use the HTTP `Upgrade` header
- use prior knowledge
- use HTTP Alternative Services
HTTPS handshake - TLSv1.2

Client -> Server:
- 1. ClientHello: 客户端的密码技术能力

Server -> Client:
- 2. ServerHello: 根据客户端能力, 选择HTTPS协议(例如TLSv1.2), 用于该连接的cipher(例ECDHE-RSA-AES128-GCM-SHA256)
- 3. ServerCetificate: 服务端HTTPS证书
- 4. ServerKeyExchange: 根据选择的cipher使用的密钥详情
- 5. CertificateRequest: 是否需要客户端HTTPS证书
- 6. ServerHelloDone

Client -> Server:
- 7. ClientCertificate: 服务端需要时发送
- 8. ClientKeyExchange - 用公私钥加密: 客户端cipher使用的密钥详情, 用服务端证书中公钥加密.
- 9. CertificateVerify - 用公私钥加密: 使用客户端证书时发送, 用客户端证书私钥加密.
- 10. ChangeCipherSpec: 客户端使用ServerKeyExchange和ClientKeyExchange确定对称密钥, 告知服务端开始使用对称密钥.
- 11. Finished - 用约定的密钥加密

Server -> Client:
- 12. ChangeCipherSpec: 告知客户端开始使用对称密钥.
- 13. Finished - 用约定的密钥加密


TODO: key exchange, cipher???
ALPN: Application Layer Protocol Negotiation
- ClientHello: with ALPN options
- ServerHello: with ALPN choice
`Upgrade` header: 只在未加密的HTTP连接中使用
- h2c: unencrypted HTTP connection
- h2: encrypted HTTPS HTTP/2 connection
- 由客户端决定发送:
  - 每个请求中
  - 只在初始请求中
  - 服务端已经在响应中告知HTTP/2支持: `Upgrade`头
使用先验知识: 客户端已经知道服务端支持HTTP/2, 直接使用HTTP/2, 避免了升级请求.
- 明确知道后端服务支持HTTP/2.
- 通过HTTP/1.1通告的`Alt-Svc` header, 或者`ALTSVC`帧.
HTTP Alternative Services:
- 服务端告知使用HTTP/1.1的客户端, 用`Alt-Svc`头告知, 请求的资源使用不同的协议在另一个位置可用.
- 在已有的HTTP/2连接上: `ALTSVC`帧: 例客户端想切换到不同的连接.
HTTP/2 preface消息:
- `PRI`方法
- 用于客户端尝试与不支持HTTP/2的服务端使用HTTP/2, 这时服务端不理解`PRI`方法而拒绝该消息.
## HTTP/2 Frames
查看:
- Chrome net-export
- nghttp
- Wireshark
帧格式: ...


帧类型:
- DATA (0x0)
- HEADERS (0x1)
- PRIORITY (0x2)
- RST_STREAM (0x3)
- SETTINGS (0x4)
- PUSH_PROMISE (0x5)
- PING (0x6)
- GOAWAY (0x7)
- WINDOW_UPDATE (0x8)
- CONTINUATION (0x9)
- ALTSVC (0xa), added through RFC 7838
- (0xb), not used at present but used in the past
- ORIGIN (0xc), added through RFC 8336
- CACHE_DIGEST, proposed
## HTTP/2 Sever Push
How to push?

Nginx:
```shell
# use HTTP link header
add_header Link "</assets/css/common.css>;as=style;rel=preload"

# push earlier
http2_push /assets/css/common.css;
```
## HTTP/2 Advance concepts
- stream states
- flow control
- prioritization
- conformance testing
- HPACK protocol: header compression 头压缩
# HTTP/3
## QUIC
- original acronym: Quick UDP Internet Connections
- QUIC is a name, not an acronym
features:
- dramatically reduce connection establishment time: 减少连接建立时间
  - 客户端再没有建立连接的情况下, 向服务端发送请求
- improved congestion control
- multiplexing without HOL line blocking
- forward error correction: 纠错码
  - 减少包重传
- connection migration: 连接迁移
  - 通过允许连接在网络上移动来减少连接设置消耗
  - 例: 在Wi-Fi中开始会话, 然后移动到移动网络中, 不需要重启会话.
  - 甚至在一个QUIC连接中同时使用Wi-Fi和移动网络: 使用multipath技术.
standard:
- gQUIC: Google
  - tool: Chrome net-export
- iQUIC: IETF
# Tools
- telnet
- nc
- Chrome developer tools
- Advanced REST client application: https://install.advancedrestclient.com
- curl, wget, httpie, SOAP-UI
- Fiddler, Wireshark: network sniffer programs
- site performance: https://www.webpagetest.org/
  - Waterfall diagram
- HTTP/2 support: https://caniuse.com/?search=HTTP%2F2
  - brower
  - server: Apache httpd 2.4.17, Jetty 9.3, Netty 4.1, Nginx 1.9.5, Tomcat 8.5
- Chrome net-export: `chrome://net-export/`, https://netlog-viewer.appspot.com
- nghttp: nghttp2 C库上开发的命令行工具


<!--
断点续传原理
https://mdnice.com/writing/727e20d51c304f05aa2ad42bdf3a27a9

断点续传中断点续传是如何实现的？
https://cloud.tencent.com/developer/article/1688461

HTTP2.0和HTTP3.0相关问题
https://juejin.cn/post/7224793504106577976
简述 http3.0~http1.0 分别有什么改进？
https://blog.51cto.com/leadingcode/5995864
【网络小知识】之TCP IP 五元组(five-tuple/5-tuple)
https://blog.csdn.net/myhes/article/details/108318908

-->