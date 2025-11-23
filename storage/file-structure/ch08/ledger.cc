#include "ledger.h"
#include <string>

using namespace std;

/// Ledger

int
Ledger::Pack (IOBuffer &buffer) const
{
  buffer.Clear ();
  buffer.Pack (&Acct, sizeof (Acct));
  buffer.Pack (&Title, -1);
  for (int i = 0; i < 12; i++)
    {
      buffer.Pack (&Balances[i], sizeof (Balances[i]));
    }

  return true;
}

int
Ledger::Unpack (IOBuffer &buffer)
{
  buffer.Unpack (&Acct, sizeof (Acct));
  buffer.Unpack (&Title, sizeof (Title));
  for (int i = 0; i < 12; i++)
    {
      buffer.Unpack (&Balances[i], sizeof (Balances[i]));
    }
  return true;
}

ostream &
Ledger::Print (ostream &stream)
{
  stream.setf (ios::right, ios::adjustfield);
  stream << Acct << "\t";
  stream.setf (ios::left, ios::adjustfield);
  stream << setw (20) << Title;
  stream.setf (ios::right, ios::adjustfield);
  stream.setf (ios::right | ios::fixed, ios::floatfield);
  stream.precision (2);
  for (int i = 0; i < 12; i++)
    {
      if (Balances[i] < 0.005 && i > 2)
        break;
      stream << "\t" << setw (8) << Balances[i];
    }
  stream << endl;
  return stream;
}

ostream &
Ledger::PrintHeader (ostream &stream)
{
  stream << Acct << "\t" << Title << endl;
  return stream;
}

Ledger::Ledger ()
{
  Acct = 0;
  Title[0] = 0;
  for (int i = 0; i < 12; i++)
    Balances[i] = 0.0;
}

Ledger::Ledger (int acct, const char *title, double jan, double feb,
                double mar)
{
  Acct = acct;
  strcpy (Title, title);
  Balances[0] = jan;
  Balances[1] = feb;
  Balances[2] = mar;
  for (int i = 3; i < 12; i++)
    Balances[i] = 0.0;
}

ostream &
PrintBalances (ostream &stream, double previousBalance, double currentBalance)
{
  stream << "\t\t\tPrev. bal: " << setw (8) << setprecision (2)
         << previousBalance << "\tNew bal: " << setw (8) << setprecision (2)
         << currentBalance << endl;
  return stream;
}

/// Journal

int
Journal::Pack (IOBuffer &buffer) const
{
  buffer.Clear ();
  buffer.Pack (&Acct, sizeof (Acct));
  buffer.Pack (&CheckNum, sizeof (CheckNum));
  buffer.Pack (&Date, -1);
  buffer.Pack (&Description, -1);
  buffer.Pack (&Amount, sizeof (Amount));
  return true;
}

int
Journal::Unpack (IOBuffer &buffer)
{
  buffer.Unpack (&Acct, sizeof (Acct));
  buffer.Unpack (&CheckNum, sizeof (CheckNum));
  buffer.Unpack (&Date, -1);
  buffer.Unpack (&Description, sizeof (Description));
  buffer.Unpack (&Amount, sizeof (Amount));
  return true;
}

ostream &
Journal::PrintLine (ostream &stream)
{
  stream.setf (ios::right, ios::adjustfield);
  stream << "\t" << setw (6) << CheckNum << "\t";
  stream.setf (ios::left, ios::adjustfield);
  stream << Date << "\t" << setw (30) << Description;
  stream.setf (ios::right, ios::adjustfield);
  stream.setf (ios::right | ios::fixed, ios::floatfield);
  stream.precision (2);
  stream << "\t" << setw (8) << Amount << endl;
  return stream;
}

Journal::Journal ()
{
  Acct = 0;
  CheckNum = 0;
  Date[0] = 0;
  Description[0] = 0;
  Amount = 0.0;
}

