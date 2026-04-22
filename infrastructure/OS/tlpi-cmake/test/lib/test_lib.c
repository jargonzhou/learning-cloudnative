#include "../../src/lib/tlpi_hdr.h"
#include <stdio.h>

int
main (int argc, char **argv)
{

  int blockSize = getInt ("1234", GN_GT_0, "block-size");

  printf ("blockSize=%d\n", blockSize);
  return 0;
}
