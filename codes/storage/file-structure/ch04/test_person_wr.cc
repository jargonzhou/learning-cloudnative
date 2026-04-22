#include "person_wr.h"
#include <fstream>

using namespace std;

const char *fname = "test_person_wr.fstxt";

int
test_std ()
{
  Person p;
  ofstream stream (fname, ios::out | ios::app);
  if (stream.fail ())
    {
      cout << "File open failed!" << endl;
      return -1;
    }

  while (1)
    {
      cin >> p;
      if (strlen (p.LastName) == 0)
        break;
      stream << p;
      stream.flush ();
    }

  stream.close ();

  return 0;
}

int
test_delim ()
{
  Person p;
  // Mary|Ames|123 Maple|Stillwater|OK|74075
  // Alan|Mason|90 Eastgate|Ada|OK|74820
  strcpy (p.FirstName, "Mary");
  strcpy (p.LastName, "Ames");
  strcpy (p.Address, "123 Maple");
  strcpy (p.City, "Stillwater");
  strcpy (p.State, "OK");
  strcpy (p.ZipCode, "74075");

  ofstream os (fname, ios::out | ios::trunc);
  write_person_lengthed_delim (os, p);
  os.flush ();
  os.close ();

  Person np;
  ifstream is (fname, ios::in);
  read_person_lengthed_delim (is, np);
  cout << np;

  return 0;
}

int
main (int argc, char const *argv[])
{
  // return test_std ();

  return test_delim ();
}
