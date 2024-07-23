#!/usr/bin/env python3
# -*-coding: UTF-8 -*-
from util import delay
import asyncio


async def add_one(number: int) -> int:
    return number + 1


async def hello_world_message() -> str:
    await delay(delay_seconds=1)
    return 'Hello World!'


async def main() -> None:
    message = await hello_world_message()
    add1 = await add_one(1)
    print(add1)
    print(message)

if __name__ == "__main__":
    asyncio.run(main=main())