Journal::Journal (int acct, int checkNum, const char *date,
                  const char *description, double amount)
{
  Acct = acct;
  CheckNum = checkNum;
  strcpy (Date, date);
  strcpy (Description, description);
  Amount = amount;
}

/// LedgerProcess

int LedgerProcess::LowAcct = 0;
int LedgerProcess::HighAcct = 9999;

LedgerProcess::LedgerProcess (int monthNumber)
    : MasterTransactionProcess<int> (), _ledgerFile (_ledgerBuffer),
      _journalFile (_journalBuffer)
{
  DEBUG ("_monthNumber={}", _monthNumber);
  _monthNumber = monthNumber;
}

int
LedgerProcess::InitializeList (int listNumber, const char *listName)
{
  _accountNumber[listNumber] = LowAcct;
  switch (listNumber)
    {
    case 1:
      return _ledgerFile.Open (listName, ios::in);
    case 2:
      return _journalFile.Open (listName, ios::in);
    }
  return false;
}

int
LedgerProcess::InitializeOutput (const char *outputListName)
{
  _outputList.open (outputListName, ios::out | ios::trunc);
  if (!_outputList.good () || !_outputList)
    ERROR ("Open {} failed", outputListName);
  else
    DEBUG ("Open {} succeed", outputListName);
  return _outputList.good ();
}

int
LedgerProcess::NextItemInLIst (int listNumber)
{
  switch (listNumber)
    {
    case 1:
      return NextItemInLedger ();
    case 2:
      return NextItemInJournal ();
    }
  return false;
}

int
LedgerProcess::Item (int listNumber)
{
  return _accountNumber[listNumber];
}

int
LedgerProcess::ProcessItem (int listNumber)
{
  switch (listNumber)
    {
    case 1:
      // _ledger.PrintHeader (_outputList);
      _ledger.PrintHeader (cout);
      break;
    case 2:
      // _journal.PrintLine (_outputList);
      _journal.PrintLine (cout);
      break;
    }
  return true;
}

int
LedgerProcess::FinishUp ()
{
  _ledgerFile.Close ();
  _journalFile.Close ();
  _outputList.close ();
  return true;
}

int
LedgerProcess::ProcessNewMaster ()
{
  // _ledger.PrintHeader (_outputList);
  _ledger.PrintHeader (cout);
  _ledger.Balances[_monthNumber] = _ledger.Balances[_monthNumber - 1];

  return true;
}

int
LedgerProcess::ProcessCurrentMaster ()
{
  _ledger.Balances[_monthNumber] += _journal.Amount;
  return true;
}

int
LedgerProcess::ProcessEndMaster ()
{
  // PrintBalances (_outputList, _ledger.Balances[_monthNumber - 1],
  //  _ledger.Balances[_monthNumber]);
  PrintBalances (cout, _ledger.Balances[_monthNumber - 1],
                 _ledger.Balances[_monthNumber]);
  return true;
}

int
LedgerProcess::ProcessTransactionError ()
{
  cerr << "Error no account for journal" << endl;
  cerr << _journal.Acct;
  // _journal.PrintLine (cerr); // MORE
  return true;
}

int
LedgerProcess::NextItemInLedger ()
{
  int result;

  result = _ledgerFile.Read (_ledger);
  if (result <= 0)
    {
      // ERROR ("Read ledger failed: {}", result);
      _accountNumber[1] = HighAcct;
      return false;
    }
  if (_ledger.Acct <= _accountNumber[1])
    {
      cerr << "Ledger account out of order" << endl;
      exit (0);
    }

  _accountNumber[1] = _ledger.Acct;
  return true;
}

int
LedgerProcess::NextItemInJournal ()
{
  int result;

  result = _journalFile.Read (_journal);
  if (result <= 0)
    {
      // ERROR ("Read journal failed: {}", result);
      _accountNumber[2] = HighAcct;
      return false;
    }
  if (_journal.Acct < _accountNumber[2])
    {
      cerr << "Journal account out of order" << endl;
      exit (0);
    }
  _accountNumber[2] = _journal.Acct;
  return true;
}