#ifndef LIB_IDX_TEXT
#define LIB_IDX_TEXT

#include "buf.h"
#include <iostream>

using namespace std;

class TextIndexBuffer; // forward declaration

//! see idx.h for a general index
class TextIndex
{
public:
  TextIndex (int maxKeys = 100, bool unique = true);
  ~TextIndex ();
  int Insert (const char *key, int recaddr);
  int Remove (const char *key);
  int Search (const char *key) const;
  void Print (ostream &) const;

protected:
  int _maxKeys;
  int _numKeys;
  char **_keys;
  int *_recAddrs;
  int Find (const char *key) const;
  int Init (int maxKeys, bool unique);
  bool _unique;
  friend class TextIndexBuffer;
};

using namespace std;

class TextIndexBuffer : public FixedFieldBuffer
{
public:
  TextIndexBuffer (int keySize, int maxKeys = 100, int extraFields = 0,
                   int extraSize = 0);
  int Pack (const TextIndex &);
  int Unpack (TextIndex &);
  void Print (ostream &) const;

protected:
  int _maxKeys;
  int _keySize;
  char *_dummy;
};

#endif /* LIB_IDX_TEXT */
