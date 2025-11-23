#include "apue.h"

#define MAXSTRINGSZ 4096
static char envbuf[MAXSTRINGSZ];
extern char **environ;

// non-reentrant version
char *
getenv (const char *name)
{
  int i, len;

  len = strlen (name);
  for (i = 0; environ[i] != NULL; i++)
    {
      if ((strncmp (name, environ[i], len) == 0) && (environ[i][len] == '='))
        {
          strncpy (envbuf, &environ[i][len + 1], MAXSTRINGSZ - 1);
          return (envbuf);
        }
    }

  return (NULL);
}

int
main (int argc, char *argv[])
{
  char *v;

  v = getenv ("SHELL");
  printf ("v=%s\n", v);

  exit (0);
}