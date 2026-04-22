#include "buf_file.h"
#include "idx_text.h"
#include "recording.h"

#include <ostream>

using namespace std;

//! read and unpack the recording, return 1 if succeeds
int
retrieveRecording (Recording &recording, const char *key, TextIndex &index,
                   BufferFile &dataFile)
{
  int result;
  int recaddr;

  recaddr = index.Search (key);
  cout << "Retrieve " << key << " at recaddr " << recaddr << endl;
  result = dataFile.Read (recaddr);
  cout << "read result: " << result << endl;
  if (result == -1)
    return false;
  result = recording.Unpack (dataFile.GetBuffer ());

  return result;
}

//! make an index from a recording file
int
indexRecordingFile (const char *fileName, TextIndex &index)
{
  Recording rec;
  int recaddr, result;

  DelimFieldBuffer::SetDefaultDelim ('|');
  DelimFieldBuffer buffer;
  BufferFile dataFile (buffer);
  result = dataFile.Open (fileName, ios::in);
  if (!result)
    {
      cout << "Unable to open file " << fileName << endl;
      return 0;
    }

  while (1)
    {
      recaddr = dataFile.Read (); // read next record
      if (recaddr < 0)
        break;
      rec.Unpack (buffer);
      index.Insert (rec.Key (), recaddr);
      cout << recaddr << "\t" << rec << endl;
    }

  index.Print (cout);
  result = retrieveRecording (rec, "LON2312", index, dataFile);
  cout << "Found record: " << rec << endl;

  return result;
}

int
main (int argc, char const *argv[])
{
  TextIndex index (10);
  indexRecordingFile ("record.fsdat", index);

  // store index to file
  TextIndexBuffer indexBuffer (12, 10);
  BufferFile indexFile (indexBuffer);
  indexBuffer.Pack (index);
  indexFile.Create ("record.fsidx", ios::out);
  indexFile.Write ();

  return 0;
}
