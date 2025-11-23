#include "hash.h"
#include "idx_text.h"
#include "log.h"
#include <string>
#include <strstream>

using std::endl;
using std::ends;

int
Hash (char *key)
{
  int result = 0;
  int len = strlen (key);
  if (len % 2 == 1)
    len++;

  for (int i = 0; i < len; i += 2)
    {
      result = (result + 100 * key[i] + key[i + 1]) % 19937;
    }
  return result;
}

int
MakeAddress (char *key, int depth)
{
  int result = 0;
  int hashVal = Hash (key);
  for (int i = 0; i < depth; i++)
    {
      result = result << 1;
      int lowbit = hashVal & 1;
      result = result | lowbit;
      hashVal = hashVal >> 1;
    }
  return result;
}

/// Directory

const int MaxKeySize = 12;

Directory::Directory (int maxBucketKeys)
{
  _depth = 0;
  _numCells = 1;
  _bucketAddr = new int[_numCells];
  _maxBucketKeys = maxBucketKeys;
  _directoryBuffer = new LengthFieldBuffer;
  _directoryFile = new BufferFile (*_directoryBuffer);
  _currentBucket = new Bucket (*this, _maxBucketKeys);
  _theBucketBuffer = new BucketBuffer (MaxKeySize, _maxBucketKeys);
  _bucketFile = new BufferFile (*_theBucketBuffer);
  _printBucket = new Bucket (*this, _maxBucketKeys);
}

Directory::~Directory () { Close (); }

void
makeNames (const char *name, char *&dirName, char *&bktName)
{
  ostrstream directoryName;
  directoryName << name << ".fsdir" << ends;
  dirName = strdup (directoryName.str ());

  ostrstream bucketName;
  bucketName << name << ".fsbkt" << ends;
  bktName = strdup (bucketName.str ());
}

int
Directory::Open (const char *name)
{
  int result;
  char *directoryName, *bucketName;
  makeNames (name, directoryName, bucketName);
  result = _directoryFile->Open (
      directoryName, ios::in | ios::out | ios::binary | ios::trunc);
  if (!result)
    return 0;

  result = _directoryFile->Read ();
  if (result == -1)
    return 0;

  result = Unpack ();
  if (result == -1)
    return 0;

  result = _bucketFile->Open (bucketName,
                              ios::in | ios::out | ios::binary | ios::trunc);
  return result;
}

int
Directory::Create (const char *name)
{
  int result;
  char *directoryName, *bucketName;
  makeNames (name, directoryName, bucketName);
  result = _directoryFile->Create (
      directoryName, ios::in | ios::out | ios::binary | ios::trunc);
  if (!result)
    return 0;

  result = _bucketFile->Create (bucketName,
                                ios::in | ios::out | ios::binary | ios::trunc);
  if (!result)
    return 0;

  _bucketAddr[0] = StoreBucket (_currentBucket);
  return result;
}

int
Directory::Close ()
{
  int result;
  result = Pack ();
  if (result == -1)
    return 0;

  _directoryFile->Rewind ();
  result = _directoryFile->Write ();
  if (result == -1)
    return 0;
  return _directoryFile->Close () && _bucketFile->Close ();
}

int
Directory::Insert (char *key, int recAddr)
{
  DEBUG ("Directory::Insert key={}, recAddr={}", key, recAddr);
  int found = Search (key);
  if (found == -1)
    return _currentBucket->Insert (key, recAddr);
  return 0;
}

int
Directory::Remove (char *key)
{
  int bucketAddr = Find (key);
  LoadBucket (_currentBucket, bucketAddr);
  return _currentBucket->Remove (key);
}

int
Directory::Search (char *key)
{
  int bucketAddr = Find (key);
  LoadBucket (_currentBucket, bucketAddr);
  return _currentBucket->Search (key);
}

ostream &
Directory::Print (ostream &stream)
{
  stream << "Directory _depth=" << _depth << ", _numCells=" << _numCells
         << endl;
  for (int i = 0; i < _numCells; i++)
    {
      stream << "bucket [" << i << "] addr=" << _bucketAddr[i] << " ";
      LoadBucket (_printBucket, _bucketAddr[i]);
      _printBucket->Print (stream);
    }
  stream << endl;
  return stream;
}

