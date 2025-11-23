#include "apue.h"
#include <pthread.h>

void
printids (const char *s)
{
  pid_t pid;
  pthread_t tid;

  pid = getpid ();       // current process id
  tid = pthread_self (); // current thread id
  printf ("%s pid %lu tid %lu (0x%lx)\n", s, (unsigned long)pid,
          (unsigned long)tid, (unsigned long)tid);
}

void *
thr_fn (void *arg)
{
  printids ("new thread: ");
  return ((void *)0);
}

int
main (int argc, char *argv[])
{
  pthread_t ntid;
  int err;

  err = pthread_create (&ntid, NULL, thr_fn, NULL);
  if (err != 0)
    err_exit (err, "can not create thread");
  printids ("main thread: ");
  sleep (1);
  exit (0);
}
