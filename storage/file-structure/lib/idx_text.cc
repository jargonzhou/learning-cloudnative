#include "idx_text.h"
#include <string>

/// TextIndex

TextIndex::TextIndex (int maxKeys, bool unique)
    : _numKeys (0), _keys (NULL), _recAddrs (NULL)
{
  Init (maxKeys, unique);
}

TextIndex::~TextIndex ()
{
  delete _keys;
  delete _recAddrs;
}

int
TextIndex::Insert (const char *key, int recaddr)
{
  int i;
  int index = Find (key);
  if (_unique && index > 0)
    return 0;
  if (_numKeys == _maxKeys)
    return 0;

  for (i = _numKeys - 1; i >= 0; i--)
    {
      if (strcmp (key, _keys[i]) > 0)
        break;

      _keys[i + 1] = _keys[i];
      _recAddrs[i + 1] = _recAddrs[i];
    }

  _keys[i + 1] = strdup (key);
  _recAddrs[i + 1] = recaddr;
  _numKeys++;
  return 1;
}

int
TextIndex::Remove (const char *key)
{
  int index = Find (key);
  if (index < 0)
    return 0;

  for (int i = index; i < _numKeys; i++)
    {
      _keys[i] = _keys[i + 1];
      _recAddrs[i] = _recAddrs[i + 1];
    }
  _numKeys--;
  return 1;
}

int
TextIndex::Search (const char *key) const
{
  int index = Find (key);
  if (index < 0)
    return index;
  return _recAddrs[index];
}

void
TextIndex::Print (ostream &stream) const
{
  stream << "text Index _maxKeys=" << _maxKeys << ", _numKeys=" << _numKeys
         << endl;
  for (int i = 0; i < _numKeys; i++)
    {
      stream << "\tKey[" << i << "]=" << _keys[i]
             << " RecAddr=" << _recAddrs[i] << endl;
    }
}

int
TextIndex::Find (const char *key) const
{
  for (int i = 0; i < _numKeys; i++)
    {
      if (strcmp (_keys[i], key) == 0)
        return i;
      else if (strcmp (_keys[i], key) > 0)
        return -1;
    }
  return -1;
}

int
TextIndex::Init (int maxKeys, bool unique)
{
  _unique = unique;
  if (maxKeys <= 0)
    {
      _maxKeys = 0;
      return 0;
    }
  _maxKeys = maxKeys;
  _keys = new char *[_maxKeys];
  _recAddrs = new int[_maxKeys];
  return 1;
}

/// TextIndexBuffer

TextIndexBuffer::TextIndexBuffer (int keySize, int maxKeys, int extraFields,
                                  int extraSpace)
    : FixedFieldBuffer (1 + 2 * maxKeys + extraFields,
                        sizeof (int) + maxKeys * keySize
                            + maxKeys * sizeof (int) + extraSpace)
{
  _maxKeys = maxKeys;
  _keySize = keySize;
  AddField (sizeof (int));
  for (int i = 0; i < _maxKeys; i++)
    {
      AddField (_keySize);
      AddField (sizeof (int));
    }
  _dummy = new char[_keySize + 1];
}

int
TextIndexBuffer::Pack (const TextIndex &index)
{
  int result;
  Clear ();
  result = FixedFieldBuffer::Pack (&index._numKeys);
  for (int i = 0; i < index._numKeys; i++)
    {
      result = result && FixedFieldBuffer::Pack (index._keys[i]);
      result = result && FixedFieldBuffer::Pack (&index._recAddrs[i]);
    }

  for (int j = 0; j < index._maxKeys - index._numKeys; j++)
    {
      result = result && FixedFieldBuffer::Pack (_dummy);
      result = result && FixedFieldBuffer::Pack (_dummy);
    }
  return result;
}

int
TextIndexBuffer::Unpack (TextIndex &index)
{
  int result;
  result = FixedFieldBuffer::Unpack (&index._numKeys);
  for (int i = 0; i < index._numKeys; i++)
    {
      index._keys[i] = new char[_keySize];
      result = result && FixedFieldBuffer::Unpack (index._keys[i]);
      result = result && FixedFieldBuffer::Unpack (&index._recAddrs[i]);
    }
  for (int j = 0; j < index._maxKeys - index._numKeys; j++)
    {
      result = result && FixedFieldBuffer::Unpack (_dummy);
      result = result && FixedFieldBuffer::Unpack (_dummy);
    }
  return result;
}

void
TextIndexBuffer::Print (ostream &stream) const
{
  stream << "TextIndexBuffer: _keySize=" << _keySize
         << ", _maxKeys=" << _maxKeys << endl;
  FixedFieldBuffer::Print (stream);
}