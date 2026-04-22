#include "apue.h"
#include <pthread.h>

struct foo
{
  int a, b, c, d;
};

void
printfoo (const char *s, const struct foo *fp)
{
  printf ("%s", s);
  printf ("\tstruture at 0x%lx\n", (unsigned long)fp);
  printf ("\tfoo.a=%d\n", fp->a);
  printf ("\tfoo.b=%d\n", fp->b);
  printf ("\tfoo.c=%d\n", fp->c);
  printf ("\tfoo.d=%d\n", fp->d);
}

void *
thr_fn1 (void *arg)
{
  // not on stack
  struct foo *fp = (struct foo *)malloc (sizeof (struct foo));
  fp->a = 1;
  fp->b = 2;
  fp->c = 3;
  fp->d = 4;

  printfoo ("thread 1:\n", fp);
  pthread_exit ((void *)fp);
}

void *
thr_fn2 (void *arg)
{
  printf ("thread2: ID is %lu\n", (unsigned long)pthread_self ());
  pthread_exit ((void *)0);
}

int
main (int argc, char *argv[])
{
  int err;
  pthread_t tid1, tid2;
  struct foo *fp;

  err = pthread_create (&tid1, NULL, thr_fn1, NULL);
  if (err != 0)
    err_exit (err, "can not create thread 1");
  err = pthread_join (tid1, (void *)&fp);
  if (err != 0)
    err_exit (err, "can not join with thread 1");

  sleep (1);

  printf ("parent starting second thread\n");
  err = pthread_create (&tid2, NULL, thr_fn2, NULL);
  if (err != 0)
    err_exit (err, "can not create thread 2");

  sleep (1);

  printfoo ("parent:\n", fp);

  exit (0);
}
