#include "apue.h"
#include "apue_thd.h"
#include <errno.h>
#include <fcntl.h>
#include <syslog.h>

int
main (int argc, char *argv[])
{
  int r;
  const char *lockfilepath = "/var/run/singleton.pid";

  r = already_running (lockfilepath);
  if (r == 0)
    {
      printf ("not running\n");
    }
  else
    {
      err_quit ("already running");
    }

  for (;;)
    {
      // do work
      sleep (2);
      printf (".");
    }

  exit (0);
}