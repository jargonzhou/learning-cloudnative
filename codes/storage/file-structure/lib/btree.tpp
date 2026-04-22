#include "log.h"
#include <string> // for std::string

/// BTree

const int MAX_HEIGHT = 5;

template <class KeyType>
BTree<KeyType>::BTree (int order, int keySize, bool unique)
    : _buffer (
        // maxFields
        1 + 2 * order,
        // recordSize: _numKeys, _keys[_numKeys], _recAddrs[_numKeys]
        sizeof (int) + order * keySize + order * sizeof (int)),
      _BTreeFile (_buffer), _root (order)
{
  _height = 1;
  _order = order;
  _poolSize = MAX_HEIGHT * 2;
  _nodes = new BTreeNode<KeyType> *[_poolSize];
  BTreeNode<KeyType>::InitBuffer (_buffer, order);
  _nodes[0] = &_root;

  DEBUG ("_height={}, _order={}, keySize={}, recordSize={}", _height, _order,
         keySize, sizeof (int) + order * keySize + order * sizeof (int));
}

template <class KeyType> BTree<KeyType>::~BTree ()
{
  Close ();
  delete _nodes;
}

template <class KeyType>
int
BTree<KeyType>::Open (const char *filename, int mode)
{
  int result;
  result = _BTreeFile.Open (filename, mode);
  if (!result)
    {
      return result;
    }

  _BTreeFile.Read (_root);
  _height = 1;
  return 1;
}

template <class KeyType>
int
BTree<KeyType>::Create (const char *filename, int mode)
{
  DEBUG ("Create file {} with mode {} failed", filename, mode);
  int result;
  result = _BTreeFile.Create (filename, mode);
  if (!result)
    {
      ERROR ("Create file {} with mode {} failed", filename, mode);
      return result;
    }
  result = _BTreeFile.Write (_root);
  _root._recAddr = result;
  DEBUG ("Write _root at {}: {} ", result, _root.str ());
  return result != -1;
}

template <class KeyType>
int
BTree<KeyType>::Close ()
{
  // store the root node to the first record
  int result;
  result = _BTreeFile.Rewind ();
  if (!result)
    {
      return result;
    }

  result = _BTreeFile.Write (_root);
  if (result == -1)
    {
      return 0;
    }
  return _BTreeFile.Close ();
}

//! search down to the leaf level to find the insert location
//! do work of insertion, overflow, splitting upward to root
template <class KeyType>
int
BTree<KeyType>::Insert (const KeyType key, const int recAddr)
{
  DEBUG ("Insert key={}, recAddr={}", key, recAddr);
  int result;
  int level = _height - 1;
  bool newLargest = false;
  KeyType prevKey, largestKey;
  BTreeNode<KeyType> *thisNode, *newNode, *parentNode;

  thisNode = FindLeaf (key);

  if (key > thisNode->LargestKey ())
    {
      newLargest = true;
      prevKey = thisNode->LargestKey ();
    }

  result = thisNode->Insert (key, recAddr);

  if (newLargest)
    {
      for (int i = 0; i < _height - 1; i++)
        {
          _nodes[i]->UpdateKey (prevKey, key);
          if (i > 0)
            Store (_nodes[i]);
        }
    }

  while (result == -1) // overflow and not root
    {
      DEBUG ("Insert key={}, recAddr={} overflow", key, recAddr);
      largestKey = thisNode->LargestKey ();
      // split the node
      newNode = NewNode ();
      thisNode->Split (newNode);
      Store (thisNode);
      Store (newNode);

      level--; // to parent level
      if (level < 0)
        break;

      parentNode = _nodes[level];
      result = parentNode->UpdateKey (largestKey, thisNode->LargestKey ());
      result = parentNode->Insert (newNode->LargestKey (), newNode->_recAddr);
      thisNode = parentNode;
    }
  Store (thisNode);
  if (level >= 0)
    return 1; // insert complete

  // split the root
  DEBUG ("Append old root node at recAddr={}: {}", recAddr, _root.str ());
  int newAddr = _BTreeFile.Append (_root); // put previous root into file
  _root._keys[0] = thisNode->LargestKey ();
  _root._recAddrs[0] = newAddr;
  _root._keys[1] = newNode->LargestKey ();
  _root._recAddrs[1] = newNode->_recAddr;
  _root._numKeys = 2;
  _height++;
  return 1;
}

template <class KeyType>
int
BTree<KeyType>::Remove (const KeyType key, const int recAddr)
{
  // TODO
  return -1;
}

//! load a page into memory, search through the page, looking for
//! the key at succesive lower levels until it reaches the leaf level.
template <class KeyType>
int
BTree<KeyType>::Search (const KeyType key, const int recAddr)
{
  BTreeNode<KeyType> *leafNode;
  leafNode = FindLeaf (key);
  return leafNode->Search (key, recAddr);
}

