#ifndef CH08_COSEQ
#define CH08_COSEQ

template <class ItemType> //
class CosequentialProcess
{
public:
  CosequentialProcess (int numberOfLists);

  /// basic list processing

  virtual int InitializeList (int listNumber, const char *listName) = 0;
  virtual int InitializeOutput (const char *outputListName) = 0;
  virtual int NextItemInLIst (int listNumber)
      = 0; //!< advance to next item in this list
  virtual ItemType Item (int listNumber) = 0; //!< current item in this list
  virtual int ProcessItem (int listNumber)
      = 0;                     //!< process the item in this list
  virtual int FinishUp () = 0; //!< compelete the processing

  /// general list processing

  virtual int Match2Lists (
      const char *list1Name, const char *list2Name,
      const char *outputListName); //!< 2-way sequential matching algorithm
  virtual int Merge2Lists (
      const char *list1Name, const char *list2Name,
      const char *outputListName); //!< 2-way sequential merging algorithm
protected:
  int _numberOfLists; //!< number of lists to be processed
};

template <class ItemType>
CosequentialProcess<ItemType>::CosequentialProcess (int numberOfLists)
{
  _numberOfLists = numberOfLists;
}

template <class ItemType>
int
CosequentialProcess<ItemType>::Match2Lists (const char *list1Name,
                                            const char *list2Name,
                                            const char *outputListName)
{
  int moreItems;
  InitializeList (1, list1Name);
  InitializeList (2, list2Name);
  InitializeOutput (outputListName);

  moreItems = NextItemInLIst (1) && NextItemInLIst (2);
  while (moreItems)
    {
      if (Item (1) < Item (2))
        moreItems = NextItemInLIst (1);
      else if (Item (1) == Item (2))
        {
          ProcessItem (1); // found match
          moreItems = NextItemInLIst (1) && NextItemInLIst (2);
        }
      else
        moreItems = NextItemInLIst (2);
    }

  FinishUp ();
  return 1;
}

template <class ItemType>
int
CosequentialProcess<ItemType>::Merge2Lists (const char *list1Name,
                                            const char *list2Name,
                                            const char *outputListName)
{
  int moreItems1, moreItems2;

  InitializeList (1, list1Name);
  InitializeList (2, list2Name);
  InitializeOutput (outputListName);

  moreItems1 = NextItemInLIst (1);
  moreItems2 = NextItemInLIst (2);

  while (moreItems1 || moreItems2)
    {
      if (Item (1) < Item (2))
        {
          ProcessItem (1);
          moreItems1 = NextItemInLIst (1);
        }
      else if (Item (1) == Item (2))
        {
          ProcessItem (1);
          moreItems1 = NextItemInLIst (1);
          moreItems2 = NextItemInLIst (2);
        }
      else
        {
          ProcessItem (2);
          moreItems2 = NextItemInLIst (2);
        }
    }

  FinishUp ();
  return 1;
}

#endif /* CH08_COSEQ */
