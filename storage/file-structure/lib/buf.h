#ifndef LIB_BUF
#define LIB_BUF

#include <iostream>
#include <strstream>

using namespace std;

//! An abstract base class for file buffers
//! char array for buffer value
class IOBuffer
{
public:
  IOBuffer (int maxBytes = 1000);
  IOBuffer &operator= (const IOBuffer &);
  virtual void Clear (); //!< clear fields from buffer
  virtual int Pack (const void *field, int size = -1)
      = 0; //!< set the value of the next field of the buffer
  virtual int Unpack (void *field, int maxBytes = -1)
      = 0; //!< extract the value of the next field of the buffer
  virtual void Print (ostream &) const;
  int Init (int maxBytes);

  // sequential read and write operations: return the address of the record

  virtual int Read (istream &)
      = 0; //!< read a buffer from the stream, if success return read position
  virtual int
  Write (ostream &) const = 0; //!< write a buffer to the stream, if success
                               //!< return write position

  // direct read and write operations

  virtual int DRead (istream &, int recref);        //!< read specified record
  virtual int DWrite (ostream &, int recref) const; //!< write specified record

  // header operations: return the size of the header
  virtual int ReadHeader (istream &);
  virtual int WriteHeader (ostream &) const;

  char *
  str () const
  {
    ostrstream stream;
    this->Print (stream);
    return stream.str ();
  };

protected:
  bool _initialized; //!< TREE if buffer is initialized
  char *_buffer;     //!< character array to hold field values
  int _bufferSize;   //!< sum of the sizes of packed fields
  int _maxBytes;     //!< maximum number of characters in the buffer
  int _nextByte;     //!< index of next byte to be packed/unpacked
  bool _packing;     //!< TRUE if in packing mode, FALSE if unpacking
};

//! Abstract class designed to support variable length records
//! fields may be a variety of types
//! read and write operations for variable length records
class VariableLengthBuffer : public IOBuffer
{
public:
  VariableLengthBuffer (int maxBytes = 1000);
  VariableLengthBuffer (const VariableLengthBuffer &buffer) : IOBuffer (buffer)
  { // copy constructor
  }
  void Clear ();
  int Read (istream &);
  int Write (ostream &) const;
  int ReadHeader (istream &);
  int WriteHeader (ostream &) const;
  void Print (ostream &) const;

  int PackFixLen (void *, int);
  int PackDelimeted (void *, int);
  int PackLength (void *, int);
  int SizeOfBuffer () const; //!< return current size of buffer
  int Init ();
};

//! A buffer which holds delimited text fields
//! pack and unpack operations for delimited fields
class DelimFieldBuffer : public VariableLengthBuffer
{
public:
  DelimFieldBuffer (char delim = -1, int maxBytes = 1000);
  DelimFieldBuffer (const DelimFieldBuffer &buffer); //!< copy constructor
  void clear ();
  int Pack (const void *field, int size = -1);
  int Unpack (void *field, int maxBytes = -1);
  int ReadHeader (istream &stream);
  int WriteHeader (ostream &stream) const;
  void Print (ostream &) const;

  int Init (char delim = 0);
  static void SetDefaultDelim (char delim);

protected:
  char _delim; //!< delimiter character
  static char DefaultDelim;
};

inline DelimFieldBuffer::DelimFieldBuffer (const DelimFieldBuffer &buffer)
    : VariableLengthBuffer (buffer)
{
  Init (buffer._delim);
}

//! Class that supports length plus value fields
//! pack and unpack operation for length-based fields
class LengthFieldBuffer : public VariableLengthBuffer
{
public:
  LengthFieldBuffer (int maxBytes = 1000);
  LengthFieldBuffer (const LengthFieldBuffer &buffer)
      : VariableLengthBuffer (buffer)
  { // copy constructor
  }
  void Clear ();
  int Pack (const void *field, int size = -1);
  int Unpack (void *field, int maxBytes = -1);
  void Print (ostream &) const;

  int Init ();

protected:
};

//! Abstract class designed to support fixed length records
//! read and write operations for fixed length records
class FixedLengthBuffer : public IOBuffer
{
public:
  FixedLengthBuffer (int recordSize = 1000);
  FixedLengthBuffer (const FixedLengthBuffer &buffer); //!< copy constructor

  void Clear ();
  int Read (istream &);
  int Write (ostream &) const;
  int ReadHeader (istream &);
  int WriteHeader (ostream &) const;
  void Print (ostream &) const;

  int SizeOfBuffer () const; //!< return size of the buffer

protected:
  int Init (int recordSize);
  int ChangeRecordSize (int recordSize);
};

inline FixedLengthBuffer::FixedLengthBuffer (const FixedLengthBuffer &buffer)
    : IOBuffer (buffer)
{
  Init (buffer._bufferSize);
}

//! Abstract class designed to support fixed fields records
//! pack and unpack operations for fixed sized fields
class FixedFieldBuffer : public FixedLengthBuffer
{
public:
  FixedFieldBuffer (int maxFields, int recordSize = 1000);
  FixedFieldBuffer (int maxFields, int *fieldSize);
  FixedFieldBuffer (const FixedFieldBuffer &); //!< copy constructor
  FixedFieldBuffer &operator= (const FixedFieldBuffer &);

  void Clear ();
  int ReadHeader (istream &);
  int WriteHeader (ostream &) const;
  int Pack (const void *field, int size = -1);
  int Unpack (void *field, int maxBytes = -1);
  void Print (ostream &) const;
  int Init (int maxFields);

  int AddField (int fieldSize);
  int NumberOfFields () const; //!< return number of defined fields
  int Init (int numFields, int *fieldSize);

protected:
  int *_fieldSize; //!< array to hold field sizes
  int _maxFields;  //!< maximum number of fields
  int _numFields;  //!< actual number of defined fields
  int _nextField;  //!< index of next field to be packed/unpacked
};

inline FixedFieldBuffer::FixedFieldBuffer (const FixedFieldBuffer &buffer)
    : FixedLengthBuffer (buffer)
{
  Init (buffer._numFields, buffer._fieldSize);
}

#endif /* LIB_BUF */
