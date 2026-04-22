#include <sys/epoll.h> // only Linux

#include <errno.h>
#include <fcntl.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define MAX_BUF 1000
#define MAX_EVENTS 5

/**
 * REF: http://man7.org/linux/man-pages/man7/epoll.7.html
 *
 * sys/epoll.h
 *
 * epoll_create()
 * epoll_ctl()
 * epoll_wait()
 *
 *
 * Output:
 * (1) Terminal 1
 $ mkfifo p q
 $ ./a.out p q
 Opened "p" on fd 4
 Opened "q" on fd 5
 About to epoll_wait()
 Ready: 1
 fd=5; events: EPOLLIN
 read 4 bytes: qqq

 About to epoll_wait()
 Ready: 1
 fd=5; events: EPOLLHUP
 closing fd 5
 About to epoll_wait()
 Ready: 1
 fd=4; events: EPOLLIN
 read 4 bytes: ppp

 About to epoll_wait()
 Ready: 1
 fd=4; events: EPOLLHUP
 closing fd 4
 All fd closed; bye
 *
 * (2) Terminal 2:
 $ ls
 a.out  example_epoll.c  p  q
 $ cat > p
 ^Z
 [1]+  Stopped                 cat > p
 $ cat > q
 qqq
 $ fg %1
 cat > p
 ppp
 */

int
main (int argc, char **argv)
{

  int epfd, ready, fd, s, j, numOpenFds;
  struct epoll_event ev;
  struct epoll_event evlist[MAX_EVENTS];

  char buf[MAX_BUF];

  if (argc < 2 || strcmp (argv[1], "--help") == 0)
    {
      printf ("%s file...\n", argv[0]);
      exit (-1);
    }

  // epoll_create
  epfd = epoll_create (argc - 1);
  if (epfd == -1)
    {
      printf ("epoll_create failed\n");
      exit (-1);
    }

  // 打开命令行中文件, 并加入epoll示例的感兴趣列表中
  for (j = 1; j < argc; j++)
    {
      fd = open (argv[j], O_RDONLY);
      if (fd == -1)
        {
          printf ("open failed\n");
          exit (-1);
        }
      printf ("Opened \"%s\" on fd %d\n", argv[j], fd);

      // epoll_ctl
      ev.events = EPOLLIN;
      ev.data.fd = fd;
      if (epoll_ctl (epfd, EPOLL_CTL_ADD, fd, &ev) == -1)
        {
          printf ("epoll_ctl failed\n");
          exit (-1);
        }
    }

  numOpenFds = argc - 1;
  while (numOpenFds > 0)
    {
      // epoll_wait
      printf ("About to epoll_wait()\n");
      ready = epoll_wait (epfd, evlist, MAX_EVENTS, -1);
      if (ready == -1)
        {
          if (errno == EINTR)
            continue;
          else
            {
              printf ("epoll_wait failed\n");
              exit (-1);
            }
        }

      printf ("Ready: %d\n", ready);

      // 处理事件
      for (j = 0; j < ready; j++)
        {
          printf ("fd=%d; events: %s%s%s\n", evlist[j].data.fd,
                  (evlist[j].events & EPOLLIN) ? "EPOLLIN" : "",
                  (evlist[j].events & EPOLLHUP) ? "EPOLLHUP" : "",
                  (evlist[j].events & EPOLLERR) ? "EPOLLERR" : "");

          if (evlist[j].events & EPOLLIN)
            {
              s = read (evlist[j].data.fd, buf, MAX_BUF);
              if (s == -1)
                {
                  printf ("read failed\n");
                  exit (-1);
                }
              printf ("read %d bytes: %.*s\n", s, s, buf);
            }
          else if (evlist[j].events & (EPOLLHUP | EPOLLERR))
            {
              printf ("closing fd %d\n", evlist[j].data.fd);
              if (close (evlist[j].data.fd) == -1)
                {
                  printf ("close failed\n");
                  exit (-1);
                }
              numOpenFds--;
            }
        }
    }

  printf ("All fd closed; bye\n");
  return 0;
}
