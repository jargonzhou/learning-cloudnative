# Wireshak
* https://www.wireshark.org
  * [User’s Guide](https://www.wireshark.org/docs/wsug_html_chunked/)
	* [wiki](https://wiki.wireshark.org/)

> The world's leading network protocol analyzer
>
> Wireshark lets you dive deep into your network traffic - free and open source.

books:
- Wireshark网络分析就这么简单
- Wireshark数据包分析实战
- Wireshark网络分析的艺术

action version: `4.4.8 (v4.4.8-0-g0d289c003bfb)`. 
- `D:/software/Wireshark`.

# Terminology
- NIC: Network Interface Card, 网卡
- wire: 线路
- packet: 报文
- promiscuous mode: 混杂模式, 允许NIC查看所有流经线路上的报文
- endpoint: 在网络上发送或接收数据的设备
- convestation: 两个端点之间的通信

# Packet Capture
Capture > Options: 选择网络接口
- Input Tab: 可用于捕获报文的接口
- Output Tab: 允许自动保存捕获的报文到文件
- Options Tab: 报文捕获选项
	- Dispaly Options: 捕获的报文如何显示
	- Name Resolution: 名称解析(MAC, network, transport)
	- Stop Capture: 结束捕获选项

Edit > Preferences
- Appearance: 如何展示数据
- Capture: 报文捕获选项
- Filter Expressions: 流量过滤条件
- Name Resolution: 解析地址名称
	- MAC addresses: (1) ARP: MAC address => IP address, (2) ether files, (3) manufacture name
	- transport names: ex 80 => http
	- network(IP) address: IP => DNS name
- Protocols: 协议报文捕获和展示的选项
- RSA Keys
- Statistics: 统计特性的选项
- Advanced: 高级选项

View > Coloring Rules: 报文颜色

File >
- Save As: 保存捕获文件`.pcapng
- Export Specified Packets: 导出指定的报文
- Export Packet Dissections: 导出不同的格式
- Merge: 合并捕获文件

Main Window: Packet List, Packet Details, Packet Bytes

`Ctrl-F`: Find Packet bar
- display filter option
- hex value option
- string option

`Ctrl-M`: Mark Packet
- `Shift-Ctrl-N`, `Shift-Ctrl-B`: jump forward/backward between marked packets

File > Print: printing packets

time display format and references
- View > Time Display Format
	- presentation format options
	- precision options
- set a time reference to a packet
	- right-click the reference packet in Packet List pane: display `*REF*`
	- choose Set/Unset Time Reference
	- or `Ctrl-T`
- Edit > Time Shift: time shifting
	- or `Ctrl-Shift-T`

View > Name Resolution
- Edit Resolved Names: 指定名称
- Resolve Phisycal Addresses
- Resolve Network Addresses
- Resolve Transport Addresses

Protocol Dissection: 协议解剖, ex `wrongdissector.pcapng`
- 强制解码force decoded: right clike packet > Decode As... > Current
- code: [epan/dissectors](https://gitlab.com/wireshark/wireshark/-/tree/master/epan/dissectors), `packet-<protocol>.c`

Packet transcript/stream following: 报文抄本/跟随流, ex `http_google.pcapng`
- right click packet > Follow > XXX Stream
- XXX: TCP, UDP, SSL, HTTP
- SSL: Edit > Preference > Protocols > TLS

# Filters
捕获过滤器(Capture filters)
- Capture > Options > interface Capture Filter
- Capture > Capture Filters
	- 创建/保存捕获过滤器
- 由libpcap/WinPcap使用, 使用BPF语法

显示过滤器(Display filters)
- Filter text box at the top of the Packet List pane
- right click > Display Filter Expression
	- Field Name: 协议的条件字段
	- Relation: 关系
	- Quantifier
	- Value
	- Predefined Values
	- Range
- ribbon button left to Filter text box
	- Save this filter: 保存显示过滤器
	- Manage Display Filters: 关闭显示过滤器

## BPF Syntax/pcap-filter Syntax
使用BPF语法创建的过滤器: 表达式expression
- 由一个或多个原语primitive组成
		- 一个或多个限定符qualifier, ID name/number

qualtifier
- Type: 确定ID name/number的引用, 
	- `host`, `net`, `port`
- Dir: ID name/number的传输方向
	- `src`, `dst`
- Proto: 匹配特定协议
	- `ether`, `ip`, `tcp`, `udp`, `http`, `ftp`

过滤器示例
```BPF
src host 192.168.0.10 && port 80
```
hostname and addressing filters
```BPF
host 172.16.16.149
host 2001:db8:85a3::8a2e:370:7334
host testserver2
# MAC地址
ether host 00-1a-a0-52-e2-a0
# 源主机
src host 172.16.16.149
# 目标主机
dst host 172.16.16.149
# 默认为host
dst 172.16.16.149
```
port filters
```BPF
port 8080
!port 8080
dst port 80
```
protocol filters
```BPF
icmp
!ip6
```
protocol field filters
```BPF
# ICMP报文头部第一个字节: 类型字段, 值为3表示目标不可达
icmp[0] == 3
# 0: echo reply, 8: echo request
icmp[0] == 8 || icmp[0] == 0
# 两个字节: 3-目标不可达类型, 1-主机布尔打
icmp[0:2] == 0x0301

# TCP RST标志已设置
tcp[13] & 4 == 4
# TCP PSH标志已设置
tcp[13] & 8 == 8
```

more:
```BPF
tcp[13] & 32 == 32            # TCP packets with the URG flag set
tcp[13] & 16 == 16            # TCP packets with the ACK flag set
tcp[13] & 8 == 8              # TCP packets with the PSH flag set
tcp[13] & 4 == 4              # TCP packets with the RST flag set
tcp[13] & 2 == 2              # TCP packets with the SYN flag set
tcp[13] & 1 == 1              # TCP packets with the FIN flag set
tcp[13] == 18                 # TCP SYN-ACK packets
ether host 00:00:00:00:00:00  # Traffic to or from your MAC address
!ether host 00:00:00:00:00:00 # Traffic not to or from your MAC address
broadcast                     # Broadcast traffic only
icmp                          # ICMP traffic
icmp[0:2] == 0x0301           # ICMP destination unreachable, host unreachable
ip                            # IPv4 traffic only
ip6                           # IPv6 traffic only
udp                           # UDP traffic only
```

## Display Filter Syntax
more: [Display Filter Reference](https://www.wireshark.org/docs/dfref/)

- schema: `protocol.feature.subfeature`
- 比较操作符: ```
```
== != > < >= <=
```
- 逻辑操作符
```
and
or
xor # on and only one condition must be true
not
```

example
```BPF
ip.addr==192.168.0.1
frame.len<=128
ip.addr==192.168.0.1 or ip.addr==192.168.0.2

!tcp.port==3389              # Filter out RDP traffic
tcp.flags.syn==1             # TCP packets with the SYN flag set
tcp.flags.reset==1           # TCP packets with the RST flag set
!arp                         # Clear ARP traffic
http                         # All HTTP traffic
tcp.port==23 || tcp.port==21 # Telnet or FTP traffic
smtp || pop || imap          # Email traffic (SMTP, POP, or IMAP)
```


# Configuration
Help > About Wireshark > Folders
- Personal configuration: 个人配置 `~\AppData\Roaming\Wireshark`
- Global configuration: 全局配置 `D:\software\Wireshark`

Edit > Configuration Profiles
- 每个profile有独立的目录和一组配置文件
- preferences
- capture filters
- display filters
- coloring rules
- disabled protocols
- forced decodes
- recent settings: pane size, view menu settings, column width, etc
- protocol-specific tables: SNMP users, custom HTTP headers, etc

more: [B.2. Configuration File and Plugin Folders](file:///D:/software/Wireshark/Wireshark%20User's%20Guide/ChConfigurationPluginFolders.html)

# Statistics

Statistics >
- Capture File Properties
- Resolved Addresses: ex `lotsofweb.pcapng`
- Protocol Hierarchy: ex `lotsofweb.pcapng`
- Converstaion: ex `lotsofweb.pcapng`
- Endpoints: ex `lotsofweb.pcapng`
- Packet Lengths: ex `download-slow.pcapng`
	- Ethernet网络中最大帧为1518字节
	- Ethernet头部-14字节, IP头部-最小20字节, TPC报文(无数据或选项)-20字节
- I/O Graphs: ex `download-fast.pcapng`, `download-slow.pcapng`, `http_espn.pcapng`
- Service Response Time
- DNS
- Flow Graph: ex `dns_recursivequery_server.pcapng`
- TCP Stream Graphs
	- Time Sequence (Stevens)
	- Time Sequence (tcptrace)
	- Throughput
	- Round Time Trip Time Graph: ex `download-fast.pcapng`
	- Window Scaling
- UDP Multicast Streams

# Expert Information
Analyze > Expert Information: ex `download-slow.pcapng`
- Chat: 关于通信的基本信息
- Note: 常规通信中的不常见的报文
- Warning: 不属于常规通信中的不常见报文
- Error: 报文中错误或剖析器解释错误

more:
- [TCP Analysis](file:///D:/software/Wireshark/Wireshark%20User's%20Guide/ChAdvTCPAnalysis.html)
- [Customize the Wireshark Expert](https://www.chappell-university.com/post/customize-the-wireshark-expert)

## TCP Analysis
flags:
- TCP ACKed unseen segment
- TCP Dup ACK `<frame>#<acknowledgment number>`
- TCP Fast Retransmission
- TCP Keep-Alive
- TCP Keep-Alive ACK
- TCP Out-Of-Order
- TCP Port numbers reused
- TCP Previous segment not captured
- TCP Spurious Retransmission
- TCP Retransmission
- TCP Window Full
- TCP Window Update
- TCP ZeroWindow
- TCP ZeroWindowProbe
- TCP ZeroWindowProbeAck
- TCP Ambiguous Interpretations
- TCP Conversation Completeness

# Command line tools
- [ref](file:///D:/software/Wireshark/Wireshark%20User's%20Guide/AppTools.html)
- tshark: terminal based Wireshark
- tcpdump: capture with `tcpdump`
- dumpcap: capture with `dumpcap`
- capinfos: print information about capture files
- rawshark: dump and analyze network traffic
- editcap: edit capture files
- mergecap: merge multiple capture files into one
- text2pcap: converting ASCII hexdumps to network captures
- reordercap: reorder a capture file

## tcpdump
- [TcpDump](https://wiki.wireshark.org/TcpDump): [WinDump](https://www.winpcap.org/windump/)
	- Before running WinDump, you have to download and install WinPcap 3.1 or newer.
	- WinPcap Has Ceased Development. We recommend [Npcap](https://npcap.com/).
- [Nmap Packet Capture (Npcap)](https://wiki.wireshark.org/NPcap): Npcap is the Windows version of the libpcap library; it includes a driver to support capturing packets. Wireshark uses this library to capture live network data on Windows.

```shell
➜  ~ sudo tcpdump -h
tcpdump version 4.99.1
libpcap version 1.10.1 (with TPACKET_V3)
OpenSSL 3.0.2 15 Mar 2022
Usage: tcpdump [-AbdDefhHIJKlLnNOpqStuUvxX#] [ -B size ] [ -c count ] [--count]
                [ -C file_size ] [ -E algo:secret ] [ -F file ] [ -G seconds ]
                [ -i interface ] [ --immediate-mode ] [ -j tstamptype ]
                [ -M secret ] [ --number ] [ --print ] [ -Q in|out|inout ]
                [ -r file ] [ -s snaplen ] [ -T type ] [ --version ]
                [ -V file ] [ -w file ] [ -W filecount ] [ -y datalinktype ]
                [ --time-stamp-precision precision ] [ --micro ] [ --nano ]
                [ -z postrotate-command ] [ -Z user ] [ expression ]
```

```shell
➜  ~ ifconfig
eth1: flags=4163<UP,BROADCAST,RUNNING,MULTICAST>  mtu 1500
        inet 192.168.0.104  netmask 255.255.255.0  broadcast 192.168.0.255
        inet6 fe80::3155:5954:f582:f646  prefixlen 64  scopeid 0x20<link>
        inet6 240e:ec:6665:e228:6d96:9c6a:2221:3b6e  prefixlen 128  scopeid 0x0<global>
        inet6 240e:ec:6665:e228:e6b0:7694:12f4:2537  prefixlen 64  scopeid 0x0<global>
        inet6 240e:ec:6665:e228::1002  prefixlen 128  scopeid 0x0<global>
        ether b0:dc:ef:9e:9a:4e  txqueuelen 1000  (Ethernet)
        RX packets 115  bytes 9638 (9.6 KB)
        RX errors 0  dropped 0  overruns 0  frame 0
        TX packets 221  bytes 20646 (20.6 KB)
        TX errors 0  dropped 0 overruns 0  carrier 0  collisions 0

lo: flags=73<UP,LOOPBACK,RUNNING>  mtu 65536
        inet 127.0.0.1  netmask 255.0.0.0
        inet6 ::1  prefixlen 128  scopeid 0x10<host>
        loop  txqueuelen 1000  (Local Loopback)
        RX packets 168  bytes 21577 (21.5 KB)
        RX errors 0  dropped 0  overruns 0  frame 0
        TX packets 168  bytes 21577 (21.5 KB)
        TX errors 0  dropped 0 overruns 0  carrier 0  collisions 0

loopback0: flags=4163<UP,BROADCAST,RUNNING,MULTICAST>  mtu 1500
        ether 00:15:5d:d9:6a:80  txqueuelen 1000  (Ethernet)
        RX packets 56  bytes 5428 (5.4 KB)
        RX errors 0  dropped 0  overruns 0  frame 0
        TX packets 48  bytes 3648 (3.6 KB)
        TX errors 0  dropped 0 overruns 0  carrier 0  collisions 0

➜  ~ sudo tcpdump -i eth1
tcpdump: verbose output suppressed, use -v[v]... for full protocol decode
listening on eth1, link-type EN10MB (Ethernet), snapshot length 262144 bytes
...
```

## TShark
- [tshark(1) Manual Page](file:///D:/software/Wireshark/tshark.html)

```shell
$ tshark -v
TShark (Wireshark) 4.4.8 (v4.4.8-0-g0d289c003bfb).

$ tshark -D
1. \Device\NPF_{996AAB6E-9019-42DD-B7CC-0DA4D7213085} (本地连接* 10)
2. \Device\NPF_{9420CA43-6B3A-4850-B62A-478A0104FFD5} (本地连接* 9)
3. \Device\NPF_{32B81144-50EA-49D3-AC64-1B4EF5033929} (本地连接* 8)
4. \Device\NPF_{C656440C-905E-4D16-A4AB-BA000A609555} (WLAN)
5. \Device\NPF_{EAB3F521-8294-461E-9A8C-38E63361BF02} (本地连接* 2)
6. \Device\NPF_{110A48EF-A940-4DBA-8837-8EC8DA0C8F7A} (本地连接* 1)
7. \Device\NPF_Loopback (Adapter for loopback traffic capture)
8. \Device\NPF_{1ABF106B-BE4D-49E2-9661-145275B5F1C5} (以太网 2)
9. ciscodump (Cisco remote capture)
10. etwdump (Event Tracing for Windows (ETW) reader)
11. randpkt (Random packet generator)
12. sshdump.exe (SSH remote capture)
13. udpdump (UDP Listener remote capture)
14. wifidump.exe (Wi-Fi remote capture)

$ tshark -i 7
Capturing on 'Adapter for loopback traffic capture'
...
```

```shell
# write to file
tshark -i 7 -w packets.pcap
tcpdump -i eth1 -w packets.pcap
# capture only first 10 packets
tshark -i 7 -w packets.pcap -c10
tcpdump -i eth1 -w packets.pcap -c10

# read from file
tshark -r packets.pcap
tcpdump -r packets.pcap
# show only first 10 packets
tshark -r packets.pcap -c10
tcpdump -r packets.pcap -c10
```

output
```shell
# TCP
# [Timestamp] [Layer 3 Protocol] [Source IP].[Source Port] > [Destination IP]. [Destination Port]: [TCP Flags], [TCP Sequence Number], [TCP Acknowledgement Number], [TCP Windows Size], [Data Length]
Capturing on 'Adapter for loopback traffic capture'
    1   0.000000    127.0.0.1 49765 127.0.0.1    53231 TCP 45 49765 → 53231 [ACK] Seq=1 Ack=1 Win=254 Len=1
    2   0.000110    127.0.0.1 53231 127.0.0.1    49765 TCP 56 53231 → 49765 [ACK] Seq=1 Ack=2 Win=253 Len=0 SLE=1 SRE=2

# UDP
# [Timestamp] [Layer 3 Protocol] [Source IP].[Source Port] > [Destination IP]. [Destination Port]: [Layer 4 Protocol], [Data Length]
```
```shell
# verbosity
tshark –r packets.pcap –V
tcpdump –r packets.pcap –vvv

# x: ASCII representation of packets
tshark –xr packets.pcap
tcpdump –Xr packets.pcap
```
name resolution: ex `tcp_ports.pcapng`
```shell
# -n: disable
tshark –ni 1
# -N: enable except
# m: MAC address resolution
# n: network address resolution
# t: transport port name resolution
# N: use external resolver
# C: concurrent DNS lookup
tshark –i 1 –Nt 
tshark –i 1 -Ntm 

# -n: disable IP name resolution
# -nn: disable port name resolution
tcpdump –nni eth1
```
filters:
```shell
# capture filter: BPF syntax
tshark –ni 1 –w packets.pcap –f "tcp port 80"

# display filter: Wireshark filter syntax
tshark –ni 1 –w packets.pcap –Y "tcp.dstport == 80"
tshark –r packets.pcap –Y "tcp.dstport == 80"

# capture
tcpdump –nni eth0 –w packets.pcap 'tcp dst port 80'
# display
tcpdump –r packets.pcap 'tcp dst port 80'
# save subset
tcpdump –r packets.pcap 'tcp dst port 80' –w http_packets.pcap
# ref a BPF file containing filters
tcpdump –nni eth0 –F dns_servers.bpf
```
time display format
```shell
# default: timestamp relative to the start of capture
# -t
# a: absolute time(your timezone)
# ad: absolute date and time(your timezone)
# d: delta since previous captured packet
# dd: delta since previous displayed packet
# e: epoch time
# r: elapsed time between first packet and current packet
# u: absolute time(UTF)
# ud: absolute date and time(UTC)
tshark –r packets.pcap –t ad
```
summary statistics: ex `http_google.pcapng`
```shell
# -z
# options:
# ip_hosts,tree: rate and percentage of traffic each IP address
# ip,phs: display protocol hierarchy
# http,tree: statistics about HTTP requests and responses
# heep_req,tree: statistics about HTTP requests
# smb,srt: statistics about SMB command for analyzing Windows communination - Server Message Block (SMB) 
# endpoints,wlan: display wireless endpoints
# exprt: display expert information
tshark -r packets.pcap –z conv,ip
tshark -r packets.pcap –z http,tree
tshark -r http_google.pcap -z follow,tcp,ascii,0
tshark –r packets.pcap –z follow,udp,ascii,192.168.1.5:23429,4.2.2.1:53
```

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