#include "buf.h"
#include "buf_file.h"
#include "recording.h"

using namespace std;

int
main (int argc, char const *argv[])
{
  int recaddr;
  DelimFieldBuffer::SetDefaultDelim ('|');
  DelimFieldBuffer buffer;
  BufferFile recordingFile (buffer);
  recordingFile.Create ("record.fsdat", ios::out);

  // prepare data
  Recording *R[10];

  R[0] = new Recording ("LON", "2312", "Romeo and Juliet", "Prokofiev",
                        "Maazel");
  R[1] = new Recording ("RCA", "2626", "Quarter in C Sharp Minor", "Beethoven",
                        "Julliard");
  R[2] = new Recording ("WAR", "23699", "Touchstone", "Corea", "Corea");
  R[3] = new Recording ("ANG", "3795", "Symphony No. 9", "Beethoven",
                        "Giulini");
  R[4] = new Recording ("COL", "38358", "Nebraska", "Springsteen",
                        "Springsteen");
  R[5] = new Recording ("DG", "18807", "Symphony No. 9", "Beethoven",
                        "Karajan");
  R[6] = new Recording ("MER", "75016", "Coq d'or Suite", "Rimsky-Korsakov",
                        "Leinsdorf");
  R[7] = new Recording ("COL", "31809", "Symphony No. 9", "Dvorak",
                        "Bernstein");
  R[8] = new Recording ("DG", "139201", "Violin Concerto", "Beethoven",
                        "Ferras");
  R[9] = new Recording ("FF", "245", "Good News", "Sweet Honey in the Rock",
                        "Sweet Honey in the Rock");

  for (int i = 0; i < 10; i++)
    {
      R[i]->Pack (buffer);
      recaddr = recordingFile.Write ();
      cout << "Recording R[" << i << "] at recaddr " << recaddr << endl;
      delete R[i];
    }

  return 0;
}
