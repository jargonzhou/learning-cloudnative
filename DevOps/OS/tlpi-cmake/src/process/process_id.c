// 进程ID和父进程ID

#include <unistd.h>
#include <stdio.h>

int
main (int argc, char **argv)
{
  pid_t pid = getpid ();
  pid_t ppid = getppid ();
  printf ("process id: %d\n", pid);
  printf ("parent process id: %d\n", ppid);

  return 0;
}
