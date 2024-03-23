#!/usr/bin/env python3
# -*-coding: UTF-8 -*-

import asyncio


async def my_coroutine() -> None:
    print('Hello world!')


def add_one(number: int) -> int:
    return number + 1


async def coroutine_add_one(number: int) -> int:
    return number + 1


async def main() -> None:
    add1 = await coroutine_add_one(1)
    add2 = await coroutine_add_one(2)
    print(add1)
    print(add2)

if __name__ == "__main__":
    function_result = add_one(1)
    coroutine_result = coroutine_add_one(1)
    print(f'F result: {function_result} of type {type(function_result)}')
    print(f'C result: {coroutine_result} of type {type(coroutine_result)}')

    result = asyncio.run(coroutine_add_one(1))
    print(result)

    asyncio.run(main=main())
