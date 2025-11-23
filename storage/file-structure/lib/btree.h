#ifndef LIB_BTREE
#define LIB_BTREE

#include "buf.h"
#include "idx.h"
#include "rec_file.h"
#include <strstream>

// template <class KeyType> class BTree; // forward declaration

//! in-memory version of B tree node
template <class KeyType> //
class BTreeNode : public SimpleIndex<KeyType>
{
public:
  BTreeNode (int maxKeys, bool unique = true);
  ~BTreeNode ();

  /// Insert, Remove: 0 for failure, -1 for overflow, 1 for success
  int Insert (const KeyType key, int recAddr);
  int Remove (const KeyType key, int recAddr = -1);
  void Print (ostream &) const;
  void PrintGraphviz (ostream &) const;
  KeyType LargestKey ();
  int Split (BTreeNode<KeyType> *noewNode); //!< move into newNode
  int Merge (BTreeNode<KeyType> *fromNode); //!< move from fromNode
  int UpdateKey (KeyType oldKey, KeyType newKey, int recAddr = -1);
  int Pack (IOBuffer &buffer) const;
  int Unpack (IOBuffer &buffer);
  static int InitBuffer (FixedFieldBuffer &buffer, int maxKeys,
                         int KeySize = sizeof (KeyType));
  static KeyType *NullKey; //!< for padding
  char *str () const;

protected:
  int _nextNode; //!< address of next node at same level
  int _recAddr;  //!< address of this node in the BTree file
  int _minBKeys; //!< minimum number of keys in a node
  int _maxBKeys; //!< maximum number of keys in a node

  int Init ();
  void
  Clear ()
  {
    this->_numKeys = 0;
    _recAddr = -1;
  }
  friend class BTree<KeyType>;
};

template <class KeyType>
char *
BTreeNode<KeyType>::str () const
{
  ostrstream stream;

  stream << "_numKeys=" << this->_numKeys << ", _recAddr=" << this->_recAddr
         << ", _maxBKeys=" << _maxBKeys << endl;
  for (int i = 0; i < _maxBKeys; i++)
    {
      stream << "\t_key[" << i << "]=" << this->_keys[i] << ", _recAddrs[" << i
             << "]=" << this->_recAddrs[i] << endl;
    }
  return stream.str ();
}

//! B tree: an index file associated with a data file
template <class KeyType> //
class BTree
{
public:
  BTree (int order, int keySize = sizeof (KeyType), bool unique = true);
  ~BTree ();
  int Open (const char *filename, int mode);
  int Create (const char *filename, int mode);
  int Close ();
  int Insert (const KeyType key, const int recAddr);
  int Remove (const KeyType key, const int recAddr = -1);
  int Search (const KeyType key, const int recAddr = -1);
  void Print (ostream &stream);
  void Print (ostream &stream, int nodeAddr, int level);
  void PrintGraphvizHeader (ostream &stream, const char *gname);
  void PrintGraphviz (ostream &stream);
  void PrintGraphviz (ostream &stream, int nodeAddr, int level);

protected:
  typedef BTreeNode<KeyType> BTNode;
  BTNode *FindLeaf (const KeyType key); //!< load a bracnch into memory down to
                                        //!< the leaf with key
  BTNode *NewNode ();                //!< create a fresh node, insert into tree
  BTNode *Fetch (const int recAddr); //!< load from file into a new BTNode
  int Store (BTNode *tnode);         //!< store BTNode to file

  BTNode _root;    //!< root node in memory
  int _height;     //!< height of tree
  int _order;      //!< order of tree
  int _poolSize;   //!< storage size
  BTNode **_nodes; //!< storage for a branch
  FixedFieldBuffer _buffer;
  RecordFile<BTNode> _BTreeFile;
};

#include "btree.tpp"
#include "btreenode.tpp"

#endif /* LIB_BTREE */
