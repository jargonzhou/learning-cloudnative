/// SimpleIndex

template <class KeyType>
SimpleIndex<KeyType>::SimpleIndex (int maxKeys, bool unique)
    : _numKeys (0), _keys (NULL), _recAddrs (NULL)
{
  Init (maxKeys, unique);
}

template <class KeyType> SimpleIndex<KeyType>::~SimpleIndex ()
{
  delete _keys;
  delete _recAddrs;
}

template <class KeyType>
void
SimpleIndex<KeyType>::Clear ()
{
  _numKeys = 0;
}

template <class KeyType>
int
SimpleIndex<KeyType>::Insert (const KeyType key, int recAddr)
{
  int i;
  int index = Find (key);
  if (_unique && index >= 0)
    return false; // key already in
  if (_numKeys == _maxKeys)
    return false; // no room

  for (i = _numKeys - 1; i >= 0; i--)
    {
      if (key > _keys[i])
        break; // insert into loc i+1
      _keys[i + 1] = _keys[i];
      _recAddrs[i + 1] = _recAddrs[i];
    }
  _keys[i + 1] = key;
  _recAddrs[i + 1] = recAddr;
  _numKeys++;
  return true;
}

template <class KeyType>
int
SimpleIndex<KeyType>::Remove (const KeyType key, const int recAddr)
{

  int index = Find (key, recAddr);
  if (index < 0)
    return false; // key not in index

  for (int i = index; i < _numKeys; i++)
    {
      _keys[i] = _keys[i + 1];
      _recAddrs[i] = _recAddrs[i + 1];
    }
  _numKeys--;
  return true;
}

template <class KeyType>
int
SimpleIndex<KeyType>::Search (const KeyType key, const int recAddr,
                              const bool exact)
{
  int index = Find (key, recAddr, exact);
  if (index < 0)
    return index;
  return _recAddrs[index];
}

template <class KeyType>
void
SimpleIndex<KeyType>::Print (ostream &stream) const
{
  // stream << "_maxKeys=" << _maxKeys << ", _numKeys=" << _numKeys << endl;
  for (int i = 0; i < _numKeys; i++)
    {
      // stream << "\t_keys[" << i << "]=" << _keys[i] << " _recAddrs[" << i
      //        << "]=" << _recAddrs[i] << endl;
      stream << "[" << _keys[i] << "|" << _recAddrs[i] << "]";
    }

  if (_numKeys < _maxKeys)
    {
      for (int i = 0; i < _maxKeys - _numKeys; i++)
        {
          stream << "[|]";
        }
    }
}

template <class KeyType>
void
SimpleIndex<KeyType>::PrintGraphviz (ostream &stream) const
{
  if (_numKeys > 0)
    {
      stream << "<P" << _recAddrs[0] << ">" << _keys[0];
    }
  for (int i = 1; i < _numKeys; i++)
    {
      stream << "|<P" << _recAddrs[i] << ">" << _keys[i];
    }
  if (_numKeys < _maxKeys)
    {
      for (int i = 0; i < _maxKeys - _numKeys; i++)
        {
          stream << "|";
        }
    }
}

template <class KeyType>
int
SimpleIndex<KeyType>::Find (const KeyType key, const int recAddr,
                            const bool exact) const
{
  for (int i = 0; i < _numKeys; i++)
    {
      if (_keys[i] < key)
        continue; // not found yet
      else if (_keys[i] == key)
        { // exact match
          if (recAddr < 0)
            return i;
          else if (recAddr == _recAddrs[i])
            return i;
          else
            return -1;
        }
      else
        {
          if (!exact)
            return i; // not eaxt match: _keys[i-1] < key < _keys[i]
          else
            return -1;
        }
    }

  // key > all _keys[i]
  if (exact)
    return -1; // no exact match
  else
    return _numKeys - 1; // match last key
}

template <class KeyType>
int
SimpleIndex<KeyType>::Init (const int maxKeys, const bool unique)
{
  _unique = unique;
  if (maxKeys <= 0)
    {
      _maxKeys = 0;
      return false;
    }

  _maxKeys = maxKeys;
  _keys = new KeyType[_maxKeys];
  _recAddrs = new int[_maxKeys];
  return true;
}

/// IndexBuffer
template <class KeyType>
IndexBuffer<KeyType>::IndexBuffer (int maxKeys, int keySize)
    : FixedFieldBuffer (1 + 2 * maxKeys, sizeof (int) + maxKeys * keySize
                                             + maxKeys * sizeof (int))
{
  _maxKeys = maxKeys;
  _keySize = keySize;
  AddField (sizeof (int));
  for (int i = 0; i < _maxKeys; i++)
    {
      AddField (_keySize);
      AddField (sizeof (int));
    }
}

template <class KeyType>
int
IndexBuffer<KeyType>::Pack (const SimpleIndex<KeyType> &index)
{
  int result;
  Clear ();
  result = FixedFieldBuffer::Pack (&index._numKeys);
  for (int i = 0; i < index._numKeys; i++)
    {
      result = result && FixedFieldBuffer::Pack (&index._keys[i]);
      result = result && FixedFieldBuffer::Pack (&index._recAddrs[i]);
    }
  return result;
}

template <class KeyType>
int
IndexBuffer<KeyType>::Unpack (SimpleIndex<KeyType> &index)
{
  int result;
  result = FixedFieldBuffer::Unpack (&index._numKeys);
  for (int i = 0; i < index._numKeys; i++)
    {
      result = result && FixedFieldBuffer::Unpack (&index._keys[i]);
      result = result && FixedFieldBuffer::Unpack (&index._recAddrs[i]);
    }
  return result;
}

template <class KeyType>
void
IndexBuffer<KeyType>::Print (ostream &stream) const
{
  stream << "IndexBuffer: _keySize=" << _keySize << ", _maxKeys=" << _maxKeys
         << endl;
  FixedFieldBuffer::Print (stream);
}
