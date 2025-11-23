#include "apue.h"
#include "apue_thd.h"

int
main (int argc, char *argv[])
{
  // char *cmd = "sleep 10 && uptime && echo \"xyz\"";
  char *cmd = "demo daemon";
  daemonize (cmd);

  for (;;)
    {
      printf (".");
      sleep (2);
    }
  exit (0);
}