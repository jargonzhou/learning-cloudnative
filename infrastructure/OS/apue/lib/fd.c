#include "apue.h"
#include <fcntl.h>

int
set_cloexec (int fd)
{
  int val;

  if ((val = fcntl (fd, F_GETFD, 0)) < 0)
    return (-1);

  val |= FD_CLOEXEC; // enable close on exec

  return (fcntl (fd, F_SETFD, val));
}

int
lockfile (int fd)
{
  struct flock fl;

  fl.l_type = F_WRLCK;
  fl.l_start = 0;
  fl.l_whence = SEEK_SET;
  fl.l_len = 0;

  return (fcntl (fd, F_SETLK, &fl));
}