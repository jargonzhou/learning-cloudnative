#include "person_buf.h"

PersonBuf::PersonBuf () { Clear (); }

void
PersonBuf::Clear ()
{
  LastName[0] = 0;
  FirstName[0] = 0;
  Address[0] = 0;
  City[0] = 0;
  State[0] = 0;
  ZipCode[0] = 0;
}

int
PersonBuf::Pack (IOBuffer &Buffer) const
{
  // pack the fields into a FixedFieldBuffer
  // return TRUE if all succeed

  int numBytes;
  Buffer.Clear ();
  numBytes = Buffer.Pack (LastName);
  if (numBytes == -1)
    return false;
  numBytes = Buffer.Pack (FirstName);
  if (numBytes == -1)
    return false;
  numBytes = Buffer.Pack (Address);
  if (numBytes == -1)
    return false;
  numBytes = Buffer.Pack (City);
  if (numBytes == -1)
    return false;
  numBytes = Buffer.Pack (State);
  if (numBytes == -1)
    return false;
  numBytes = Buffer.Pack (ZipCode);
  if (numBytes == -1)
    return false;
  return true;
}

int
PersonBuf::Unpack (IOBuffer &Buffer)
{
  Clear ();
  int numBytes;
  numBytes = Buffer.Unpack (LastName);
  if (numBytes == -1)
    return false;
  LastName[numBytes] = 0;

  numBytes = Buffer.Unpack (FirstName);
  if (numBytes == -1)
    return false;
  FirstName[numBytes] = 0;

  numBytes = Buffer.Unpack (Address);
  if (numBytes == -1)
    return false;
  Address[numBytes] = 0;

  numBytes = Buffer.Unpack (City);
  if (numBytes == -1)
    return false;
  City[numBytes] = 0;

  numBytes = Buffer.Unpack (State);
  if (numBytes == -1)
    return false;
  State[numBytes] = 0;

  numBytes = Buffer.Unpack (ZipCode);
  if (numBytes == -1)
    return false;
  ZipCode[numBytes] = 0;

  return true;
}

int
PersonBuf::InitBuffer (FixedFieldBuffer &Buffer)
{
  int result;

  result = Buffer.AddField (10);
  result = result && Buffer.AddField (10);
  result = result && Buffer.AddField (15);
  result = result && Buffer.AddField (15);
  result = result && Buffer.AddField (2);
  result = result && Buffer.AddField (9);

  return result;
}

int
PersonBuf::InitBuffer (DelimFieldBuffer &Buffer)
{
  return true;
}

int
PersonBuf::InitBuffer (LengthFieldBuffer &Buffer)
{
  return true;
}

void
PersonBuf::Print (ostream &stream, const char *label) const
{
  if (label == 0)
    stream << "Person: ";
  else
    stream << label;
  stream << "\n\tLast Name=" << LastName << endl
         << "\tFirst Name=" << FirstName << endl
         << "\tAddress=" << Address << endl
         << "\tCity=" << City << endl
         << "\tState=" << State << endl
         << "\tZip Code=" << ZipCode << endl;
  stream.flush ();
}