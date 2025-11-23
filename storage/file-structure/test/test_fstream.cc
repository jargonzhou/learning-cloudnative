#include "log.h"
#include <fstream>
#include <iostream>

using namespace std;

class Ledger
{
public:
  int Acct;
  char Title[30];
  double Balances[12];

  ostream &Print (ostream &);
  ostream &PrintHeader (ostream &);

  Ledger ();
  Ledger (int acct, const char *title, double jan, double feb, double mar);
};

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
write_something (ostream &stream, const char *data)
{
  stream << data << endl;
  stream.setf (ios::right, ios::adjustfield);
  stream << "\t" << setw (6) << 123 << "\t";
  stream.setf (ios::left, ios::adjustfield);
  stream << "2021/01/01"
         << "\t" << setw (30) << "Description";
  stream.setf (ios::right, ios::adjustfield);
  stream.setf (ios::right | ios::fixed, ios::floatfield);
  stream.precision (2);
  stream << "\t" << setw (8) << 12.34 << endl;

  return stream;
}

void
test_ledger ()
{
  // fstream
  const char *filename = "test_ledger.fsdat";
  fstream stream;

  stream.open (filename, ios::out | ios::binary);
  if (!stream.good ())
    {
      ERROR ("Open file {} failed", filename);
    }
  const char *data = "hello";
  stream.write (data, sizeof (data));
  stream.close ();

  // ofstream
  const char *filename2 = "test2.fsdat";
  ofstream outf;
  outf.open (filename2, ios::out | ios::trunc);
  if (!outf.good ())
    {
      ERROR ("Open file {} failed", filename2);
    }
  outf << data << endl;
  write_something (outf, data);

  Ledger ledger (101, "Checking Account #1", 1032.57, 2114.56, 5219.23);
  ledger.Print (outf);
  ledger.PrintHeader (outf);
  outf.close ();
}

void
test_fstream ()
{
  // fstream
  const char *filename = "test_fstream.fsdat";
  std::fstream stream;

  // NOTE the openmode!!!
  stream.open (filename, ios::in | ios::out | ios::binary | ios::trunc);
  if (!stream.good ())
    {
      ERROR ("Open file {} failed", filename);
    }
  const char *data = "hello";
  stream.write (data, sizeof (data));

  stream.seekp (0);
  char *dataOut = new char[5];
  stream.read (dataOut, 5);
  cout << "dataOut=" << dataOut << endl;

  stream.close ();
}

int
main (int argc, char const *argv[])
{
  // test_ledger();

  test_fstream ();

  return 0;
}
