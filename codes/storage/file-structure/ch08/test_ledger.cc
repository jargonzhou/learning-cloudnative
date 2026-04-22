#include "ledger.h"
#include "log.h"

using namespace std;

const char *LEDGER_FILE_NAME = "ledger.fsdat";
const char *JOURNAL_FILE_NAME = "journal.fsdat";

int
prepareData ()
{
  int result;

  Ledger ledgers[]
      = { Ledger (101, "Checking Account #1", 1032.57, 2114.56, 5219.23),
          Ledger (102, "Checking Account #2", 543.78, 3094.17, 1321.20),
          Ledger (505, "Advertising expense", 25.00, 25.00, 25.00),
          Ledger (510, "Auto expenses", 195.40, 307.92, 501.12),
          Ledger (515, "Bank charges", 0.00, 0.00, 0.00),
          Ledger (520, "Books and publications", 27.95, 27.95, 87.40),
          Ledger (525, "Interest expense", 103.50, 255.20, 380.27),
          Ledger (535, "Miscellaneous expense", 12.45, 17.87, 23.87),
          Ledger (540, "Office expense", 57.50, 105.25, 138.37),
          Ledger (545, "Postage and shipping", 21.00, 27.63, 57.45),
          Ledger (550, "Rent", 500.00, 1000.00, 1500.00),
          Ledger (555, "Supplies", 112.00, 167.50, 2441.80) };

  Journal journals[]
      = { Journal (101, 1271, "04/02/86", "Auto expense", -78.70),
          Journal (101, 1272, "04/02/97", "Rent", -500.00),
          Journal (101, 1273, "04/04/97", "Advertising", -87.50),
          Journal (101, 1274, "04/02/97", "Auto expense", -31.83),
          Journal (102, 670, "04/02/97", "Office expense", -32.78),
          Journal (505, 1273, "04/04/97", "Newspaper ad re: new product",
                   87.50),
          Journal (510, 1271, "04/02/97", "Tune up and minor repair", 78.70),
          Journal (510, 1274, "04/09/97", "Oil change", 31.83),
          Journal (540, 670, "04/02/97", "Printer cartridge", 32.78),
          Journal (550, 1272, "04/02/97", "Rent for April", 500.00) };

  // write
  LengthFieldBuffer ledgerBuffer;
  RecordFile<Ledger> ledgerFile (ledgerBuffer);
  result = ledgerFile.Create (LEDGER_FILE_NAME, ios::out);
  if (!result)
    {
      ERROR ("Create {} failed", LEDGER_FILE_NAME);
    }
  int i;
  for (i = 0; i < 12; i++)
    {
      result = ledgerFile.Write (ledgers[i]);
      if (result <= 0)
        {
          cout << "write ledger failed: ";
          ledgers[i].Print (cout);
        }
    }
  ledgerFile.Close ();
  DEBUG ("Write {} succeed", LEDGER_FILE_NAME);

  LengthFieldBuffer journalBuffer;
  RecordFile<Journal> journalFile (journalBuffer);
  result = journalFile.Create (JOURNAL_FILE_NAME, ios::out);
  if (!result)
    {
      ERROR ("Create {} failed", JOURNAL_FILE_NAME);
    }

  for (i = 0; i < 10; i++)
    {
      result = journalFile.Write (journals[i]);
      if (result <= 0)
        {
          cout << "write journal failed: ";
          journals[i].PrintLine (cout);
        }
    }
  journalFile.Close ();
  DEBUG ("Write {} succeed", JOURNAL_FILE_NAME);

  // read
  RecordFile<Ledger> ledgerFile2 (ledgerBuffer);
  result = ledgerFile2.Open (LEDGER_FILE_NAME, ios::in);
  if (!result)
    {
      ERROR ("Open {} failed", LEDGER_FILE_NAME);
    }

  Ledger ledger;
  while (true)
    {
      result = ledgerFile2.Read (ledger);
      if (result <= 0)
        {
          ERROR ("Read Ledger failed: {}", result);
          break;
        }

      ledger.Print (cout);
    }

  RecordFile<Journal> journalFile2 (journalBuffer);
  result = journalFile2.Open (JOURNAL_FILE_NAME, ios::in);
  if (!result)
    {
      ERROR ("Open {} failed", JOURNAL_FILE_NAME);
    }

  Journal journal;
  while (true)
    {
      result = journalFile2.Read (journal);
      if (result <= 0)
        {
          ERROR ("Read Journal failed: {}", result);
          break;
        }
      journal.PrintLine (cout);
    }

  return true;
}

int
main (int argc, char const *argv[])
{
  init_log ();

  // prepareData ();

  LedgerProcess posting (3);
  posting.PostTransactions (LEDGER_FILE_NAME, JOURNAL_FILE_NAME,
                            "ledger.fstxt");

  return 0;
}
