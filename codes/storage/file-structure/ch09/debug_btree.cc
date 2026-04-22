#include "btree.h"
#include "buf.h"
#include "rec_file.h"

using namespace std;
int
main (int argc, char const *argv[])
{
  init_log ();

  FixedFieldBuffer _buffer (9, 24);
  _buffer.AddField (sizeof (int));
  for (int i = 0; i < 4; i++)
    {
      _buffer.AddField (1);
      _buffer.AddField (sizeof (int));
    }

  RecordFile<BTreeNode<char> > _BTreeFile (_buffer);
  int result, result1, result2;
  // find bug: _numKeys=0
  // reason: ios:out => this openmode settings
  result = _BTreeFile.Create ("btree2.fsdat",
                              ios::in | ios::out | ios::binary | ios::trunc);

  BTreeNode<char> node (4);
  node.Insert ('A', 0);
  node.Insert ('B', 1);
  node.Insert ('C', 2);
  node.Insert ('D', 3);
  result1 = _BTreeFile.Write (node);
  std::cout << "result1=" << result1 << std::endl;

  node.Remove ('A', 0);
  result2 = _BTreeFile.Append (node);
  std::cout << "result2=" << result2 << std::endl;

  BTreeNode<char> node2 (4);
  result = _BTreeFile.Read (node2, result1);
  std::cout << "result=" << result << std::endl;
  std::cout << node2.str () << std::endl;

  BTreeNode<char> node3 (4);
  result = _BTreeFile.Read (node3, result2);
  std::cout << "result=" << result << std::endl;
  std::cout << node3.str () << std::endl;

  _BTreeFile.Close ();
  return 0;
}
