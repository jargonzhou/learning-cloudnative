#ifndef CH04_PERSON_WR
#define CH04_PERSON_WR

#include "person.h"
#include <iostream>
#include <sstream>
#include <string>

using namespace std;

// read person from input
istream &
operator>> (istream &stream, Person &p)
{
  cout << "Enter last name: " << flush;
  stream.getline (p.LastName, 30);
  if (strlen (p.LastName) == 0)
    return stream;

  cout << "Enter first name: " << flush;
  stream.getline (p.FirstName, 30);

  cout << "Enter address: " << flush;
  stream.getline (p.Address, 30);

  cout << "Enter city: " << flush;
  stream.getline (p.City, 30);

  cout << "Enter state: " << flush;
  stream.getline (p.State, 15);

  cout << "Enter zip code: " << flush;
  stream.getline (p.ZipCode, 10);

  return stream;
}

// read person from file: fields seperated by delimiter '|'
istream &
read_person_lengthed_delim (istream &stream, Person &p)
{
  const char delim = '|';
  size_t length;
  stream.read ((char *)&length, sizeof (length));

  stream.getline (p.LastName, 30, delim);
  if (strlen (p.LastName) == 0)
    return stream;

  stream.getline (p.FirstName, 30, delim);
  stream.getline (p.Address, 30, delim);
  stream.getline (p.City, 30, delim);
  stream.getline (p.State, 15, delim);
  stream.getline (p.ZipCode, 10, delim);

  return stream;
}

// read person from file: variable sized record
istream &
read_person_lengthed (istream &stream, Person &p)
{
  size_t length;
  stream.read ((char *)&length, sizeof (length));
  char *buffer = new char[length + 1];
  stream.read (buffer, length);
  buffer[length] = 0;

  istringstream strbuf (buffer);
  strbuf >> p;

  delete[] buffer;

  return stream;
}

// write person
ostream &
operator<< (ostream &stream, Person &p)
{
  stream << "Last Name '" << p.LastName << "'\n"
         << "First Name '" << p.FirstName << "'\n"
         << "Address '" << p.Address << "'\n"
         << "City '" << p.City << "'\n"
         << "State '" << p.State << "'\n"
         << "Zip Code '" << p.ZipCode << "'\n"
         << flush;

  return stream;
}

// write person: fields seperated by delimiter '|'
const int MAX_BUF_SIZE = 1024;

ostream &
write_person_lengthed_delim (ostream &stream, Person &p)
{
  char *buf = new char[MAX_BUF_SIZE];

  const char *delim = "|";

  strcpy (buf, p.LastName);
  strcat (buf, delim);

  strcat (buf, p.FirstName);
  strcat (buf, delim);

  strcat (buf, p.Address);
  strcat (buf, delim);

  strcat (buf, p.City);
  strcat (buf, delim);

  strcat (buf, p.State);
  strcat (buf, delim);

  strcat (buf, p.ZipCode);
  strcat (buf, delim);

  size_t length = strlen (buf);
  stream.write ((const char *)&length, sizeof (length));
  stream.write (buf, length);

  delete[] buf;

  return stream;
}
#endif /* CH04_PERSON_WR */
