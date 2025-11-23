// 修改环境变量

#define _DEFAULT_SOURCE
#include <stdlib.h>
#include "../lib/tlpi_hdr.h"

// 全局变量访问环境列表
extern char **environ;

int
main (int argc, char **argv)
{
  int j;
  char **ep;

  //	clearenv();					// 清空环境变量

  for (j = 1; j < argc; j++)
    {
      if (putenv (argv[j]) != 0)			// 添加或修改
	{
	  errExit ("putenv: %s", argv[j]);
	}
    }

  if (setenv ("GREET", "Hello world", 1) == -1)		// 添加或修改
    {
      errExit ("setenv");
    }

  unsetenv ("BYE");					// 移除

  for (ep = environ; *ep != NULL ; ep++)
    {
      puts (*ep);
    }

  exit (EXIT_SUCCESS);

}
