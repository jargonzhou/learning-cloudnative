#ifndef CH08_LEDGER
#define CH08_LEDGER

#include "buf.h"
#include "coseq.h"
#include "log.h"
#include "rec_file.h"
#include <fstream>
#include <iostream>

using namespace std;

class Ledger
{
public:
  int Acct;
  char Title[30];
  double Balances[12];

  int Pack (IOBuffer &buffer) const;
  int Unpack (IOBuffer &buffer);
  ostream &Print (ostream &stream);
  ostream &PrintHeader (ostream &stream);

  Ledger ();
  Ledger (int acct, const char *title, double jan, double feb, double mar);
};

ostream &PrintBalances (ostream &stream, double previousBalance,
                        double currentBalance);

class Journal
{
public:
  int Acct;
  int CheckNum;
  char Date[10];
  char Description[30];
  double Amount;

  int Pack (IOBuffer &buffer) const;
  int Unpack (IOBuffer &buffer);
  ostream &PrintLine (ostream &stream);

  Journal ();
  Journal (int acct, int checkNum, const char *date, const char *description,
           double amount);
};

template <class ItemType> //
class MasterTransactionProcess : public CosequentialProcess<ItemType>
{
public:
  MasterTransactionProcess ();
  virtual int ProcessNewMaster () = 0;
  virtual int ProcessCurrentMaster () = 0; //!< called when new master read
  virtual int ProcessEndMaster ()
      = 0; //!< called for each transaction for a master
  virtual int ProcessTransactionError () = 0; //!< no master for transaction

  int PostTransactions (
      const char *masterFileName, const char *transactionFileName,
      const char *outputListName); //!< cosequential processing of master and
                                   //!< transaction records
};

template <class ItemType>
MasterTransactionProcess<ItemType>::MasterTransactionProcess ()
    : CosequentialProcess<int> (2)
{
}

template <class ItemType>
int
MasterTransactionProcess<ItemType>::PostTransactions (
    const char *masterFileName, const char *transactionFileName,
    const char *outputListName)
{
  DEBUG ("masterFileName={}, transactionFileName={}, outputListName={}",
         masterFileName, transactionFileName, outputListName);
  int moreMasters, moreTransactions;

  this->InitializeList (1, masterFileName);
  this->InitializeList (2, transactionFileName);
  this->InitializeOutput (outputListName);

  moreMasters = this->NextItemInLIst (1);
  moreTransactions = this->NextItemInLIst (2);
  DEBUG ("moreMasters={}, moreTransactions={}", moreMasters, moreTransactions);

  if (moreMasters)
    this->ProcessNewMaster ();

  while (moreMasters || moreTransactions)
    {
      if (this->Item (1) < this->Item (2))
        { // finish this master record
          this->ProcessEndMaster ();
          moreMasters = this->NextItemInLIst (1);
          if (moreMasters)
            this->ProcessNewMaster ();
        }
      else if (this->Item (1) == this->Item (2))
        { // transaction match master
          this->ProcessCurrentMaster ();
          this->ProcessItem (2);
          moreTransactions = this->NextItemInLIst (2);
        }
      else
        { // transaction with no master
          this->ProcessTransactionError ();
          moreTransactions = this->NextItemInLIst (2);
        }
    }

  this->FinishUp ();
  return 1;
}

class LedgerProcess : public MasterTransactionProcess<int>
{
public:
  LedgerProcess (int monthNumber);

  int InitializeList (int listNumber, const char *listName);
  int InitializeOutput (const char *outputListName);
  int NextItemInLIst (int listNumber);
  int Item (int listNumber);
  int ProcessItem (int listNumber);
  int FinishUp ();

  virtual int ProcessNewMaster ();
  virtual int ProcessCurrentMaster ();
  virtual int ProcessEndMaster ();
  virtual int ProcessTransactionError ();

protected:
  int _monthNumber;
  LengthFieldBuffer _ledgerBuffer, _journalBuffer;
  RecordFile<Ledger> _ledgerFile;
  RecordFile<Journal> _journalFile;
  int _accountNumber[3]; //!< current item in each list
  Ledger _ledger;        //!< current ledger object
  Journal _journal;      //!< current journal object
  ofstream _outputList;  //!< FIXME: NOT WORK!
  static int LowAcct;
  static int HighAcct;

  int NextItemInLedger ();
  int NextItemInJournal ();
};

#endif /* CH08_LEDGER */
