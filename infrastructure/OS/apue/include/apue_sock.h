#ifndef INCLUDE_APUE_SOCK
#define INCLUDE_APUE_SOCK

#include <netdb.h>
#include <sys/socket.h>

// sock.c

/*
 * Retryable connect();
 *
 * domain: AF_INET, AF_INET6, AF_UNIX, AF_UPSPEC
 * type: SOCK_DGRAM, SOCK_RAW, SOCK_SEQPACKET, SOCK_STREAM
 * protocol: IPPROTO_IP, IPPROTO_IP6, IPPROTO_ICMP, IPPROTO_RAW, IPPROTO_TCP,
 * IPPROTO_UDP
 * addr: socket address
 * alen: length of socket address
 */
int connect_retry(int domain, int type, int protocol,
                  const struct sockaddr *addr, socklen_t alen);

/*
 * Allocate and initialize socket for server.
 *
 * type: SOCK_DGRAM, SOCK_RAW, SOCK_SEQPACKET, SOCK_STREAM
 * addr: socket address
 * alen: length of socket address
 * qlen: backlog
 */
int initserver(int type, const struct sockaddr *addr, socklen_t alen, int qlen);

#endif /* INCLUDE_APUE_SOCK */
