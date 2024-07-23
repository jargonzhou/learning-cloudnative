# Pthreads example in The Art of Multiprocessor Programming(TAMP)

```shell
$ make

# clean
$ make clean
rm -f threadid threads queue ptqueue
```

- [threadid.c](./threadid.c): Illustrates thread-local storage with Pthreads

```shell
$ ./threadid
Hello from thread 0
Hello from thread 1
Hello from thread 2
Hello from thread 3
Hello from thread 4
Hello from thread 5
Hello from thread 6
Hello from thread 7
```

- [threads.c](./threads.c): Illustrates Pthread creation and joining

```shell
$ ./threads
Hello from thread 0
Hello from thread 1
Hello from thread 2
Hello from thread 5
Hello from thread 4
Hello from thread 3
Hello from thread 6
Hello from thread 7
```

- [queue.c](./queue.c): Simple bounded queue using Pthreads

```shell
$ ./queue
queue ckecked out
```

- [ptqueue.c](./ptqueue.c): Simple bounded queue using Pthreads

```shell
$ ./ptqueue
queue ckecked out
```