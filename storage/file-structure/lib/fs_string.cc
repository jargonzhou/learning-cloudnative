#include "fs_string.h"
#include <string>

FSString::FSString ()
{
  _string = NULL;
  _maxLength = 0;
}

FSString::FSString (const FSString &str)
{
  _string = strdup (str._string);
  _maxLength = strlen (_string);
}

FSString::FSString (const char *str)
{
  _string = strdup (str);
  _maxLength = strlen (_string);
}

FSString::~FSString ()
{
  if (_string != NULL)
    delete _string;

  _maxLength = 0;
  _string = NULL;
}

FSString::operator char * () { return strdup (_string); }

FSString &
FSString::operator= (const FSString &str)
{
  if (strlen (str._string) >= _maxLength)
    {
      delete _string;
      _string = strdup (str._string);
      _maxLength = strlen (_string);
      return *this;
    }

  strcpy (_string, str._string);
  return *this;
}

int
FSString::operator< (const FSString &str) const
{
  return strcmp (_string, str._string) < 0;
}

int
FSString::operator<= (const FSString &str) const
{
  return strcmp (_string, str._string) <= 0;
}

int
FSString::operator== (const FSString &str) const
{
  return strcmp (_string, str._string) == 0;
}

char *
FSString::str () const
{
  return strdup (_string);
}