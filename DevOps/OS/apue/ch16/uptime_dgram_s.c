#include "apue.h"
#include "apue_sock.h"
#include "apue_thd.h"
#include "log.h"
#include <errno.h>
#include <syslog.h>

#define BUFLEN 128
#define MAXADDRLEN 256

#ifndef HOST_NAME_MAX
#define HOST_NAME_MAX 256
#endif

void
serve (int sockfd)
{
  int n;
  socklen_t alen;
  FILE *fp;
  char buf[BUFLEN];
  char abuf[MAXADDRLEN];
  struct sockaddr *addr = (struct sockaddr *)abuf;

  set_cloexec (sockfd);
  for (;;)
    {
      alen = MAXADDRLEN;
      if ((n = recvfrom (sockfd, buf, BUFLEN, 0, addr, &alen)) < 0)
        {
          syslog (LOG_ERR, "uptime_dgram_s: recvfrom error: %s",
                  strerror (errno));
          log_trace ("uptime_dgram_s: recvfrom error: %s", strerror (errno));
          exit (1);
        }

      if ((fp = popen ("/usr/bin/uptime", "r")) == NULL)
        {
          sprintf (buf, "error: %s\n", strerror (errno));
          sendto (sockfd, buf, strlen (buf), 0, addr, alen);
        }
      else
        {
          if (fgets (buf, BUFLEN, fp) != NULL)
            sendto (sockfd, buf, strlen (buf), 0, addr, alen);
          pclose (fp);
        }
    }
}

int
main (int argc, char *argv[])
{
  // intialize logging
  FILE *fp = fopen ("uptime_dgram_s.log", "a+");
  log_add_fp (fp, 0);

  struct addrinfo *ailist, *aip;
  struct addrinfo hint;
  int sockfd, err, n;
  char *host;

  if ((n = sysconf (_SC_HOST_NAME_MAX)) < 0)
    n = HOST_NAME_MAX;
  if ((host = malloc (n)) == NULL)
    err_sys ("malloc error");
  // if (gethostname (host, n))
  // err_sys ("gethostname error");
  host = "localhost";

  daemonize ("uptime_dgram_s");

  memset (&hint, 0, sizeof (hint));
  hint.ai_flags = AI_CANONNAME;
  hint.ai_socktype = SOCK_DGRAM;
  hint.ai_canonname = NULL;
  hint.ai_addr = NULL;
  hint.ai_next = NULL;

  if ((err = getaddrinfo (host, "uptime_dgram_s", &hint, &ailist)) != 0)
    {
      syslog (LOG_ERR, "uptime_dgram_s: getaddrinfo error: %s",
              gai_strerror (err));
      log_trace ("uptime_dgram_s: getaddrinfo error: %s", gai_strerror (err));
      exit (1);
    }
  for (aip = ailist; aip != NULL; aip = aip->ai_next)
    {
      if ((sockfd = initserver (SOCK_DGRAM, aip->ai_addr, aip->ai_addrlen, 0))
          > 0)
        {
          serve (sockfd);
          exit (0);
        }
    }

  exit (1);
}