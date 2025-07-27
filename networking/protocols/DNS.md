# DNS(Domain Name System)

books:
- TCP/IP Illustrated, Volume 1: The Protocols. - Chapter 11. Name Resolution and the Domain Name System (DNS)
- Liu, Cricket / Albitz, Paul. **DNS and BIND**. 2006, 5. edition. O'Reilly Media.
TLDs: top-level domains
- gTLDs: generic TLDs
  - generic
  - generic-restricted
  - sponsored
- ccTLDs: country-code TLDs
  - ex: uk, su, ac, eu, tp
- IDN ccTLDs: internationalized country-code TLDs
- ARPA: infrastructure TLD

IDNS: internationalized domain names
FQDNs: fully qualified domain names
# Protocol
- a query/response protocol used for perfoming queries against the DNS for particular names
- protocol for name servers to exchange database records (zone transfers)
- DNS notify: notify secondary servers taht the zone database has evolved and a zone transfer is necessary
- dynamic updates: dynamically update the zone
root servers: https://www.iana.org/domains/root/servers
## Message Format
* https://www.rfc-editor.org/rfc/rfc6895.txt
Querey/Response Headers:

```
                     1  1  1  1  1  1
 0  1  2  3  4  5  6  7  8  9  0  1  2  3  4  5
+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
|                      ID                       |
+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
|QR|   OpCode  |AA|TC|RD|RA| Z|AD|CD|   RCODE   |
+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
|                QDCOUNT/ZOCOUNT                | query/zone
+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
|                ANCOUNT/PRCOUNT                | answer/prerequisite
+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
|                NSCOUNT/UPCOUNT                | authority record/update
+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
|                    ARCOUNT                    | additional information
+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
```

- QR: query 0, response 1
- AA: authoritative answer
- TC: truncated answer
- RD: recursion desired
- RA: recursion available
- Z: zero
- AD: atuthentic data
- CD: checking disabled

OpCode:
- 0: query
- 4: notify
- 5: update

RCODE:
- 0: no error
- 1: format error
- 2: server failure
- 3: non-existent domain
- 4: not implemented
- 5: query refused 
Resource Records:

- question/query and zone section format
- answer, authority and additional information section format

```
                                1  1  1  1  1  1
  0  1  2  3  4  5  6  7  8  9  0  1  2  3  4  5
+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
|                                               |
/                                               /
/                     NAME                      / Query Name/Zone Name
/                                               /
+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
|                     TYPE                      | Query Type/Zone Type
+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
|                     CLASS                     | Query Class/Zone Class
+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
|                     TTL                       |
|                                               |
+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
|                   RDLENGTH                    |
+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--|
/                    RDATA                      /
/                                               /
+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+--+
```

NAME: names and labels
- data labels
- compression labels

TYPE:
- A: IPv4 address record
- AAAA: IPv6 address record
- NS: name server
- CNAME: canonical name - name aliasing
- SRV: server selection - transport endpoints of a generic service
- ...
- ANY: request for all records

CLASS:
- 1: Internet data

TTL

RDLNGTH

RDATA:


# Implementation
* [A List of Free and Public DNS Servers for 2025](https://www.lifewire.com/free-and-public-dns-servers-2626062)


|Provider|Primary DNS|Secondary DNS|
|---|---|---|
|Google|8.8.8.8|8.8.4.4|
|Control D|76.76.2.0|76.76.10.0|
|Quad9|9.9.9.9|149.112.112.112|
|OpenDNS Home|208.67.222.222|208.67.220.220|
|Cloudflare|1.1.1.1|1.0.0.1|
|AdGuard DNS|94.140.14.14|94.140.15.15|
|CleanBrowsing|185.228.168.9|185.228.169.9|
|Alternate DNS|76.76.19.19|76.223.122.150|
## BIND9
* https://gitlab.isc.org/isc-projects/bind9
* [Ubuntu Domain Name Service (DNS)](https://documentation.ubuntu.com/server/how-to/networking/install-dns/index.html)
```shell
# https://documentation.ubuntu.com/server/how-to/networking/install-dns/index.html
~ sudo apt install bind9
~ sudo apt-get install dnsutils
```
!sudo systemctl restart bind9.service
```shell
✗ dig -v
netmgr/netmgr.c:232:isc__netmgr_create(): fatal error: libuv version too new: running with libuv 1.43.0 when compiled with libuv 1.34.2 will lead to libuv failures
[1]    7331 IOT instruction (core dumped)  dig -v
# manual install libuv 1.34.2
# https://ubuntu.pkgs.org/20.04/ubuntu-main-amd64/libuv1_1.34.2-1ubuntu1_amd64.deb.html
~ sudo dpkg -i libuv1_1.34.2-1ubuntu1_amd64.deb
~ dig -v
DiG 9.18.30-0ubuntu0.20.04.2-Ubuntu
```
!python --version
!ip a
`spike.com`: directory `/etc/bind`

named.conf.local
```conf
include "/etc/bind/zones.spike.com";
include "/etc/bind/zones.r";
```

zones.spike.com
```conf
zone "spike.com"      { type master; file "/etc/bind/db.spike.com"; };
```

db.spike.com
```conf
;
; BIND data file for spike.com
;
$TTL	604800
@	IN	SOA	spike.com. root.spike.com. (
			      2		; Serial
			 604800		; Refresh
			  86400		; Retry
			2419200		; Expire
			 604800 )	; Negative Cache TTL
;
@	IN	NS	ns.spike.com.
@	IN	A	172.22.152.92
@	IN	AAAA	::1
ns IN A 172.22.152.92
```

zones.r
```conf
zone "92.152.22.in-addr.arpa"      { type master; file "/etc/bind/db.172"; };
```

db.127
```conf
;
; BIND reverse data file for local 172.22.152.92/20 net
;
$TTL	604800
@	IN	SOA	ns.spike.com. root.spike.com. (
			      2		; Serial
			 604800		; Refresh
			  86400		; Retry
			2419200		; Expire
			 604800 )	; Negative Cache TTL
;
@	IN	NS	ns.
10	IN	PTR	ns.spike.com.
```

/etc/resolv.conf
```conf
nameserver 172.22.152.92
search spike.com
```

!dig -x 127.0.0.1
# !dig ubuntu.com
# !dig spike.com
## CoreDNS
* https://coredns.io/

> CoreDNS: DNS and Service Discovery

actions:
- https://github.com/jargonzhou/application-store/tree/main/ops/coredns
!dig @192.168.3.178 -p 1053 -t SRV +all -q users.services.devops.org
# Clients


more:
- dig: DNS lookup utility
- nslookup: query Internet name servers interactively
- host: DNS lookup utility
- Wireshark
## dnsjava
* https://github.com/dnsjava/dnsjava

actions:
- workbench\DataEngineering\codes\data-engineering-java\infrastructure\wire\dns