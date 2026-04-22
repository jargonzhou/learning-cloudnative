#include "hash.h"
#include "log.h"
#include <iostream>
#include <string>

using std::cout;
using std::endl;

template <typename T>
std::string
get_bits (T value)
{
  int size = sizeof (value) * CHAR_BIT;
  std::string ret;
  ret.reserve (size);
  for (int i = size - 1; i >= 0; --i)
    ret += (value & (1 << i)) == 0 ? '0' : '1';
  return ret;
}

int
main (int argc, char const *argv[])
{
  // init_log ();

  int result;
  Directory dir (4);
  result = dir.Create ("test_hash");
  if (result == 0)
    {
      cout << "Please delete test_hash.fsdir and test_hash.fsbkt" << endl;
      return -1;
    }

  char *keys[] = { // 100, 101, 102, 103, 104
                   (char *)"bill", (char *)"lee", (char *)"pauline",
                   (char *)"alan", (char *)"julie",
                   // 105, 106, 107, 108, 109
                   (char *)"mike", (char *)"elizabeth", (char *)"mark",
                   (char *)"ashley", (char *)"peter",
                   // 110, 111, 112, 113, 114
                   (char *)"joan", (char *)"john", (char *)"charles",
                   (char *)"mary", (char *)"emily"
  };
  const int numKeys = 15;
  for (int i = 0; i < numKeys; i++)
    {
      cout << "\nInsert: " << keys[i] << "\trecAddr=" << 100 + i << "\nhash=\n"
           << get_bits (Hash (keys[i])) << " = " << Hash (keys[i])
           << "\naddress=\n"
           << get_bits (MakeAddress (keys[i], 16)) << " = "
           << MakeAddress (keys[i], 16) << endl;

      result = dir.Insert (keys[i], 100 + i);

      if (result == 0)
        {
          cout << "insert for " << keys[i] << " failed" << endl;
        }

      dir.Print (cout);
    }

  return 0;
}
