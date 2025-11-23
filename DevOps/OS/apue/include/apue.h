#ifndef INCLUDE_APUE
#define INCLUDE_APUE

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

#define MAXLINE 4096 /* max line length */

// error.c
void err_msg(const char *, ...);
void err_dump(const char *, ...) __attribute__((noreturn));
void err_quit(const char *, ...) __attribute__((noreturn));
void err_cont(int, const char *, ...);
void err_exit(int, const char *, ...) __attribute__((noreturn));
void err_ret(const char *, ...);
void err_sys(const char *, ...) __attribute__((noreturn));

// print.c
void pr_exit(int status);
void pr_mask(const char *);

// fd.c
/*
 * Set FD_CLOEXEC.
 */
int set_cloexec(int fd);

/*
 * Lock file.
 */
int lockfile(int fd);

#endif /* INCLUDE_APUE */