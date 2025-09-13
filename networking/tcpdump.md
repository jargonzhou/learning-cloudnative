# tcpdump
* https://www.tcpdump.org/ 
* [tcpdump(1) man page](https://www.tcpdump.org/manpages/tcpdump.1.html): dump traffic on a network
  * [tcpdump(8) — Linux manual page](https://man7.org/linux/man-pages/man8/tcpdump.8.html)
* [pcap-filter(7) man page](https://www.tcpdump.org/manpages/pcap-filter.7.html): packet filter syntax

> This is the home web site of `tcpdump`, a powerful command-line packet analyzer; and `libpcap`, a portable C/C++ library for network traffic capture.
> 
> tcpdump, libpcap
> 
> - [tcpdump](https://www.tcpdump.org/manpages/tcpdump.1.html) - dump traffic on a network
> - [pcap-filter](https://www.tcpdump.org/manpages/pcap-filter.7.html) - packet filter syntax
> - [pcap](https://www.tcpdump.org/manpages/pcap.3pcap.html) - Packet Capture library
> - [rpcapd](https://www.tcpdump.org/manpages/rpcapd.8.html) - capture daemon to be controlled by a remote libpcap application

## TCP SYN, ACK, FIN
- [How to capture TCP SYN, ACK and FIN packets with tcpdump](https://www.xmodulo.com/capture-tcp-syn-ack-fin-packets-tcpdump.html)
- [How to capture ack or syn packets by Tcpdump?](https://serverfault.com/questions/217605/how-to-capture-ack-or-syn-packets-by-tcpdump)
- [TCP flags](https://gist.github.com/tuxfight3r/9ac030cb0d707bb446c7)

```
                     TCPDUMP FLAGS
Unskilled =  URG  =  (Not Displayed in Flag Field, Displayed elsewhere) 
Attackers =  ACK  =  (Not Displayed in Flag Field, Displayed elsewhere)
Pester    =  PSH  =  [P] (Push Data)
Real      =  RST  =  [R] (Reset Connection)
Security  =  SYN  =  [S] (Start Connection)
Folks     =  FIN  =  [F] (Finish Connection)
          SYN-ACK =  [S.] (SynAcK Packet)
                     [.] (No Flag Set)
```

```shell
# SYN
tcpdump -i <interface> "tcp[tcpflags] & (tcp-syn) != 0"
# ACK
tcpdump -i <interface> "tcp[tcpflags] & (tcp-ack) != 0"
# FIN
tcpdump -i <interface> "tcp[tcpflags] & (tcp-fin) != 0"
# SYN or ACK
tcpdump -r <interface> "tcp[tcpflags] & (tcp-syn|tcp-ack) != 0"

tcpdump -i any -c100 -nn -Av 'port 3000 & tcp[tcpflags] & (tcp-syn|tcp-ack|tcp-fin) != 0
```
