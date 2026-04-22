#include "apue.h"
#include <pthread.h>

#define NHASH 29
#define HASH(id) (((unsigned long)id) % NHASH)

struct foo *fh[NHASH];

pthread_mutex_t hashlock = PTHREAD_MUTEX_INITIALIZER;

struct foo
{
  int f_count; // reference count
  pthread_mutex_t f_lock;
  int f_id;
  struct foo *f_next; // protected by hashlock
};

struct foo *foo_alloc (int);
void foo_hold (struct foo *); // add reference
struct foo *foo_find (int);
void foo_release (struct foo *); // release reference

void
pr_fh ()
{
  int i;
  for (i = 0; i < NHASH; i++)
    {
      struct foo *fp = fh[i];
      if (fp == NULL)
        {
          // printf ("fh[%d] = NULL\n", i);
          continue;
        }

      printf ("fh[%d] =", i);
      for (; fp != NULL; fp = fp->f_next)
        {
          if (fp != NULL)
            {
              printf (" %d", fp->f_id);
            }
        }
      printf ("\n");
    }

  printf ("\n");
}

int
main (int argc, char *argv[])
{
  struct foo *fp1 = foo_alloc (42);
  struct foo *fp2 = foo_alloc (39);
  struct foo *fp3 = foo_alloc (76);
  struct foo *fp4 = foo_alloc (37);
  struct foo *fp5 = foo_alloc (59);
  struct foo *fp6 = foo_alloc (88);
  pr_fh ();

  foo_release (fp6);
  pr_fh ();

  foo_release (fp1);
  foo_release (fp2);
  foo_release (fp3);
  foo_release (fp4);
  foo_release (fp5);

  exit (0);
}

struct foo *
foo_alloc (int id)
{
  struct foo *fp;
  int idx;

  if ((fp = malloc (sizeof (struct foo))) != NULL)
    {
      fp->f_count = 1;
      fp->f_id = id;
      if (pthread_mutex_init (&fp->f_lock, NULL) != 0)
        {
          free (fp);
          return (NULL);
        }

      idx = HASH (id);
      // +hashlock then +fp->f_lock
      pthread_mutex_lock (&hashlock);
      fp->f_next = fh[idx];
      fh[idx] = fp;
      pthread_mutex_lock (&fp->f_lock);

      pthread_mutex_unlock (&hashlock);
      // other intialization
      pthread_mutex_unlock (&fp->f_lock);
    }

  return fp;
}
void
foo_hold (struct foo *fp)
{
  pthread_mutex_lock (&fp->f_lock);
  fp->f_count++;
  pthread_mutex_unlock (&fp->f_lock);
}

struct foo *
foo_find (int id)
{
  struct foo *fp;
  pthread_mutex_lock (&hashlock);
  for (fp = fh[HASH (id)]; fp != NULL; fp = fp->f_next)
    {
      if (fp->f_id == id)
        {
          foo_hold (fp);
          break;
        }
    }
  pthread_mutex_unlock (&hashlock);
  return (fp);
}

void
foo_release (struct foo *fp)
{
  struct foo *tfp;
  int idx;

  pthread_mutex_lock (&fp->f_lock);
  if (fp->f_count == 1) // lase reference
    {
      pthread_mutex_unlock (&fp->f_lock);
      pthread_mutex_lock (&hashlock);
      pthread_mutex_lock (&fp->f_lock);

      if (fp->f_count != 1)
        {
          fp->f_count--;
          pthread_mutex_unlock (&fp->f_lock);
          pthread_mutex_unlock (&hashlock);
          return;
        }

      // remove from list
      idx = HASH (fp->f_id);
      tfp = fh[idx];
      if (tfp == fp)
        fh[idx] = fp->f_next;
      else
        {
          while (tfp->f_next != fp)
            tfp = tfp->f_next;
          tfp->f_next = fp->f_next;
        }

      pthread_mutex_unlock (&hashlock);
      pthread_mutex_unlock (&fp->f_lock);
      pthread_mutex_destroy (&fp->f_lock);
      free (fp);
    }
  else
    {
      fp->f_count--;
      pthread_mutex_unlock (&fp->f_lock);
    }
}