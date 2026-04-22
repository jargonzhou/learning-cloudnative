// 进程内存布局

#include <stdio.h>
#include <stdlib.h>

char globBuffer[65536]; 	// 未初始化的数据段
int primes[] =
  { 2, 3, 5, 7 }; 		// 已初始化的数据段

static int
square (int x)			// 在square()栈帧中分配
{
  int result;			// 在square()栈帧中分配

  result = x * x;
  return result; 		// 返回值通过寄存器传递
}

static void
doCalc (int val)		// 在doCalc()栈帧中分配
{
  printf ("The square of %d is %d\n", val, square (val));

  if (val < 1000)
    {
      int t;			// 在doCalc()栈帧中分配

      t = val * val * val;
      printf ("The cube of %d is %d\n", val, t);
    }
}

int
main (int argc, char **argv)
{
  static int key = 9973;	// 已初始化的数据段
  static char mbuf[10240000];	// 未初始化的数据段

  char *p;			// 在main()栈帧中分配

  p = malloc (1024);		// 指向堆段中内存

  doCalc (key);

  exit (EXIT_SUCCESS);
}
