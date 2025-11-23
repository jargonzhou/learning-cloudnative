#ifndef LIB_IDX
#define LIB_IDX

#include "buf.h"
#include <iostream>

using namespace std;

template <class KeyType> class IndexBuffer; // forward declaration
#ifdef LIB_BTREE
template <class KeyType> class BTree; // forward declaration
#endif

template <class KeyType> // <, ==, =
class SimpleIndex
{
public:
  SimpleIndex (int maxKeys = 100, bool unique = true);
  ~SimpleIndex ();
  void Clear (); //!< remove all keys from index
  int Insert (const KeyType key, int recAddr);
  int Remove (const KeyType key, const int recAddr = -1);
  int Search (const KeyType key, const int recAddr = -1,
              const bool exact
              = true); //!< if exact, return -1 if not found, if !exact, find
                       //!< largest key in node < key
  void Print (ostream &stream) const;
  void PrintGraphviz (ostream &stream) const;

  int
  numKeys () const
  {
    return _numKeys;
  }

protected:
  int _maxKeys;
  int _numKeys;
  KeyType *_keys; //!< keys
  int *_recAddrs; //!< record addresses

  int Find (const KeyType key, const int recAddr = -1,
            const bool exact = true) const;
  int Init (const int maxKeys, const bool unique);
  bool _unique; //!< if true, each key value must be unique
  friend class IndexBuffer<KeyType>;
#ifdef LIB_BTREE
  friend class BTree<KeyType>;
#endif
};

template <class KeyType> //
class IndexBuffer : public FixedFieldBuffer
{
public:
  IndexBuffer (int maxKeys = 100, int keySize = sizeof (KeyType));
  int Pack (const SimpleIndex<KeyType> &index);
  int Unpack (SimpleIndex<KeyType> &index);
  void Print (ostream &stream) const;

protected:
  int _maxKeys;
  int _keySize;
};

#include "idx.tpp"

#endif /* LIB_IDX */
