#include "buf.h"
#include "buf_file.h"
#include "log.h"
#include "recording.h"
#include "text_idx_file.h"
#include <iostream>

using namespace std;

int
prepareData (TextIndexedFile<Recording> &indexFile)
{
  int recaddr;
  Recording R[10]{
    Recording ("LON", "2312", "Romeo and Juliet", "Prokofiev", "Maazel"),
    Recording ("RCA", "2626", "Quarter in C Sharp Minor", "Beethoven",
               "Julliard"),
    Recording ("WAR", "23699", "Touchstone", "Corea", "Corea"),
    Recording ("ANG", "3795", "Symphony No. 9", "Beethoven", "Giulini"),
    Recording ("COL", "38358", "Nebraska", "Springsteen", "Springsteen"),
    Recording ("DG", "18807", "Symphony No. 9", "Beethoven", "Karajan"),
    Recording ("MER", "75016", "Coq d'or Suite", "Rimsky-Korsakov",
               "Leinsdorf"),
    Recording ("COL", "31809", "Symphony No. 9", "Dvorak", "Bernstein"),
    Recording ("DG", "139201", "Violin Concerto", "Beethoven", "Ferras"),
    Recording ("FF", "245", "Good News", "Sweet Honey in the Rock",
               "Sweet Honey in the Rock")
  };

  for (int i = 0; i < 10; i++)
    {
      recaddr = indexFile.Append (R[i]);
      cout << "Recording R[" << i << "] at recaddr " << recaddr << endl;
    }

  return 0;
}

//! make an indexed file from a recording file
int
readIndexRecordingFile (const char *filename,
                        TextIndexedFile<Recording> &indexFile)
{
  Recording fileRec;
  Recording indexRec;
  int recaddr, result;

  DelimFieldBuffer buffer;
  BufferFile recFile (buffer);
  result = recFile.Open (filename, ios::in);
  if (!result)
    {
      ERROR ("Unable to open file {}", filename);
      return 0;
    }

  while (true)
    {
      recaddr = recFile.Read ();
      if (recaddr < 0)
        break;
      fileRec.Unpack (buffer);
      cout << "\nLooking for record with key: " << fileRec.Key () << endl;
      result = indexFile.Read (fileRec.Key (), indexRec);
      if (result < 0)
        cout << "record not found" << endl;
      else
        indexRec.Print (cout);
    }

  return true;
}

int
main (int argc, char const *argv[])
{
  init_log ();

  int result;
  TextIndex recIndex (10);
  DelimFieldBuffer dataBuffer;
  TextIndexedFile<Recording> indexFile (dataBuffer, 12, 10);
  result = indexFile.Create ("record1", ios::out);
  if (!result)
    {
      ERROR ("Unable to create file: {}", result);
      return 0;
    }
  prepareData (indexFile);

  indexFile.Close ();
  result = indexFile.Open ("record1");
  if (!result)
    {
      ERROR ("Unable to create index file: {}", result);
      return 0;
    }

  readIndexRecordingFile ("record1.fsdat", indexFile);
  indexFile.Close ();

  return 0;
}
