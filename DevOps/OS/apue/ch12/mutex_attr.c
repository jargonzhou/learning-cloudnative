#include "apue.h"
#include "apue_thd.h"
#include <pthread.h>
#include <sys/time.h>
#include <time.h>

struct to_info
{
  void (*to_fn) (void *);  // function
  void *to_arg;            // argument
  struct timespec to_wait; // time to wait
};

#define SECTONSEC 1000000000 // seconds to nanoseconds

#if !defined(CLOCK_REALTIME)
#define clock_nanosleep(ID, FL, REQ, REM) nanosleep ((REQ), (REM))
#endif

#ifndef CLOCK_REALTIME
#define CLOCK_REALTIME 0
#define USECTONSEC 1000 // microseconds to nanoseconds

void
clock_gettime (int id, struct timespec *tsp)
{
  struct timeval tv;

  gettimeofday (&tv, NULL);
  tsp->tc_sec = tv.tv_sec;
  tsp->tv_nsec = tv.tv_usec * USECTONSEC;
}
#endif

pthread_mutexattr_t attr;
pthread_mutex_t mutex;

void *
timeout_helper (void *arg)
{
  printf ("timeout_helper start\n");
  struct to_info *tip;

  tip = (struct to_info *)arg;
  clock_nanosleep (CLOCK_REALTIME, 0, &tip->to_wait, NULL);
  (*tip->to_fn) (tip->to_arg);
  free (arg);

  printf ("timeout_helper end\n");
  return (0);
}

void
timeout (const struct timespec *when, void (*func) (void *), void *arg)
{
  struct timespec now;
  struct to_info *tip;
  int err;

  clock_gettime (CLOCK_REALTIME, &now);
  if ((when->tv_sec > now.tv_sec)
      || (when->tv_sec == now.tv_sec && when->tv_nsec > now.tv_nsec))
    {
      tip = malloc (sizeof (struct to_info));
      if (tip != NULL)
        {
          tip->to_fn = func;
          tip->to_arg = arg;
          tip->to_wait.tv_sec = when->tv_sec - now.tv_sec;
          if (when->tv_nsec >= now.tv_nsec)
            tip->to_wait.tv_nsec = when->tv_nsec - now.tv_nsec;
          else
            {
              tip->to_wait.tv_sec--;
              tip->to_wait.tv_nsec = SECTONSEC - now.tv_nsec + when->tv_nsec;
            }

          err = makethread_detached (timeout_helper, (void *)tip);
          if (err == 0)
            return;
          else
            free (tip);
        }
    }

  (*func) (arg);
}

void
retry (void *arg)
{
  pthread_mutex_lock (&mutex);
  // retry
  printf ("in retry\n");
  pthread_mutex_unlock (&mutex);
}

int
main (int argc, char *argv[])
{
  int err, condition, arg;
  struct timespec when;

  if ((err = pthread_mutexattr_init (&attr)) != 0)
    err_exit (err, "pthread_mutexattr_init failed");
  if ((err = pthread_mutexattr_settype (&attr, PTHREAD_MUTEX_RECURSIVE)) != 0)
    err_exit (err, "can not set recursive type");
  if ((err = pthread_mutex_init (&mutex, &attr)) != 0)
    err_exit (err, "can not create recursive mutext");

  // processing...

  condition = 1;
  arg = 2;
  if (condition)
    {
      clock_gettime (CLOCK_REALTIME, &when);
      when.tv_sec += 2; // 10 seconds
      timeout (&when, retry, (void *)((unsigned long)arg));
    }

  pthread_mutex_unlock (&mutex);

  // processing...

  sleep (5);

  exit (0);
}