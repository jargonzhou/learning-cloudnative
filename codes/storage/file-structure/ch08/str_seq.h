#ifndef CH08_STR_SEQ
#define CH08_STR_SEQ

#include "coseq.h"
#include "fs_string.h"
#include <fstream>

using namespace std;

class StringListProcess : public CosequentialProcess<FSString &>
{
public:
  StringListProcess (int numberOfLists);

  int InitializeList (int listNumber, const char *listName);
  int InitializeOutput (const char *outputListName);
  int NextItemInLIst (int listNumber); //!< advance to next item in this list
  FSString &Item (int listNumber);     //!< current item in this list
  int ProcessItem (int listNumber);    //!< process the item in this list
  int FinishUp ();                     //!< compelete the processing

protected:
  ifstream *_lists;
  FSString *_items;
  ofstream _outputList;
  static const char *LowValue;
  static const char *HighValue;
};

const int MaxItemLength = 100;

#endif /* CH08_STR_SEQ */
