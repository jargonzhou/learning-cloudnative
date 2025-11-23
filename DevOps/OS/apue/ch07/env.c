#include "apue.h"

int
main (int argc, char *argv[])
{
  char *shell = getenv ("SHELL");
  int res;
  char *name1;

  printf ("shell: %s\n", shell);

  res = putenv ("NAME1=VALUE1");
  if (res != 0)
    err_sys ("putenv failed");

  res = setenv ("NAME1", "VALUE2", 1);
  if (res != 0)
    err_sys ("setenv failed");

  name1 = getenv ("NAME1");
  printf ("NAME1: %s\n", name1);

  res = unsetenv ("NAME1");
  if (res != 0)
    err_sys ("unsetenv failed");
  name1 = getenv ("NAME1");
  printf ("NAME1: %s\n", name1);

  return 0;
}
