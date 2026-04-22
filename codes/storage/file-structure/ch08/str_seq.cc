#include "str_seq.h"

const char *StringListProcess::LowValue = "";
const char *StringListProcess::HighValue = "\xff";

StringListProcess::StringListProcess (int numberOfLists)
    : CosequentialProcess<FSString &> (numberOfLists), _outputList ()
{
  _lists = new ifstream[_numberOfLists + 1];
  _items = new FSString[_numberOfLists + 1];
}

int
StringListProcess::InitializeList (int listNumber, const char *listName)
{
  _lists[listNumber].open (listName, ios::in);
  _items[listNumber] = LowValue;
  return 1;
}

int
StringListProcess::InitializeOutput (const char *outputListName)
{
  _outputList.open (outputListName, ios::out);
  return 1;
}

int
StringListProcess::NextItemInLIst (int listNumber)
{
  char thisItem[MaxItemLength];
  FSString previousItem = _items[listNumber];
  _lists[listNumber].getline (thisItem, MaxItemLength + 1);

  if (!_lists[listNumber].good ())
    {
      _items[listNumber] = HighValue;
      return 0;
    }

  if (strlen (thisItem) == 0)
    {
      _items[listNumber] = LowValue;
      return 0;
    }

  if (strcmp (thisItem, (char *)previousItem) < 0)
    {
      cerr << "item out of order: current=" << thisItem
           << ", previous=" << previousItem.str () << endl;
      _items[listNumber] = FSString (HighValue);
      return 0;
    }

  cout << "DEBUG: NextItemInLIst(" << listNumber << ")=" << thisItem << endl;
  _items[listNumber] = FSString (thisItem);
  return 1;
}

FSString &
StringListProcess::Item (int listNumber)
{
  return _items[listNumber];
}

int
StringListProcess::ProcessItem (int listNumber)
{
  _outputList << _items[listNumber] << endl;
  return 1;
}

int
StringListProcess::FinishUp ()
{
  for (int i = 0; i <= _numberOfLists; i++)
    {
      _lists[i].close ();
    }
  _outputList.close ();
  return 1;
}
