// 显示进程环境

#include "../lib/tlpi_hdr.h"

// 全局变量访问环境列表
extern char **environ;

int
main (int argc, char **argv)
{
  char **ep;

  for (ep = environ; *ep != NULL ; ep++)
    {
      puts (*ep);
    }

  exit (EXIT_SUCCESS);
}
