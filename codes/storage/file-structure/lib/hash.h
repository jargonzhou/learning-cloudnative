#ifndef LIB_HASH
#define LIB_HASH

#include "buf_file.h"
#include "idx_text.h"
#include <iostream>

using std::ostream;

//! create the primary hash value from a string
int Hash (char *key);

//! get the primary hash, reverse the bits
//! return an address of depth bits
int MakeAddress (char *, int depth);

class Bucket;       // forward declaration
class BucketBuffer; // forward declaration

class Directory
{
public:
  Directory (int maxBucketKeys = -1);
  ~Directory ();
  int Open (const char *name);
  int Create (const char *name);
  int Close ();
  int Insert (char *key, int recAddr);
  int Remove (char *key);
  int Search (char *key); //!< return recAddr for key
  ostream &Print (ostream &stream);

protected:
  int _depth;       //!< depth of directory
  int _numCells;    //!< number of entries: 2^_depth
  int *_bucketAddr; //!< array of bucket address

  int DoubleSize (); //!< double the size of the directory
  int Collapse ();   //!< collapse, halve the size
  int InsertBucket (int bucketAddr, int first, int last);
  int RemoveBucket (int bucketIndex,
                    int depth);     //!< remove bucket from the directory
  int Find (char *key);             //!< return bucketAddr for key
  int StoreBucket (Bucket *bucket); //!< update or append bucket in file
  int LoadBucket (Bucket *bucket, int bucketAddr); //!< load bucket from file

  /// methods to support remove
  int _maxBucketKeys;
  BufferFile *_directoryFile;
  LengthFieldBuffer *_directoryBuffer;
  Bucket *_currentBucket;         //!< object to hold one bucket
  BucketBuffer *_theBucketBuffer; //!< buffer for buckets
  BufferFile *_bucketFile;
  int Pack () const;
  int Unpack ();
  Bucket *_printBucket; //!< object to hold one bucket

  friend class Bucket;
};

const int defaultMaxKeys = 100;

class Bucket : public TextIndex
{
protected:
  Bucket (Directory &dir, int maxKeys = defaultMaxKeys);
  int Insert (char *key, int recAddr);
  int Remove (char *key); //!< remove the key, return its recAddr
  Bucket *Split ();       //!< split the bucket and redistributed the keys
  int NewRange (int &newStart,
                int &newEnd); //!< calculate the range of a new (split) bucket
  int Redistribute (Bucket &newBucket); //!< redistribute keys
  int FindBuddy ();  //!< find the bucket that is the buddy of this
  int TryCombine (); //!< attempt to combine buckets
  int Combine (Bucket *buddy, int buddyIndex); //!< combine two buckets
  ostream &Print (ostream &stream);

  int _depth; //!< number of bits used 'in common' by the keys in this bucket
  Directory &_dir; //!< directory that contains the bucket
  int _bucketAddr; //!< address of file

  friend class Directory;
  friend class BucketBuffer;
};

class BucketBuffer : public TextIndexBuffer
{
public:
  BucketBuffer (int keySize, int maxKeys);
  int Pack (const Bucket &bucket);
  int Unpack (Bucket &bucket);
};

#endif /* LIB_HASH */
