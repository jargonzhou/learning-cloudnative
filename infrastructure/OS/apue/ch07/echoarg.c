#include "apue.h"

int
main (int argc, char *argv[])
{
  int i;
  for (i = 0; i < argc; i++) /* echo all command line arguments */
    printf ("argv[%d]: %s\n", i, argv[i]);
  return 0;
}
