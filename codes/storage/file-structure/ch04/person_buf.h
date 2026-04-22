#ifndef CH04_PERSON_BUF
#define CH04_PERSON_BUF

#include "buf.h"

class PersonBuf
{
public:
  char LastName[11];
  char FirstName[11];
  char Address[16];
  char City[16];
  char State[3];
  char ZipCode[10];

  PersonBuf ();

  static int InitBuffer (DelimFieldBuffer &);
  static int InitBuffer (LengthFieldBuffer &);
  static int InitBuffer (FixedFieldBuffer &);

  void Clear ();
  int Unpack (IOBuffer &);
  int Pack (IOBuffer &) const;
  void Print (ostream &, const char *label = 0) const;
};

#endif /* CH04_PERSON_BUF */
