#include "apue.h"
#include <syslog.h>

int
main (int argc, char *argv[])
{
  openlog ("ex_syslog", LOG_CONS, LOG_USER);
  syslog (LOG_INFO, "this is a syslog message");
  closelog ();

  exit (0);
}