int
Directory::DoubleSize ()
{
  int newSize = 2 * _numCells;
  int *newBucketAddr = new int[newSize];
  for (int i = 0; i < _numCells; i++)
    {
      newBucketAddr[2 * i] = _bucketAddr[i];
      newBucketAddr[2 * i + 1] = _bucketAddr[i];
    }
  delete _bucketAddr;
  _bucketAddr = newBucketAddr;
  _depth++;
  _numCells = newSize;
  return 1;
}

int
Directory::Collapse ()
{
  if (_depth == 0)
    return 0; // only one bucket

  for (int i = 0; i < _numCells; i += 2)
    {
      if (_bucketAddr[i] != _bucketAddr[i + 1])
        return 0;
    }
  int newSize = _numCells / 2;
  int *newAddrs = new int[newSize];
  for (int j = 0; j < newSize; j++)
    newAddrs[j] = _bucketAddr[j * 2];
  delete _bucketAddr;
  _bucketAddr = newAddrs;
  _depth--;
  _numCells = newSize;
  return 1;
}

int
Directory::InsertBucket (int bucketAddr, int first, int last)
{
  for (int i = first; i <= last; i++)
    _bucketAddr[i] = bucketAddr;
  return 1;
}

int
Directory::RemoveBucket (int bucketIndex, int depth)
{
  int fillBits = _depth - depth;
  int buddyIndex = bucketIndex ^ (1 << (fillBits - 1));
  int newBucketAddr = _bucketAddr[buddyIndex];
  int lowIndex = bucketIndex >> fillBits << fillBits; // zero low bits
  int highIndex = lowIndex + (1 << fillBits) - 1;
  for (int i = lowIndex; i <= highIndex; i++)
    _bucketAddr[i] = newBucketAddr;
  return 0;
}

int
Directory::Find (char *key)
{
  return _bucketAddr[MakeAddress (key, _depth)];
}

int
Directory::StoreBucket (Bucket *bucket)
{
  int result;
  result = _theBucketBuffer->Pack (*bucket);
  if (result == -1)
    return -1;
  int addr = bucket->_bucketAddr;
  if (addr != 0)
    return _bucketFile->Write (addr);
  addr = _bucketFile->Append ();
  bucket->_bucketAddr = addr;
  return addr;
}

int
Directory::LoadBucket (Bucket *bucket, int bucketAddr)
{
  int result;
  result = _bucketFile->Read (bucketAddr);
  if (result == -1)
    return 0;
  result = _theBucketBuffer->Unpack (*bucket);
  if (result == -1)
    return 0;
  bucket->_bucketAddr = bucketAddr;
  return 1;
}

int
Directory::Pack () const
{
  int result, packSize;
  _directoryBuffer->Clear ();
  packSize = _directoryBuffer->Pack (&_depth, sizeof (int));
  if (packSize == -1)
    return -1;
  for (int i = 0; i < _numCells; i++)
    {
      result = _directoryBuffer->Pack (&_bucketAddr[i], sizeof (int));
      if (result == -1)
        return -1;
      packSize += result;
    }
  return packSize;
}

int
Directory::Unpack ()
{
  int result;
  result = _directoryBuffer->Unpack (&_depth, sizeof (int));
  if (result == -1)
    return -1;

  _numCells = 1 << _depth;
  if (_bucketAddr != NULL)
    delete _bucketAddr;
  _bucketAddr = new int[_numCells];
  for (int i = 0; i < _numCells; i++)
    {
      result = _directoryBuffer->Unpack (&_bucketAddr[i], sizeof (int));
      if (result == -1)
        return -1;
    }
  return 0;
}

/// Bucket
Bucket::Bucket (Directory &dir, int maxKeys) : TextIndex (maxKeys), _dir (dir)
{
  _bucketAddr = 0;
  _depth = 0;
}

int
Bucket::Insert (char *key, int recAddr)
{
  if (_numKeys < _maxKeys)
    {
      int result = TextIndex::Insert (key, recAddr);
      _dir.StoreBucket (this);
      return result;
    }
  else
    {
      Split ();
      return _dir.Insert (key, recAddr);
    }
}

