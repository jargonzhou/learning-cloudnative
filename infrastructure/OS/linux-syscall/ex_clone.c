// An example from 深入剖析Kubernetes.
#define _GNU_SOURCE
#include <sched.h>
#include <signal.h>
#include <stdio.h>
#include <sys/mount.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <unistd.h>

#define STACK_SIZE (1024 * 1024)

static char container_stack[STACK_SIZE];

// char *const container_args[] = { "/bin/bash", NULL };
char *const container_args[] = { "/bin/bash", "-c", "ls /tmp/test.txt", NULL };

int
container_main (void *arg)
{
  printf ("container - inside the container!\n");
  // 挂载/tmp目录
  // mount ("none", "/tmp", "tmpfs", 0, "");

  // execc
  execv (container_args[0], container_args);
  printf ("something is wrong!\n");
  return 1;
}

int
main (int argc, char const *argv[])
{
  // clone: Mount namespace
  int container_pid = clone (container_main, container_stack + STACK_SIZE,
                             CLONE_NEWNS | SIGCHLD, NULL);
  if (container_pid == -1)
    {
      printf ("Parent - start a container failed!\n");
      return 1;
    }
  printf ("Parent - start a container [%d]!\n", container_pid);

  waitpid (container_pid, NULL, 0);
  printf ("Parent - container stopped!\n");
  return 0;
}
