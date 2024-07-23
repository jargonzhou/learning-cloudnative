#!/usr/bin/env python3
# -*-coding: UTF-8 -*-

import os
import threading


def hello_from_thread():
    print(f'Hello from thread {threading.current_thread().name}')


if __name__ == "__main__":
    print(f'Current process id: {os.getpid()}')

    hello_thread = threading.Thread(target=hello_from_thread)
    hello_thread.start()

    total_threads = threading.active_count()
    thread_name = threading.current_thread().name
    print(
        f'Total threads: {total_threads}, current thread name: {thread_name}')

    hello_thread.join()
