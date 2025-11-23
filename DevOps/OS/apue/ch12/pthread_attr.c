#include "apue.h"
#include <pthread.h>

int
_makethread (void *(*fn) (void *), void *arg)
{
  int err;
  pthread_t tid;
  pthread_attr_t attr;

  err = pthread_attr_init (&attr);
  if (err != 0)
    return (err);
  err = pthread_attr_setdetachstate (&attr, PTHREAD_CREATE_DETACHED);
  if (err == 0)
    err = pthread_create (&tid, &attr, fn, arg);
  pthread_attr_destroy (&attr);
  return (err);
}

void *
thr_fn (void *arg)
{
  pthread_t tid = pthread_self ();
  printf ("tid %lu start\n", (unsigned long)tid);

  int err;
  pthread_attr_t attr;
  size_t stacksize;

  err = pthread_attr_init (&attr);
  if (err != 0)
    {
      printf ("pthread_attr_init error");
      pthread_exit ((void *)1);
    }
  err = pthread_attr_getstacksize (&attr, &stacksize);
  if (err != 0)
    {
      printf ("pthread_attr_getstack error");
      pthread_exit ((void *)1);
    }
  printf ("tid %lu statcksize=%lu\n", (unsigned long)tid,
          (unsigned long)stacksize);

  printf ("tid %lu end\n", (unsigned long)tid);
  return ((void *)0);
}

int
main (int argc, char *argv[])
{
  int err;
  long stacksizes;
  size_t statcksize;
  pthread_attr_t attr;

  // _POSIX_THREAD_ATTR_STACKSIZE, unistd.h: -1 or 200809L
  stacksizes = (size_t)sysconf (_SC_THREAD_ATTR_STACKSIZE);
  if (stacksizes != 0)
    printf ("support _POSIX_THREAD_ATTR_STACKSIZE\n");

  err = pthread_attr_init (&attr);
  if (err != 0)
    err_sys ("pthread_attr_init error");
  err = pthread_attr_getstacksize (&attr, &statcksize);
  if (err != 0)
    err_sys ("pthread_attr_getstack error");
  printf ("attr statcksize=%zu\n", statcksize);

  err = _makethread (thr_fn, (void *)0);
  if (err != 0)
    err_sys ("makethread error");

  sleep (2);

  exit (0);
}