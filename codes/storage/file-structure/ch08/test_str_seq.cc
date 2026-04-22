#include "str_seq.h"

int
main (int argc, char const *argv[])
{
  StringListProcess list (2);
  list.Match2Lists ("list1.fstxt", "list2.fstxt", "match.fstxt");
  list.Merge2Lists ("list1.fstxt", "list2.fstxt", "merge.fstxt");

  return 0;
}
