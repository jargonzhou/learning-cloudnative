#!/usr/bin/env python3
# -*-coding: UTF-8 -*-

# GIL: Global Interpreter Lock
import requests
import time
import threading

# CPU-bound


def print_fib(number: int) -> None:
    def fib(n: int) -> int:
        if n == 1:
            return 0
        elif n == 2:
            return 1
        else:
            return fib(n-1) + fib(n-2)

    print(f'fib({number}) = {fib(number)}')


def fibs_no_threading():
    print_fib(30)
    print_fib(31)


def fibs_with_threading():
    t1 = threading.Thread(target=print_fib, args=(30,))
    t2 = threading.Thread(target=print_fib, args=(31,))

    t1.start()
    t2.start()

    t1.join()
    t2.join()


# IO-bound


def read_example() -> None:
    response = requests.get('https://www.example.com')
    print(response.status_code)


if __name__ == "__main__":
    # CPU-bound
    # time_start = time.time()
    # fibs_no_threading()
    # time_end = time.time()
    # print(f'Completed in {time_end-time_start:.4f} seconds')

    # time_start = time.time()
    # fibs_with_threading()
    # time_end = time.time()
    # print(f'Completed in {time_end-time_start:.4f} seconds')

    time_start = time.time()
    read_example()
    read_example()
    time_end = time.time()
    print(f'Completed in {time_end-time_start:.4f} seconds')

    t1 = threading.Thread(target=read_example)
    t2 = threading.Thread(target=read_example)
    time_start = time.time()
    t1.start()
    t2.start()

    t1.join()
    t2.join()
    time_end = time.time()
    print(f'Completed in {time_end-time_start:.4f} seconds')
