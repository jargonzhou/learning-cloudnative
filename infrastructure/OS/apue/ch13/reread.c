#include "apue.h"
#include "apue_thd.h"
// #include "log.h" // log_trace
#include <errno.h>
#include <fcntl.h> // open
#include <signal.h>
#include <string.h>
#include <syslog.h> // syslog
void
reread (void)
{
  printf ("in reread\n");
}

void
sigterm (int signo)
{
  syslog (LOG_INFO, "got SIGTERM; exiting");
  // log_trace ("got SIGTERM; exiting");
  exit (0);
  // err_quit ("got SIGTERM; exiting");
}

void
siguhup (int signo)
{
  syslog (LOG_INFO, "Re-reading configuration file");
  // log_trace ("Re-reading configuration file");
  // printf ("Re-reading configuration file\n");

  reread ();
}

int
main (int argc, char *argv[])
{
  // intialize logging
  // FILE *fp = fopen ("reread.log", "a+");
  // log_add_fp (fp, 0);

  char *cmd;
  struct sigaction sa;
  // in case of can not open reread.pid: Read-only file system
  const char *lockfilepath = "/tmp/reread.pid";

  if ((cmd = strrchr (argv[0], '/')) == NULL)
    cmd = argv[0];
  else
    cmd++;

  // log_trace ("cmd=%s", cmd);
  daemonize (cmd);
  // log_trace ("daemonized");

  openlog ("reread", LOG_CONS, LOG_USER);
  if (already_running (lockfilepath))
    {
      syslog (LOG_ERR, "daemon already running");
      // log_trace ("daemon already running");
      exit (1);
      // err_quit ("daemon already running");
    }

  // handle signal of interest
  sa.sa_handler = sigterm;
  sigemptyset (&sa.sa_mask);
  sigaddset (&sa.sa_mask, SIGHUP);
  sa.sa_flags = 0;
  if (sigaction (SIGTERM, &sa, NULL) < 0)
    {
      syslog (LOG_ERR, "can not catch SIGTERM: %s", strerror (errno));
      // log_trace ("can not catch SIGTERM: %s", strerror (errno));
      exit (1);
      // err_quit ("can not catch SIGTERM: %s", strerror (errno));
    }
  sa.sa_handler = siguhup;
  sigemptyset (&sa.sa_mask);
  sigaddset (&sa.sa_mask, SIGTERM);
  sa.sa_flags = 0;
  if (sigaction (SIGHUP, &sa, NULL) < 0)
    {
      syslog (LOG_ERR, "can not catch SIGHUP: %s", strerror (errno));
      // log_trace ("can not catch SIGHUP: %s", strerror (errno));
      exit (1);
      // err_quit ("can not catch SIGHUP: %s", strerror (errno));
    }

  for (;;)
    {
      sleep (2);
    }

  exit (0);
}