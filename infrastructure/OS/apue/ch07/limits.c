#include "apue.h"
#include <sys/resource.h>

#define doit(name) pr_limits (#name, name);

static void pr_limits (char *name, int resoure);
int
main (int argc, char *argv[])
{
#ifdef RLIMIT_AS
  doit (RLIMIT_AS);
#endif
#ifdef RLIMIT_CORE
  doit (RLIMIT_CORE);
#endif
#ifdef RLIMIT_CPU
  doit (RLIMIT_CPU);
#endif
#ifdef RLIMIT_DATA
  doit (RLIMIT_DATA);
#endif
#ifdef RLIMIT_FSIZE
  doit (RLIMIT_FSIZE);
#endif
#ifdef RLIMIT_NOFILE
  doit (RLIMIT_NOFILE);
#endif
#ifdef RLIMIT_STACK
  doit (RLIMIT_STACK);
#endif
  return 0;
}

static void
pr_limits (char *name, int resoure)
{
  struct rlimit limit;
  unsigned long long lim;

  if (getrlimit (resoure, &limit) < 0)
    err_sys ("getrlimit error for %s", name);
  printf ("%-14s ", name);
  if (limit.rlim_cur == RLIM_INFINITY)
    printf ("(infinite) ");
  else
    {
      lim = limit.rlim_cur;
      printf ("%10lld ", lim);
    }

  if (limit.rlim_max == RLIM_INFINITY)
    printf ("(infinite) ");
  else
    {
      lim = limit.rlim_max;
      printf ("%10lld ", lim);
    }

  putchar ((int)'\n');
}
