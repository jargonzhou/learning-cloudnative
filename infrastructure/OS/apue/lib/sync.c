#include "apue.h"
#include "apue_sync.h"
#include <signal.h>

/******************************************************************************
 * Process synchronization:
 *
 * PARENT -SIGUSR1-> CHILD
 * CHILD -SIGUSR2-> PARENT
 */

static volatile sig_atomic_t sigflag;
static sigset_t newmask, oldmask, zeromask;

// signal handler for SIGUSR1, SIGUSR2
static void
sig_usr (int signo)
{
  sigflag = 1;
}

void
TELL_WAIT (void)
{
  if (signal (SIGUSR1, sig_usr) == SIG_ERR)
    err_sys ("signal(SIGUSR1) error");
  if (signal (SIGUSR2, sig_usr) == SIG_ERR)
    err_sys ("signal(SIGUSR2) error");
  sigemptyset (&zeromask);
  sigemptyset (&newmask);
  sigaddset (&newmask, SIGUSR1);
  sigaddset (&newmask, SIGUSR2);

  // block SIGUSR1 and SIGUSR2, save current signal mask
  if (sigprocmask (SIG_BLOCK, &newmask, &oldmask) < 0)
    err_sys ("SIG_BLOCK error");
}

/*
 * Tell parent we are done.
 */
void
TELL_PARENT (pid_t pid)
{
  kill (pid, SIGUSR2);
}

/*
 * Tell child we are done.
 */
void
TELL_CHILD (pid_t pid)
{
  kill (pid, SIGUSR1);
}

/*
 * Wait for parent.
 */
void
WAIT_PARENT (void)
{
  while (sigflag == 0)
    sigsuspend (&zeromask);
  sigflag = 0;

  if (sigprocmask (SIG_SETMASK, &oldmask, NULL) < 0)
    err_sys ("SIG_SETMASK error");
}

/*
 * Wait for child.
 */
void
WAIT_CHILD (void)
{
  while (sigflag == 0)
    sigsuspend (&zeromask);
  sigflag = 0;

  if (sigprocmask (SIG_SETMASK, &oldmask, NULL) < 0)
    err_sys ("SIG_SETMASK error");
}

#if defined(MACOS)

int
pthread_barrier_init (pthread_barrier_t *barrier,
                      const pthread_barrierattr_t *attr, unsigned int count)
{
  if (count == 0)
    {
      errno = EINVAL;
      return -1;
    }
  if (pthread_mutex_init (&barrier->mutex, 0) < 0)
    {
      return -1;
    }
  if (pthread_cond_init (&barrier->cond, 0) < 0)
    {
      pthread_mutex_destroy (&barrier->mutex);
      return -1;
    }
  barrier->tripCount = count;
  barrier->count = 0;

  return 0;
}

int
pthread_barrier_destroy (pthread_barrier_t *barrier)
{
  pthread_cond_destroy (&barrier->cond);
  pthread_mutex_destroy (&barrier->mutex);
  return 0;
}

int
pthread_barrier_wait (pthread_barrier_t *barrier)
{
  pthread_mutex_lock (&barrier->mutex);
  ++(barrier->count);
  if (barrier->count >= barrier->tripCount)
    {
      barrier->count = 0;
      pthread_cond_broadcast (&barrier->cond);
      pthread_mutex_unlock (&barrier->mutex);
      return 1;
    }
  else
    {
      pthread_cond_wait (&barrier->cond, &(barrier->mutex));
      pthread_mutex_unlock (&barrier->mutex);
      return 0;
    }
}
#endif