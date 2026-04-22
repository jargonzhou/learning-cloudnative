#include "apue.h"
#include <setjmp.h>
#include <signal.h>
#include <time.h>

static void sig_usr1 (int);
static void sig_alrm (int);

static sigjmp_buf jmpbuf;
static volatile sig_atomic_t canjmp; // guard jmpbuf

int
main (int argc, char *argv[])
{
  if (signal (SIGUSR1, sig_usr1) == SIG_ERR)
    err_sys ("signal(SIGUSR1) error");
  if (signal (SIGALRM, sig_alrm) == SIG_ERR)
    err_sys ("signal(SIGALRM) error");

  pr_mask ("strating main: ");
  if (sigsetjmp (jmpbuf, 1))
    {
      pr_mask ("ending main: ");
      exit (0);
    }
  canjmp = 1;

  for (;;)
    pause ();

  exit (0);
}

static void
sig_usr1 (int signo)
{
  time_t starttime;

  if (canjmp == 0)
    return;

  alarm (3);
  starttime = time (NULL);
  for (;;)
    if (time (NULL) > starttime + 5)
      break;

  pr_mask ("finishing sig_usr1: ");

  canjmp = 0;
  siglongjmp (jmpbuf, 1);
}

static void
sig_alrm (int signo)
{
  pr_mask ("in sig_alrm: ");
}