#include <gnu/libc-version.h>
#include <stdio.h>

int
main (int argc, char **argv)
{
  const char *version = gnu_get_libc_version ();
  printf ("GNU libc version: %s\n", version);
  return 0;
}

//$ gcc get_libc_version.c
//$ ls
//a.out  get_libc_version.c
//$ ./a.out
//GNU libc version: 2.31
//$ ldd a.out
//	linux-vdso.so.1 (0x00007ffdfecc9000)
//	libc.so.6 => /lib/x86_64-linux-gnu/libc.so.6 (0x00007febd7e4a000)
//	/lib64/ld-linux-x86-64.so.2 (0x00007febd804a000)
