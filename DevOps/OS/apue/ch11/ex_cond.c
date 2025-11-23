#include "apue.h"
#include <pthread.h>

struct msg
{
  char *m_content;
  struct msg *m_next;
};

struct msg *workq;
pthread_cond_t qready = PTHREAD_COND_INITIALIZER;
pthread_mutex_t qlock = PTHREAD_MUTEX_INITIALIZER;

void
process_msg (void)
{
  struct msg *mp;

  for (;;)
    {
      pthread_mutex_lock (&qlock);
      while (workq == NULL)
        pthread_cond_wait (&qready, &qlock);
      mp = workq;
      workq = mp->m_next;
      pthread_mutex_unlock (&qlock);

      // process msg
      printf ("msg=%s\n", mp->m_content);
    }
}

void
enqueue_msg (struct msg *mp)
{
  pthread_mutex_lock (&qlock);

  mp->m_next = workq;
  workq = mp;

  pthread_mutex_unlock (&qlock);
  pthread_cond_signal (&qready);
}

void *
thr1_fn (void *arg)
{
  process_msg ();
  return ((void *)0);
}

void *
thr2_fn (void *arg)
{
  struct msg *mp = malloc (sizeof (struct msg));
  mp->m_content = "1";
  enqueue_msg (mp);

  sleep (2);

  struct msg *mp2 = malloc (sizeof (struct msg));
  mp2->m_content = "2";
  enqueue_msg (mp2);

  return ((void *)0);
}

int
main (int argc, char *argv[])
{
  int err;
  pthread_t pid1, pid2;
  void *tret;

  err = pthread_create (&pid1, NULL, thr1_fn, NULL);
  if (err != 0)
    err_exit (err, "can not create thread 1");

  err = pthread_create (&pid2, NULL, thr2_fn, NULL);
  if (err != 0)
    err_exit (err, "can not create thread 2");

  err = pthread_join (pid2, &tret);
  if (err != 0)
    err_exit (err, "can not join with thread 2");
  printf ("thread 2 exit code: %ld\n", (long)tret);

  err = pthread_join (pid1, &tret);
  if (err != 0)
    err_exit (err, "can not join with thread 1");
  printf ("thread 1 exit code: %ld\n", (long)tret);

  exit (0);
}
