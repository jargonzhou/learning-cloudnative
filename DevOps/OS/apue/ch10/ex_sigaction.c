#include "apue.h"
#include <signal.h>

typedef void Sigfunc (int); /* for signal handlers */

static void
sig_usr (int signo)
{
  if (signo == SIGUSR1)
    printf ("received SIGUSR1\n");
  else if (signo == SIGUSR2)
    printf ("received SIGUSR2\n");
  else
    err_dump ("received signal %d\n", signo);
}

// implement singal() using sigaction()
Sigfunc *
_signal (int signo, Sigfunc *func)
{
  struct sigaction act, oact;

  act.sa_handler = func;
  sigemptyset (&act.sa_mask);
  act.sa_flags = 0;
  if (signo == SIGALRM)
    {
#ifdef SA_INTERRUPT
      act.sa_flags |= SA_INTERRUPT;
#endif
    }
  else
    act.sa_flags |= SA_RESTART;

  if (sigaction (signo, &act, &oact) < 0)
    return SIG_ERR;

  return oact.sa_handler;
}

int
main (int argc, char *argv[])
{
  if (_signal (SIGUSR1, sig_usr) == SIG_ERR)
    err_sys ("can not catch SIGUSR1");
  if (_signal (SIGUSR2, sig_usr) == SIG_ERR)
    err_sys ("can not catch SIGUSR2");

  for (;;)
    pause ();
  exit (0);
}
