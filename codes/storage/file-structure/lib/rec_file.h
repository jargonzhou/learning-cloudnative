#ifndef LIB_REC_FILE
#define LIB_REC_FILE

#include "buf_file.h"
#include "log.h"
#include <strstream>

template <class RecType> // Pack(IOBuffer &), Unpack(IOBuffer &)
class RecordFile : public BufferFile
{
public:
  int Read (RecType &record, int recaddr = -1);
  int Write (const RecType &record, int recaddr = -1);
  int Append (const RecType &record);
  RecordFile (IOBuffer &buffer) : BufferFile (buffer) {}
};

template <class RecType>
int
RecordFile<RecType>::Read (RecType &record, int recaddr)
{
  DEBUG ("RecordFile Read record at {}", recaddr);
  int readAddr, result;
  readAddr = BufferFile::Read (recaddr);
  if (readAddr == -1)
    {
      ERROR ("RecordFile Read record at {} failed", recaddr);
      return -1;
    }

  result = record.Unpack (_buffer);
  if (!result)
    {
      ERROR ("RecordFile Read record at {} failed: unpack failed", recaddr);
      return -1;
    }

  return readAddr;
}

template <class RecType>
int
RecordFile<RecType>::Write (const RecType &record, int recaddr)
{
  int result;

  result = record.Pack (_buffer);
  DEBUG ("RecordFile Write record at {}: _buffer={}", recaddr, _buffer.str ());
  if (!result)
    {
      ERROR ("RecordFile Write record at {} failed: pack failed", recaddr);
      return -1;
    }

  return BufferFile::Write (recaddr);
}

template <class RecType>
int
RecordFile<RecType>::Append (const RecType &record)
{
  int result, pos;

  result = record.Pack (_buffer);

  if (!result)
    {
      ERROR ("RecordFile Append record failed: pack failed");
      return -1;
    }

  pos = BufferFile::Append ();
  DEBUG ("RecordFile Append record at {}", pos);
  return pos;
}

#endif /* LIB_REC_FILE */