int
Bucket::Remove (char *key)
{
  int result = TextIndex::Remove (key);
  if (!result)
    return 0; // key not in bucket

  TryCombine (); // attemp to combine with buddy

  // make the changes permanent
  _dir.StoreBucket (this);
  return 1;
}

Bucket *
Bucket::Split ()
{
  int newStart, newEnd;
  if (_depth == _dir._depth)
    _dir.DoubleSize (); // no room to split the bucket

  Bucket *newBucket = new Bucket (_dir, _maxKeys);
  _dir.StoreBucket (newBucket);
  NewRange (newStart, newEnd);
  _dir.InsertBucket (newBucket->_bucketAddr, newStart, newEnd);
  _depth++;
  newBucket->_depth = _depth;

  Redistribute (*newBucket);

  _dir.StoreBucket (this);
  _dir.StoreBucket (newBucket);
  return newBucket;
}

int
Bucket::NewRange (int &newStart, int &newEnd)
{
  int sharedAddr = MakeAddress (_keys[0], _depth);
  int bitsToFill = _dir._depth - (_depth + 1);
  newStart = (sharedAddr << 1) | 1;
  newEnd = newStart;
  for (int i = 0; i < bitsToFill; i++)
    {
      newStart = newStart << 1;
      newEnd = (newEnd << 1) | 1;
    }
  return 1;
}

int
Bucket::Redistribute (Bucket &newBucket)
{
  for (int i = _numKeys - 1; i >= 0; i--)
    {
      int bucketAddr = _dir.Find (_keys[i]);
      if (bucketAddr != _bucketAddr)
        {
          newBucket.TextIndex::Insert (_keys[i], _recAddrs[i]);
          TextIndex::Remove (_keys[i]); // delete key from this bucket
        }
    }
  return 1;
}

int
Bucket::FindBuddy ()
{
  if (_dir._depth == 0)
    return -1; // no buddy, empty directory

  if (_depth < _dir._depth)
    return -1;

  int sharedAddr = MakeAddress (_keys[0], _depth);
  return sharedAddr ^ 1;
}

int
Bucket::TryCombine ()
{
  int result;
  int buddyIndex = FindBuddy ();
  if (buddyIndex == -1)
    return 0;

  int buddyAddr = _dir._bucketAddr[buddyIndex];
  Bucket *buddyBucket = new Bucket (_dir, _maxKeys);
  _dir.LoadBucket (buddyBucket, buddyAddr);

  if (_numKeys + buddyBucket->_numKeys > _maxKeys)
    return 0;

  Combine (buddyBucket, buddyIndex); // collapse the 2 buckets

  result = _dir.Collapse ();
  if (result)
    TryCombine ();

  return 1;
}

int
Bucket::Combine (Bucket *buddy, int buddyIndex)
{
  // collapse this and buddy into a single bucket
  int result;
  for (int i = 0; i < buddy->_numKeys; i++)
    {
      result = Insert (buddy->_keys[i], buddy->_recAddrs[i]);
      if (!result)
        return 0;
    }
  _depth--;
  _dir.RemoveBucket (buddyIndex, _depth);
  return 1;
}

ostream &
Bucket::Print (ostream &stream)
{
  stream << "Bucket depth=" << _depth << " ";
  TextIndex::Print (stream);
  return stream;
}

/// BucketBuffer

BucketBuffer::BucketBuffer (int keySize, int maxKeys)
    : TextIndexBuffer (keySize, maxKeys, 1, sizeof (int))
{
  AddField (sizeof (int));
}

int
BucketBuffer::Pack (const Bucket &bucket)
{
  int result;
  TextIndexBuffer::Pack (bucket);
  if (result == -1)
    return -1;
  return FixedFieldBuffer::Pack (&bucket._depth);
}

int
BucketBuffer::Unpack (Bucket &bucket)
{
  int result;
  result = TextIndexBuffer::Unpack (bucket);
  if (result == -1)
    return -1;
  return FixedFieldBuffer::Unpack (&bucket._depth);
}