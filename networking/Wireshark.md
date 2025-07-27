# Wireshak
* https://www.wireshark.org
  * [User’s Guide](https://www.wireshark.org/docs/wsug_html_chunked/)

> The world's leading network protocol analyzer
>
> Wireshark lets you dive deep into your network traffic - free and open source.

books:
- Wireshark网络分析就这么简单
- Wireshark数据包分析实战
- Wireshark网络分析的艺术

```shell
$ ls /mnt/d/software/Wireshark | grep exe
Wireshark.exe
capinfos.exe
captype.exe
dumpcap.exe
editcap.exe
mergecap.exe
mmdbresolve.exe
npcap-1.78.exe
randpkt.exe
rawshark.exe
reordercap.exe
sharkd.exe
text2pcap.exe
tshark.exe
uninstall-wireshark.exe
```

## Filters
* [CaptureFilters](https://wiki.wireshark.org/CaptureFilters)
* [Display Filter Reference](https://www.wireshark.org/docs/dfref/)

## tshark

```shell
tshark -h
```

## Statistics


### Flow Graph
* https://www.wireshark.org/docs/wsug_html_chunked/ChStatFlowGraph.html

example:

<img src="https://www.wireshark.org/docs/wsug_html_chunked/images/ws-flow-graph.png" width="800"/>

## TCP

分析ref:
- [link 1](https://www.kawabangga.com/posts/4794)
- [link 2](https://cloud.tencent.com/developer/article/2242247)

## HTTPS

```shell
"C:\Program Files\Google\Chrome\Application\chrome.exe" --ssl-key-log-file=D:\sslkey.log
```

ref: 
- [link 1](https://www.cnblogs.com/yurang/p/11505741.html)
- [link 2](https://cloud.tencent.com/developer/article/2354010)
- [link 3](https://unit42.paloaltonetworks.com/wireshark-tutorial-decrypting-https-traffic/)


<!--
Wireshark网络分析就这么简单

# 初试锋芒
> 从一道面试题开始说起

- A
  - 192.168.26.129
  - 255.255.255.0          range: 192.168.26.1 - 192.168.26.254
  - 00:0c:29:0c:22:10
- B
  - 192.168.26.3
  - 255.255.255.224    <-- range: 192.168.26.1 - 192.168.26.30
  - 00:0c:29:51:f1:7b
- 默认网关
  - 192.168.26.2
  - 00:50:56:e7:2f:88

B ping A: 通
- 跨子网通信需要默认网关的转发
- 同子网通信无需默认网关的参与
- 执行ARP回复时不考虑子网

> 小试牛刀：一个简单的应用实例

A
- eth0: 192.168.26.131/255.255.255.0  00:0C:29:CB:74:A9
- eth1: 192.168.174.131/255.255.255.0 00:0C:29:CB:74:B3            <-- ARP请求到不了B
- eth2: 192.168.186.131/255.255.255.0 00:0C:29:CB:74:BD
- route
  - default 192.168.26.2 255.255.255.0 UG 0 0 0 eth0
  - 192.168.182.0 * 255.255.255.0 U 0 0 0 eth1                 <--              删除这条路由

B
- 192.168.182.131/255.255.255.0

A ping B: 不通


> Excel 文件的保存过程

- 文件保存到网络盘

> 你一定会喜欢的技巧

抓包
- 只抓包头: Capture - Options - Limit each packet to: 80字节
  - tcpdump -i eth0 -s 80 -w tcpdump.cap
- 只抓必要的包: Capture - Options - Capture Filter: host 10.32.200.131
  - tcpdump -i eth0 host 10.32.200.131 -w tcpdump.cap
- 为每步操作打上标记
  - ping -n 1 -l 1: -l为send buffer size 

个性化设置
- 时间: View - Time Display Format - Date and Time of Day
- 网络包自定义颜色: View - Coloring Rules
- 更多设置: Edit - Preferences

过滤
- ip.addr eq 10.32.106.50 && tcp.port eq 445
  - 右键感兴趣的包: Follow TCP/UDP Stream
    - Statistics - Converstaions
- kerberos: Windows Domain身份认证
- portmap || mount: NFS共享挂载
- 用鼠标帮助过滤: 右键 - Prepare a Filter - Selected
- 保存过滤后的包: File - Save As - Displayed
  - File - Export Sepcified Packets...


让Wireshark自动分析
- Analyze - Expert Info Composite
  - 例: 重传的统计, 连接的统计, 重置的统计
- Statistics - Service Response Time
  - 响应时间的统计
- Statistics - TCP Stream Graph
  - 例: Time-Sequence Graph (Stevens) 
- Statistics - Summary
  - 统计信息: 平均流量等

最容易上手的搜索功能
- Ctrl + F

> Patrick的故事

- TCP超时重传的间隔时间
- 网络拥塞, 发送窗口大小

> Wireshark的前世今生

Gerald Combs
- 1998-07, Ethereal
- Whreshark
- 2012, No.1 Packet Sniffers
- Whreshark数据包分析实战

# 庖丁解牛

> NFS协议的解析

SUN: Stanford University Network

NFS: Network File System
- v2: 1984
- v3: 1995
- v4: 2000

RFC 1813

例: 
- 客户端: 10.32.106.159
- 文件服务器: 10.32.106.62
  - NFS进程
  - mount进程
- 过滤器: `portmap || mount || nfs`
```shell
mount 10.32.106.62:/code /tmp/code


rpcinfo -p 10.32.106.62 | egrep "portmapper|mountd|nfs"
telnet 10.32.106.62 2049
telnet 10.32.106.62 1234
telnet 10.32.106.62 111
```

NFS使用例:
- 用户: Credentials - UID, `/etc/passwd`
- 读取文件
- 写文件: async `UNSTABLE`, sync `FILE_SYNC`
- `mount -o noac`: 让客户端不缓存文件属性
  - 问题: 读写性能

> 从Wireshark看网络分层

例: NFS
- 应用层
- 传输层
- 网络层
- 数据链路层

MTU: 最大传输单元, 通常是1500字节
- TCP三次握手时告知对方MSS(Maximum Segment Size)
- MTU = TCP header length(20字节) + IP header length(20字节) + MSS
- 发包的大小是由MTU较小的一方决定的

> TCP的连接启蒙

例:
- 客户端: 10.32.106.159
- DNS服务器: 10.32.106.103
```shell
nslookup
# 默认使用UDP
# 强制使用TCP:
> set vc
```

参数:
- Seq, Len
  - Relative Sequence Number: Edit - Preferences - protocols - TCP
- Ack: 未丢包/乱序时, 等于发送方的下一个Seq
  - 可以累积确认: 当收到多个包的时候, 只需要确认最后一个就可以了.

标志位:
- SYN
- FIN
- RST

三次握手
- 两次握手? 在网络延迟严重时产生无效的连接

四次挥手

查看连接状态
- netstat
- Wireshark

> 快递员的工作策略-TCP窗口

送100个包裹到公司, 公司前台只能容20个, 电瓶车只能装10个.

- 发送窗口限制
  - 接收窗口: win/window size, 告知对方自己的接收窗口
  - 网络带宽限制

TCP Window Scale:
- TCP头中只给接收窗口16bit, 即最大65535字节
- 1992, RFC 1323, 三次握手时, TCP头的Options中放Window Scale: window scale/shift count

> 重传的讲究

网络拥塞: 网络中收到太多的数据
- 导致丢包
- 拥塞点: 能导致网络拥塞的数据量

发送方维护一个虚拟的拥塞窗口, 利用各种算法使它尽可能的接近真实的拥塞点.
- 连接刚建立时: 2/3/4个MSS
- 慢启动: 增大拥塞窗口, 每收到n个Ack, 增大n个MSS
- 拥塞避免: 拥塞窗口达到一个较大的值(临界窗口值). 每个往返时间增加1个MSS
  - 临界窗口值: 发生过拥塞的使用拥塞点作为参考, 没有发生过拥塞的使用最大接收窗口作为参考.

超时重传: 发送方等待一段时间后仍收不到Ack, 认定丢包, 只能重传.
- RTO: 从发出原始包到重传该包的这段时间.
- Analyze - Expert Info

发生重传之后的拥塞窗口:
- 降到1MSS
- 慢启动
  - 临界窗口值: (1) TCP/IP Illustrated 上次发生拥塞时发送窗口的一半 (2) RFC 5681 发生拥塞时没有被确认的数据量的一半, 但不小于2MSS.
- 拥塞避免

重复的Ack: 
- 3个或以后: 快速重传
  - 为了尽量避免因乱序而触发快速重传: 一般乱序的距离不会相差太大
  - 拥塞避免阶段发生快速重传: 快速恢复 - 临界窗口值设为拥塞时还没有被确认的数据量的一半, 将拥塞窗口设置为临界窗口值+3MSS, 暴扣在拥塞避免阶段 


如何调整RTO? 
- ref: [Linux TCP_RTO_MIN, TCP_RTO_MAX and the tcp_retries2 sysctl](https://pracucci.com/linux-tcp-rto-min-max-and-tcp-retries2.html)
- TCP_RTO_MIN: 200ms
- TCP_RTO_MAX: 120s

丢包时怎么重传
- NewReno: RFC 2582, RFC 3782
- SACK: RFC 2018

> 延迟确认与Nagle算法

延迟确认: TCP处理交互式场景, 减少了部分确认包, 减轻网络负担.
- 收到包后暂时没有数据发给对方, 延迟一段时间再确认

Nagle算法: 提高传输效率, 减轻网络负担.
- 在发出的数据还没有被确认之前, 假如有小数据生成, 就将小数据收集起来凑满一个MSS或收到确认后再发送.

> 百家争鸣

Westwood算法, Vegas算法

> 简单的代价-UDP

UDP头部: 8字节
- 不在乎双方MTU的大小
- 没有重传机制, 丢包由应用层处理
- 分片机制存在弱点: More fragments, 1表示后续还有分片, 0表示这是最后一个分片, 可以组装了.

> 剖析CIFS协议

微软维护
- SMB: Server Message Block, 服务器消息块
- CIFS: Common Internet File System, 网络文件共享系统

SKIP

> 网络江湖

NFS, CIFS的演化

> DNS小科普

记录类型:
- A: 从域名解析到IP地址
- PTR: 从IP地址解析到域名
- SRV: 指向域内资源
- CNAME: 又称Alias, 别名

工作方式:
- roles: 根服务器, 权威服务器, 不权威服务器
- 递归查询: 不权威服务器收到请求后查询权威服务器
- 迭代查询: 先查根服务器, 再从根服务器查权威服务器, 再从权威服务器查...

特性:
- 循环工作模式(round-robin): 两个同名A记录, 对应不同的IP, 用于负载均衡

缺陷:
- 山寨域名
- DNS服务器被恶意修改
- DNS服务器遭遇缓存投毒
- DNS放大攻击: `dig ANY isc.org`响应巨包, 伪造源地址请求

> 一个古老的协议-FTP

- 1971, Abhay Bhushan

控制端口: 21
- 控制连接
- 明文传输

数据连接: 需要传输数据时, 重新建立一个TCP连接, 传输结束时关闭连接
- 主动模式: 三次握手由服务端主动发起
- 被动模式: 三次握手由客户端主动发起

> 上网的学问-HTTP

Tim Berners-Lee

http://info.cern.ch/

Wireshark: 右键包 - Follow TCP Stream

HTTPS

> 无懈可击的Kerberos

身份认证协议
- Windows域环境的身份认证
- 双向认证
- 权威的第三方KDC: 知道域里所有账号和资源的密码

SKIP: 不感兴趣

> TCP/IP的故事

Vinton Cerf, Rober Kahn
- 阿帕网
- 20世纪70年代 TCP/IP协议

OSI
- Application/Presentation/Session/Transport/Network/DataLink/Physical
- 记忆: All People Seem To Need Data Processing

# 举重若轻

> "一小时内给你答复"

case: NFS
- NAT: Network Address Translation
  - 192.168.26.139 -> 10.32.200.45

> 午夜铃声

case: Isilon

乱序:
- NIC teaming
- LSO: Large Segment Offload

接收方的乱序

> 深藏功与名

case: Data Domain, AIX
- AIX SACK没有开启

> 棋逢对手

case: NFS访问文件偶尔卡一下
- 性能问题三板斧
  - Statistics - Summary
  - Statistics - Service Response Time
  - Analyze - Expoer Info Composite
- NLM(Network Locak Manager)没有回复: 防火墙策略 

> 学无止境

tshark


> 一个技术男的自白

...

-->