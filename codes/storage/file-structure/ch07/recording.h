#ifndef CH07_RECORDING
#define CH07_RECORDING

#include "buf.h"
#include <iostream>

using namespace std;

//! A recording with a composite key
class Recording
{
public:
  Recording ();
  Recording (const char *label, const char *idNum, const char *title,
             const char *composer, const char *artist);
  char *Key () const; //!< return key of object
  int Unpack (IOBuffer &);
  int Pack (IOBuffer &) const;
  void Print (ostream &, char *label = 0) const;

  char IdNum[7];
  char Title[30];
  char Composer[30];
  char Artist[30];
  char Label[7];
};

ostream &operator<< (ostream &, Recording &);

inline ostream &
operator<< (ostream &stream, Recording &rec)
{
  rec.Print (stream);
  return stream;
}
#endif /* CH07_RECORDING */
