#include "recording.h"
#include <string>
#include <strstream>

using namespace std;

Recording::Recording ()
{
  IdNum[0] = 0;
  Title[0] = 0;
  Composer[0] = 0;
  Artist[0] = 0;
  Label[0] = 0;
}

Recording::Recording (const char *label, const char *idNum, const char *title,
                      const char *composer, const char *artist)
{
  strcpy (Label, label);
  strcpy (IdNum, idNum);
  strcpy (Title, title);
  strcpy (Composer, composer);
  strcpy (Artist, artist);
}

char *
Recording::Key () const
{
  ostrstream key;
  key << Label << IdNum << ends;
  return key.str ();
}

int
Recording::Pack (IOBuffer &buffer) const
{
  int numBytes;
  buffer.Clear ();
  numBytes = buffer.Pack (IdNum);
  if (numBytes == -1)
    return false;

  numBytes = buffer.Pack (Title);
  if (numBytes == -1)
    return false;

  numBytes = buffer.Pack (Composer);
  if (numBytes == -1)
    return false;

  numBytes = buffer.Pack (Artist);
  if (numBytes == -1)
    return false;

  numBytes = buffer.Pack (Label);
  if (numBytes == -1)
    return false;

  return true;
}

int
Recording::Unpack (IOBuffer &buffer)
{
  int numBytes;
  numBytes = buffer.Unpack (IdNum, 6);
  if (numBytes == -1)
    return false;
  IdNum[numBytes] = 0;

  numBytes = buffer.Unpack (Title, 29);
  if (numBytes == -1)
    return false;
  Title[numBytes] = 0;

  numBytes = buffer.Unpack (Composer, 29);
  if (numBytes == -1)
    return false;
  Composer[numBytes] = 0;

  numBytes = buffer.Unpack (Artist, 29);
  if (numBytes == -1)
    return false;
  Artist[numBytes] = 0;

  numBytes = buffer.Unpack (Label, 29);
  if (numBytes == -1)
    return false;
  Label[numBytes] = 0;

  return true;
}

void
Recording::Print (ostream &stream, char *label) const
{
  stream << Label << "|" << IdNum << "|" << Title << "|" << Composer << "|"
         << Artist << endl;
}