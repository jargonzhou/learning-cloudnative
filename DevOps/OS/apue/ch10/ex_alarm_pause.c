#include "apue.h"
#include <setjmp.h>
#include <signal.h>

static jmp_buf env_alrm;

static void
sig_alrm (int signo)
{
  longjmp (env_alrm, 1);
}

unsigned int
_sleep (unsigned int seconds)
{
  if (signal (SIGALRM, sig_alrm) == SIG_ERR)
    return seconds;
  if (setjmp (env_alrm) == 0)
    {
      alarm (seconds);
      pause (); // wake up when next caught signal
    }
  return (alarm (0)); // return unslept seconds
}

static void
sig_int (int signo)
{
  int i, j;
  volatile int k;

  k = 0;
  printf ("sig_int statring\n");
  for (i = 0; i < 300000; i++)
    for (j = 0; j < 4000; j++)
      k += i * j;
  printf ("sig_int finished\n");
}

int
main (int argc, char *argv[])
{
  unsigned int unslept;
  if (signal (SIGINT, sig_int) == SIG_ERR)
    err_sys ("signal(SIGINT) error");
  unslept = _sleep (5);
  printf ("_sleep returned: %u\n", unslept);

  exit (0);
}
