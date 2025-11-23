#ifndef CH07_TEXT_IDX_FILE
#define CH07_TEXT_IDX_FILE

#include "buf.h"
#include "buf_file.h"
#include "idx_text.h"
#include "rec_file.h"
#include <iostream>
#include <string>
#include <strstream>

#include "log.h"

using namespace std;

//! template class to support indexed read and write of records
//! using RecordFile, BufferFile, TextIndex and TextIndexBuffer
template <class RecType> //
class TextIndexedFile
{
public:
  int Read (RecType &record);            //<! read next record
  int Read (char *key, RecType &record); //<! read by key
  int Append (const RecType &record);
  int Update (char *oldKey, const RecType &record);
  int Create (const char *fileName, int mode = ios::in | ios::out);
  int Open (const char *fileName, int mode = ios::in | ios::out);
  int Close ();
  TextIndexedFile (IOBuffer &dataBuffer, int keySize, int maxKeys = 100);
  ~TextIndexedFile (); //<! close and delete

protected:
  TextIndex _index;
  BufferFile _indexFile;
  TextIndexBuffer _indexBuffer;
  RecordFile<RecType> _dataFile;
  char *_fileName; //!< base file name
  int SetFileName (const char *fileName, char *&dataFileName,
                   char *&indexFileName);
};

template <class RecType>
int
TextIndexedFile<RecType>::Read (RecType &record)
{
  return _dataFile.Read (record, -1);
}

template <class RecType>
int
TextIndexedFile<RecType>::Read (char *key, RecType &record)
{
  int ref = _index.Search (key);
  if (ref < 0)
    return -1;

  return _dataFile.Read (record, ref);
}

template <class RecType>
int
TextIndexedFile<RecType>::Append (const RecType &record)
{
  char *key = record.Key ();
  int ref = _index.Search (key);
  if (ref != -1)
    return -1;

  ref = _dataFile.Append (record);
  return _index.Insert (key, ref);
}

template <class RecType>
int
TextIndexedFile<RecType>::Update (char *oldKey, const RecType &record)
{
  // TODO
  return -1;
}

template <class RecType>
int
TextIndexedFile<RecType>::SetFileName (const char *fileName,
                                       char *&dataFileName,
                                       char *&indexFileName)
{
  if (_fileName != NULL)
    return false;

  _fileName = strdup (fileName);
  ostrstream dataName, indexName;
  dataName << _fileName << ".fsdat" << ends;
  indexName << _fileName << ".fsidx" << ends;
  dataFileName = strdup (dataName.str ());
  indexFileName = strdup (indexName.str ());
  return true;
}

template <class RecType>
int
TextIndexedFile<RecType>::Create (const char *fileName, int mode)
{
  int result;
  char *dataFileName, *indexFileName;

  result = SetFileName (fileName, dataFileName, indexFileName);
  if (!result)
    {
      ERROR ("SetFileName {} failed", fileName);
      return false;
    }
  result = _dataFile.Create (dataFileName, mode);
  if (!result)
    {
      ERROR ("Create data file {} failed", dataFileName);
      _fileName = NULL;
      return false;
    }

  result = _indexFile.Create (indexFileName, ios::out);
  if (!result)
    {
      ERROR ("Create index file {} failed", indexFileName);
      _dataFile.Close ();
      _fileName = NULL;
      return false;
    }

  return true;
}

template <class RecType>
int
TextIndexedFile<RecType>::Open (const char *fileName, int mode)
{
  int result;
  char *dataFileName, *indexFileName;

  result = SetFileName (fileName, dataFileName, indexFileName);
  if (!result)
    {
      ERROR ("SetFileName {} failed", fileName);
      return false;
    }

  result = _dataFile.Open (dataFileName, mode);
  if (!result)
    {
      ERROR ("Open file {} with mode {} failed", dataFileName, mode);
      _fileName = NULL;
      return false;
    }

  result = _indexFile.Open (indexFileName, ios::out);
  if (!result)
    {
      ERROR ("Open index file {} failed", indexFileName);
      _dataFile.Close ();
      _fileName = NULL;
      return false;
    }

  // read index into memory
  result = _indexFile.Read ();
  if (result != -1)
    {
      result = _indexBuffer.Unpack (_index);
      if (result != -1)
        return true;
    }

  ERROR ("Open {} and {} failed", dataFileName, indexFileName);

  _dataFile.Close ();
  _indexFile.Close ();
  _fileName = NULL;
  return false;
}

template <class RecType>
int
TextIndexedFile<RecType>::Close ()
{
  int result;

  if (_fileName == NULL)
    return 0;

  _dataFile.Close ();
  _indexFile.Rewind ();
  _indexBuffer.Pack (_index);
  result = _indexFile.Write ();
  DEBUG ("result of index write: {}", result);
  _indexFile.Close ();
  _fileName = NULL;
  return 1;
}

template <class RecType>
TextIndexedFile<RecType>::TextIndexedFile (IOBuffer &dataBuffer, int keySize,
                                           int maxKeys)
    : _dataFile (dataBuffer), _index (maxKeys), _indexFile (_indexBuffer),
      _indexBuffer (keySize, maxKeys)
{
  _fileName = NULL;
}

template <class RecType> TextIndexedFile<RecType>::~TextIndexedFile ()
{
  Close ();
}

#endif /* CH07_TEXT_IDX_FILE */
