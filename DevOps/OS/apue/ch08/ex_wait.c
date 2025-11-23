#include "apue.h"
#include <sys/wait.h>

int
main (int argc, char *argv[])
{
  pid_t pid;
  int status;

  if ((pid = fork ()) < 0)
    err_sys ("fork error");
  else if (pid == 0) // child
    exit (7);

  if (wait (&status) != pid)
    err_sys ("wait error");
  pr_exit (status);

  if ((pid = fork ()) < 0)
    err_sys ("fork error");
  else if (pid == 0)
    abort (); // generate SIGABRT

  if (wait (&status) != pid)
    err_sys ("wait error");
  pr_exit (status);

  if ((pid = fork ()) < 0)
    err_sys ("fork error");
  else if (pid == 0)
    status /= 0; // generate SIGFPE

  if (wait (&status) != pid)
    err_sys ("wait error");
  pr_exit (status);

  return 0;
}