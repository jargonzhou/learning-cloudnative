#include "fs_string.h"
#include "log.h"

int
main (int argc, char const *argv[])
{
  init_log ();
  DEBUG ("testing FSString");

  FSString str = "aaa";
  FSString *arr = new FSString[2];

  arr[0] = "";
  arr[1] = "bbb";

  return 0;
}
