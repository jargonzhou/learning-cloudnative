// c++ stream class

#include <fstream>
#include <iostream>

int
main (int argc, char const *argv[])
{
  using namespace std;

  char c;
  fstream f;

  char filename[20];
  cout << "Enter the name of the file: " << flush;
  cin >> filename;

  f.open (filename, ios::in);
  f.unsetf (ios::skipws);

  while (1)
    {
      f >> c;
      if (f.fail ())
        break;
      cout << c;
    }
  f.close ();

  return 0;
}
