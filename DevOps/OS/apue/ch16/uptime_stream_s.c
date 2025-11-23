#include "apue.h"
#include "apue_sock.h"
#include "apue_thd.h"
#include "log.h"
#include <errno.h>
#include <syslog.h>

#define BUFLEN 128
#define QLEN 10

#ifndef HOST_NAME_MAX
#define HOST_NAME_MAX 256
#endif

void
serve (int sockfd)
{
  int clfd;
  FILE *fp;
  char buf[BUFLEN];

  set_cloexec (sockfd);
  for (;;)
    {
      if ((clfd = accept (sockfd, NULL, NULL)) < 0)
        {
          syslog (LOG_ERR, "uptime: accept error: %s", strerror (errno));
          log_trace ("uptime: accept error: %s", strerror (errno));
          exit (1);
        }
      set_cloexec (clfd);
      if ((fp = popen ("/usr/bin/uptime", "r")) == NULL)
        {
          sprintf (buf, "error: %s\n", strerror (errno));
          send (clfd, buf, strlen (buf), 0);
        }
      else
        {
          while (fgets (buf, BUFLEN, fp) != NULL)
            send (clfd, buf, strlen (buf), 0);
          pclose (fp);
        }
      close (clfd);
    }
}

int
main (int argc, char *argv[])
{
  // intialize logging
  FILE *fp = fopen ("uptime_stream_s.log", "a+");
  log_add_fp (fp, 0);

  struct addrinfo *ailist, *aip;
  struct addrinfo hint;
  int sockfd, err, n;
  char *host;

  if (argc != 1)
    err_quit ("usage: uptime_s");
  if ((n = sysconf (_SC_HOST_NAME_MAX)) < 0)
    n = HOST_NAME_MAX;
  if ((host = malloc (n)) == NULL)
    err_sys ("malloc error");
  if (gethostname (host, n) < 0)
    err_sys ("gethostname error");

  host = "localhost";
  log_trace ("host=%s", host);

  daemonize ("ch16/uptime_stream_s");
  log_trace ("daemonized");

  memset (&hint, 0, sizeof (hint));
  hint.ai_flags = AI_CANONNAME;
  hint.ai_socktype = SOCK_STREAM;
  hint.ai_canonname = NULL;
  hint.ai_addr = NULL;
  hint.ai_next = NULL;
  if ((err = getaddrinfo (host, "uptime_stream_s", &hint, &ailist)) != 0)
    {
      syslog (LOG_ERR, "uptime_stream_s: getaddrinfo error: %s",
              gai_strerror (err));
      log_trace ("uptime_stream_s: getaddrinfo error: %s\n",
                 gai_strerror (err));
      exit (1);
    }
  log_trace ("ailist==NULL? %d\n", ailist == NULL);
  for (aip = ailist; aip != NULL; aip = aip->ai_next)
    {
      log_trace ("name=%s\n", aip->ai_canonname);
      sockfd = initserver (SOCK_STREAM, aip->ai_addr, aip->ai_addrlen, QLEN);
      if (sockfd >= 0)
        {
          serve (sockfd);
          exit (0);
        }
    }

  exit (1);
}