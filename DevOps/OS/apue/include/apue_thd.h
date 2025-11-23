#ifndef INCLUDE_APUE_THD
#define INCLUDE_APUE_THD

// thd.c
int makethread_detached(void *(*)(void *), void *);

void daemonize(const char *cmd);

int already_running(const char *lockfilepath);

#endif /* INCLUDE_APUE_THD */
