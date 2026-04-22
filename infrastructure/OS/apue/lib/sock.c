#include "apue_sock.h"
#include <errno.h>
#include <unistd.h>

#define MAXSLEEP 128

int
connect_retry (int domain, int type, int protocol, const struct sockaddr *addr,
               socklen_t alen)
{
  int numsec, fd;

  // try to connect with exponentital backoff
  for (numsec = 1; numsec < MAXSLEEP; numsec <<= 1)
    {
      if ((fd = socket (domain, type, protocol)) < 0)
        return (-1);
      if (connect (fd, addr, alen) == 0)
        return (fd);

      // if connect() failed, status of socket is undefined
      close (fd);

      if (numsec <= MAXSLEEP / 2)
        sleep (numsec);
    }

  return (-1);
}

int
initserver (int type, const struct sockaddr *addr, socklen_t alen, int qlen)
{
  int fd;
  int err = 0;
  int reuse = 1; // reuse address

  if ((fd = socket (addr->sa_family, type, 0)) < 0)
    return (-1);
  if (setsockopt (fd, SOL_SOCKET, SO_REUSEADDR, &reuse, sizeof (int)) < 0)
    goto errout;
  if (bind (fd, addr, alen) < 0)
    goto errout;
  if (type == SOCK_STREAM || type == SOCK_SEQPACKET)
    if (listen (fd, qlen) < 0)
      goto errout;

  return (fd);

errout:
  err = errno;
  close (fd);
  errno = err;
  return (-1);
}