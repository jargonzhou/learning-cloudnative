#include "apue.h"
#include <errno.h>
#include <signal.h>
#include <sys/wait.h>

/*
 * Print exist status.
 */
void
pr_exit (int status)
{
  if (WIFEXITED (status))
    printf ("normal termination, exit status = %d\n", WEXITSTATUS (status));
  else if (WIFSIGNALED (status))
    printf ("abnormal termination, signal number = %d%s\n", WTERMSIG (status),
#ifdef WCOREDUMP
            WCOREDUMP (status) ? " (core file generated)" : "");
#else
            "");
#endif
  else if (WIFSTOPPED (status))
    printf ("child stopped, signal number = %d\n", WSTOPSIG (status));
}

/*
 * Print masked signal names.
 */
void
pr_mask (const char *str)
{
  sigset_t sigset;
  int errno_save;

  errno_save = errno;
  if (sigprocmask (0, NULL, &sigset) < 0)
    err_ret ("sigprocmask error");
  else
    {
      printf ("%s", str);
      if (sigismember (&sigset, SIGABRT))
        printf (" SIGABRT");
      if (sigismember (&sigset, SIGALRM))
        printf (" SIGALRM");
      if (sigismember (&sigset, SIGBUS))
        printf (" SIGBUS");
      if (sigismember (&sigset, SIGCHLD))
        printf (" SIGCHLD");
      if (sigismember (&sigset, SIGCONT))
        printf (" SIGCONT");
      if (sigismember (&sigset, SIGFPE))
        printf (" SIGFPE");
      if (sigismember (&sigset, SIGHUP))
        printf (" SIGHUP");
      if (sigismember (&sigset, SIGILL))
        printf (" SIGILL");
      if (sigismember (&sigset, SIGINT))
        printf (" SIGINT");
      if (sigismember (&sigset, SIGKILL))
        printf (" SIGKILL");
      if (sigismember (&sigset, SIGPIPE))
        printf (" SIGPIPE");
      if (sigismember (&sigset, SIGQUIT))
        printf (" SIGQUIT");
      if (sigismember (&sigset, SIGSEGV))
        printf (" SIGSEGV");
      if (sigismember (&sigset, SIGSTOP))
        printf (" SIGSTOP");
      if (sigismember (&sigset, SIGTERM))
        printf (" SIGTERM");
      if (sigismember (&sigset, SIGTSTP))
        printf (" SIGTSTP");
      if (sigismember (&sigset, SIGTTIN))
        printf (" SIGTTIN");
      if (sigismember (&sigset, SIGTTOU))
        printf (" SIGTTOU");
      if (sigismember (&sigset, SIGUSR1))
        printf (" SIGUSR1");
      if (sigismember (&sigset, SIGUSR2))
        printf (" SIGUSR2");
#if (defined(_POSIX_C_SOURCE) && !defined(_DARWIN_C_SOURCE))
      if (sigismember (&sigset, SIGPOLL))
        printf (" SIGPOLL");
#endif
      if (sigismember (&sigset, SIGPROF))
        printf (" SIGPROF");
      if (sigismember (&sigset, SIGSYS))
        printf (" SIGSYS");
      if (sigismember (&sigset, SIGTRAP))
        printf (" SIGTRAP");
      if (sigismember (&sigset, SIGURG))
        printf (" SIGURG");
      if (sigismember (&sigset, SIGVTALRM))
        printf (" SIGVTALRM");
      if (sigismember (&sigset, SIGXCPU))
        printf (" SIGXCPU");
      if (sigismember (&sigset, SIGXFSZ))
        printf (" SIGXFSZ");

      printf ("\n");
    }

  errno = errno_save; // restore errno
}