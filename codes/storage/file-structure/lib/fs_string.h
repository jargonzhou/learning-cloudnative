#ifndef LIB_FS_STRING
#define LIB_FS_STRING

#include <iostream>

using namespace std;

class FSString
{
public:
  FSString ();
  FSString (const FSString &); //!< copy constructor
  FSString (const char *);     //!< create from C string
  ~FSString ();
  FSString &operator= (const FSString &);
  operator char * ();
  int operator< (const FSString &) const;
  int operator> (const FSString &) const;
  int operator<= (const FSString &) const;
  int operator>= (const FSString &) const;
  int operator!= (const FSString &) const;
  int operator== (const FSString &) const;
  char *str () const; //!< return a copy of the string
protected:
  char *_string;
  unsigned int _maxLength;
  friend ostream &operator<< (ostream &stream, FSString &str);
};

ostream &operator<< (ostream &stream, FSString &str);
inline ostream &
operator<< (ostream &stream, FSString &str)
{
  stream << str._string;
  return stream;
}

inline int
FSString::operator> (const FSString &str) const
{
  return !(*this <= str);
}

inline int
FSString::operator>= (const FSString &str) const
{
  return !(*this < str);
}

inline int
FSString::operator!= (const FSString &str) const
{
  return !(*this == str);
}

#endif /* LIB_FS_STRING */
