#include "btree.h"
#include "log.h"
#include <strstream>

using namespace std;

const char *keys = "CSDTAMPIBWNGURKEHOLJYQZFXV";
const int BTreeSize = 4;

int
main (int argc, char const *argv[])
{
  // init_log ();

  int result, i;
  BTree<char> btree (BTreeSize, sizeof (char), true);
  result = btree.Create ("btree.fsdat",
                         ios::in | ios::out | ios::binary | ios::trunc);

  const char *data
      = "data[shapre=record, "
        "label=\"{<P0>0|C}|{<P1>1|S}|{<P2>2|D}|{<P3>3|T}|{<P4>4|A}|{<P5>5|M}|{"
        "<P6>6|P}|{<P7>7|I}|{<P8>8|B}|{<P9>9|W}|{<P10>10|N}|{<P11>11|G}|{<P12>"
        "12|U}|{<P13>13|R}|{<P14>14|K}|{<P15>15|E}|{<P16>16|H}|{<P17>17|O}|{<"
        "P18>18|L}|{<P19>19|J}|{<P20>20|Y}|{<P21>21|Q}|{<P22>22|Z}|{<P23>23|F}"
        "|{<P24>24|X}|{<P25>25|V}\"];";
  for (i = 0; i < 26; i++) // DEBUG: 5
    {
      cout << endl;
      // cout << "Insert " << keys[i] << endl;
      result = btree.Insert (keys[i], i);
      // btree.Print (cout);

      ostrstream ostr;
      ostr << "Insert_" << keys[i];
      btree.PrintGraphvizHeader (cout, ostr.str ());
      btree.PrintGraphviz (cout);
    }

  btree.Search (1, 1);

  return 0;
}
