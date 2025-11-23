#include "buf.h"
#include "person_buf.h"
#include <fstream>
#include <iostream>
#include <string>

using namespace std;

PersonBuf MaryAmes;
PersonBuf AlanMason;

void
InitPerson ()
{
  cout << "Initializing persons" << endl;
  strcpy (MaryAmes.LastName, "Ames");
  strcpy (MaryAmes.FirstName, "Mary");
  strcpy (MaryAmes.Address, "123 Maple");
  strcpy (MaryAmes.City, "Stillwater");
  strcpy (MaryAmes.State, "OK");
  strcpy (MaryAmes.ZipCode, "74075");
  MaryAmes.Print (cout);

  strcpy (AlanMason.LastName, "Mason");
  strcpy (AlanMason.FirstName, "Alan");
  strcpy (AlanMason.Address, "90 Eastgate");
  strcpy (AlanMason.City, "Ada");
  strcpy (AlanMason.State, "OK");
  strcpy (AlanMason.ZipCode, "74820");
  AlanMason.Print (cout);
}

template <class IOB>
void
testBuffer (IOB &buffer, const char *myfile)
{
  PersonBuf person;
  int result;
  int recaddr1, recaddr2, recaddr3, recaddr4;

  // test writing
  ofstream testOut (myfile, ios::out);
  result = buffer.WriteHeader (testOut);
  cout << "write header " << result << endl;
  MaryAmes.Pack (buffer);
  recaddr1 = buffer.Write (testOut);
  cout << "write at " << recaddr1 << endl;
  AlanMason.Pack (buffer);
  recaddr2 = buffer.Write (testOut);
  cout << "write at " << recaddr2 << endl;
  testOut.close ();

  // test reading
  ifstream testIn (myfile, ios::in);
  result = buffer.ReadHeader (testIn);
  cout << "read header " << result << endl;
  buffer.DRead (testIn, recaddr2);
  person.Unpack (buffer);
  person.Print (cout, "First record:");

  buffer.DRead (testIn, recaddr1);
  person.Unpack (buffer);
  person.Print (cout, "Second record:");
}

void
testFixedField ()
{
  cout << "\nTesting FixedFieldBuffer" << endl;
  FixedFieldBuffer buff (6);
  PersonBuf::InitBuffer (buff);

  testBuffer (buff, "fixlen.fsdat");
}

void
testLength ()
{
  cout << "\nTesting LengthFieldBuffer" << endl;
  LengthFieldBuffer buff;
  PersonBuf::InitBuffer (buff);

  testBuffer (buff, "length.fsdat");
}

void
testDelim ()
{
  cout << "\nTesting DelimFieldBuffer" << endl;
  DelimFieldBuffer::SetDefaultDelim ('|');
  DelimFieldBuffer buff;
  PersonBuf::InitBuffer (buff);

  testBuffer (buff, "delim.fsdat");
}

int
main (int argc, char const *argv[])
{
  InitPerson ();
  testFixedField ();
  testLength ();
  testDelim ();

  return 0;
}