template <class KeyType>
void
BTree<KeyType>::Print (ostream &stream)
{
  stream << "BTree[ _height=" << _height << ", _order=" << _order << endl;
  stream << "_root=";
  _root.Print (stream);
  if (_height > 1)
    {
      for (int i = 0; i < _root._numKeys; i++)
        {
          Print (stream, _root._recAddrs[i], 2);
        }
    }
  stream << "]" << endl;
}

template <class KeyType>
void
BTree<KeyType>::Print (ostream &stream, int nodeAddr, int level)
{
  BTreeNode<KeyType> *thisNode = Fetch (nodeAddr);
  stream << endl
         << std::string (level - 1, '\t') << "Node at level=" << level
         << ", address=" << nodeAddr << ": ";
  thisNode->Print (stream);
  if (_height > level)
    {
      level++;
      for (int i = 0; i < thisNode->_numKeys; i++)
        {
          Print (stream, thisNode->_recAddrs[i], level);
        }
    }
}

template <class KeyType>
void
BTree<KeyType>::PrintGraphvizHeader (ostream &stream, const char *gname)
{
  stream << "digraph " << gname << " {" << endl;
  stream << "\tsplines=line;" << endl;
}

template <class KeyType>
void
BTree<KeyType>::PrintGraphviz (ostream &stream)
{
  // root[shape=record, label="<P230>I|<P302>P|<Z206>Z||"];
  stream << "\tnode[shape = record, width = 1, height = 0.1];" << endl;
  stream << "\troot[shape=record, label=\"";
  _root.PrintGraphviz (stream);
  stream << "\"];" << endl;
  if (_height > 1)
    {
      for (int i = 0; i < _root._numKeys; i++)
        {
          PrintGraphviz (stream, _root._recAddrs[i], 2);
          // root:P230->L2_P230;
          stream << "root:P" << _root._recAddrs[i] << " -> "
                 << "L2_P" << _root._recAddrs[i] << ";" << endl;
        }
    }
  stream << "}" << endl;
}

template <class KeyType>
void
BTree<KeyType>::PrintGraphviz (ostream &stream, int nodeAddr, int level)
{
  BTreeNode<KeyType> *thisNode = Fetch (nodeAddr);

  // L2_P230[shape=record, label="<P110>D|<P86>G|<P278>I||"];
  stream << "L" << level << "_P" << nodeAddr << "[shape=record, label=\"";
  thisNode->PrintGraphviz (stream);
  stream << "\"];" << endl;

  if (_height > level)
    {
      level++;
      for (int i = 0; i < thisNode->_numKeys; i++)
        {
          PrintGraphviz (stream, thisNode->_recAddrs[i], level);
          // L2_P230:P110->L3_P110;
          stream << "L" << level - 1 << "_P" << nodeAddr << ":P"
                 << thisNode->_recAddrs[i] << " -> "
                 << "L" << level << "_P" << thisNode->_recAddrs[i] << ";"
                 << endl;
        }
    }
}

//! Search down a branch of the tree, beginning at the root, _nodes[0]
template <class KeyType>
BTreeNode<KeyType> *
BTree<KeyType>::FindLeaf (const KeyType key)
{
  int recAddr, level;
  for (level = 1; level < _height; level++)
    {
      recAddr = _nodes[level - 1]->Search (key, -1, false); // inexact search
      _nodes[level] = Fetch (recAddr); // read next level node
    }
  return _nodes[level - 1];
}

template <class KeyType>
BTreeNode<KeyType> *
BTree<KeyType>::NewNode ()
{
  BTreeNode<KeyType> *newNode = new BTreeNode<KeyType> (_order);
  int recAddr = _BTreeFile.Append (*newNode);
  newNode->_recAddr = recAddr;
  DEBUG ("Append new node at recAddr={}", recAddr);
  return newNode;
}

template <class KeyType>
BTreeNode<KeyType> *
BTree<KeyType>::Fetch (const int recAddr)
{
  DEBUG ("Read at recAddr={}", recAddr);
  int result;
  BTreeNode<KeyType> *newNode = new BTreeNode<KeyType> (_order);
  result = _BTreeFile.Read (*newNode, recAddr);
  if (result == -1)
    {
      ERROR ("Read at recAddr={} failed", recAddr);
      return NULL;
    }
  newNode->_recAddr = result;
  return newNode;
}

template <class KeyType>
int
BTree<KeyType>::Store (BTreeNode<KeyType> *tnode)
{

  // DEBUG ("Store at {}: {}", tnode->_recAddr, tnode->str ());
  int result;
  result = _BTreeFile.Write (*tnode, tnode->_recAddr);
  if (result == -1)
    ERROR ("Store BTreeNode: {} failed", tnode->str ());
  return result;
}