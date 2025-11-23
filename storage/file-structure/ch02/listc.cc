// c stream
#include <stdio.h>

int
main (int argc, char const *argv[])
{
  char c;
  FILE *f;
  char filename[20];

  printf ("Enter the name of file: ");
  gets (filename);
  f = fopen (filename, "r");
  while (fread (&c, 1, 1, f) != 0)
    fwrite (&c, 1, 1, stdout);
  fclose (f);

  return 0;
}
