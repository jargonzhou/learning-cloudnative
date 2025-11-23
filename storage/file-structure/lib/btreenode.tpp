#include "log.h"
#include <cstdlib>
/// BTreeNode

template <class KeyType>
KeyType *BTreeNode<KeyType>::NullKey
    = (KeyType *)std::calloc (1, sizeof (KeyType));

template <class KeyType>
BTreeNode<KeyType>::BTreeNode (int maxKeys, bool unique)
    : SimpleIndex<KeyType> (maxKeys + 1, // for overflow
                            unique)
{
  Init ();
}

template <class KeyType> BTreeNode<KeyType>::~BTreeNode () {}

template <class KeyType>
int
BTreeNode<KeyType>::Insert (const KeyType key, int recAddr)
{
  DEBUG ("Insert key={}, recAddr={}", key, recAddr);
  int result;
  result = SimpleIndex<KeyType>::Insert (key, recAddr);
  if (!result)
    {
      ERROR ("Insert key={}, recAddr={} failed", key, recAddr);
      return 0;
    }

  if (this->_numKeys > _maxBKeys)
    {
      DEBUG ("Insert key={}, recAddr={} overflow", key, recAddr);
      return -1;
    }

  return 1;
}

template <class KeyType>
int
BTreeNode<KeyType>::Remove (const KeyType key, int recAddr)
{
  int result;
  result = SimpleIndex<KeyType>::Remove (key, recAddr);
  if (!result)
    return 0; // remove failed
  if (this->_numKeys < _minBKeys)
    {
      return -1; // node underflow
    }
  return 1;
}

template <class KeyType>
void
BTreeNode<KeyType>::Print (ostream &) const
{
  SimpleIndex<KeyType>::Print (cout);
}

template <class KeyType>
void
BTreeNode<KeyType>::PrintGraphviz (ostream &) const
{
  SimpleIndex<KeyType>::PrintGraphviz (cout);
}

template <class KeyType>
KeyType
BTreeNode<KeyType>::LargestKey ()
{
  if (this->_numKeys > 0)
    return this->_keys[this->_numKeys - 1];
  else
    return this->_keys[0];
}

template <class KeyType>
int
BTreeNode<KeyType>::Split (BTreeNode<KeyType> *newNode)
{
  if (this->_numKeys < _maxBKeys)
    {
      return 0;
    }

  // middle point
  int mid = (this->_numKeys + 1) / 2;
  int numNewKeys = this->_numKeys - mid;
  if (numNewKeys > newNode->_maxBKeys || numNewKeys < newNode->_minBKeys)
    {
      return 0;
    }

  for (int i = mid; i < this->_numKeys; i++)
    {
      newNode->_keys[i - mid] = this->_keys[i];
      newNode->_recAddrs[i - mid] = this->_recAddrs[i];
    }

  newNode->_numKeys = numNewKeys;
  this->_numKeys = mid;
  // NOTE: not clean this key and recaddr
  return 1;
}

template <class KeyType>
int
BTreeNode<KeyType>::Merge (BTreeNode<KeyType> *fromNode)
{
  if (this->_numKeys + fromNode->_numKeys > _maxBKeys - 1)
    {
      return 0;
    }

  for (int i = 0; i < fromNode->_numKeys; i++)
    {
      this->_keys[this->_numKeys + i] = fromNode->_keys[i];
      this->_recAddrs[this->_numKeys + i] = fromNode->_recAddrs[i];
    }

  this->_numKeys += fromNode->_numKeys;
  // adjust links
  _nextNode = fromNode->_nextNode;
  return 1;
}

template <class KeyType>
int
BTreeNode<KeyType>::UpdateKey (KeyType oldKey, KeyType newKey, int recAddr)
{
  int recaddr = this->Search (oldKey, recAddr);
  if (recaddr < 0)
    {
      return 0; // not found
    }

  Remove (oldKey, recAddr);
  Insert (newKey, recaddr);
  return 1;
}

template <class KeyType>
int
BTreeNode<KeyType>::Pack (IOBuffer &buffer) const
{
  int result;
  buffer.Clear ();

  int d_numKeys = this->_numKeys;
  result = buffer.Pack (&d_numKeys);
  if (result == -1)
    {
      ERROR ("Pack _numKeys failed");
      return result;
    }
  DEBUG ("Pack: _numKeys={}", d_numKeys);

  KeyType d_key;
  int d_recAddr;
  for (int i = 0; i < _maxBKeys; i++)
    {
      if (i < this->_numKeys)
        {
          d_key = this->_keys[i];
          d_recAddr = this->_recAddrs[i];
        }
      else
        { // padding
          d_key = *NullKey;
          d_recAddr = -1;
        }

      DEBUG ("Pack _keys[{}]={}, _recAddrs[{}]={}", i, d_key, i, d_recAddr);
      result = buffer.Pack (&d_key);
      if (result == -1)
        {
          ERROR ("Pack _keys[{}] failed", i);
          return result;
        }

      result = buffer.Pack (&d_recAddr);
      if (result == -1)
        {
          ERROR ("Pack _recAddrs[{}] failed", i);
          return result;
        }
    }
  DEBUG ("Packe buffer={}", buffer.str ());
  return result;
}

template <class KeyType>
int
BTreeNode<KeyType>::Unpack (IOBuffer &buffer)
{
  int result;

  result = buffer.Unpack (&(this->_numKeys));
  if (result == -1)
    {
      ERROR ("Unpack _numKeys failed");
      return result;
    }
  DEBUG ("Unpack: _numKeys={}", this->_numKeys);

  // for (int i = 0; i < this->_numKeys; i++)
  for (int i = 0; i < _maxBKeys; i++)
    {
      result = buffer.Unpack (&(this->_keys[i]));
      if (result == -1)
        {
          ERROR ("Unpack _keys[{}] failed", i);
          return result;
        }

      result = buffer.Unpack (&(this->_recAddrs[i]));
      if (result == -1)
        {
          ERROR ("Unpack _recAddrs[{}] failed", i);
          return result;
        }

      DEBUG ("Unpack: _keys[{}]={}, _recAddrs[{}]={}", i, this->_keys[i], i,
             this->_recAddrs[i]);
    }
  return result;
}

template <class KeyType>
int
BTreeNode<KeyType>::InitBuffer (FixedFieldBuffer &buffer, int maxKeys,
                                int keySize)
{
  buffer.AddField (sizeof (int));
  for (int i = 0; i < maxKeys; i++)
    {
      buffer.AddField (keySize);
      buffer.AddField (sizeof (int));
    }
  return 1;
}

template <class KeyType>
int
BTreeNode<KeyType>::Init ()
{
  _nextNode = -1;
  _recAddr = -1;
  _maxBKeys = this->_maxKeys - 1;
  _minBKeys = _maxBKeys / 2;
  return 1;
}