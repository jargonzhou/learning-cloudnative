#!/usr/bin/env python3
# -*-coding: UTF-8 -*-
import asyncio
import time
from util import delay


async def main() -> None:
    sleep3 = asyncio.create_task(delay(3))
    print(type(sleep3))
    result = await sleep3
    print(result)


async def tasks() -> None:
    sleep1 = asyncio.create_task(delay(2))
    sleep2 = asyncio.create_task(delay(2))
    sleep3 = asyncio.create_task(delay(2))

    await sleep1
    await sleep2
    await sleep3


async def hello_every_second() -> None:
    for i in range(2):
        await asyncio.sleep(1)
        print('I\'m running other code while waiting')

async def tasks2() -> None:
    first_delay = asyncio.create_task(delay(3))
    second_delay = asyncio.create_task(delay(3))
    await hello_every_second()
    await first_delay
    await second_delay

if __name__ == "__main__":
    # asyncio.run(main=main())

    # time_start = time.time()
    # asyncio.run(main=tasks())
    # time_end = time.time()
    # print(f'Completed in {time_end-time_start:.4f} seconds')

    time_start = time.time()
    asyncio.run(main=tasks2())
    time_end = time.time()
    print(f'Completed in {time_end-time_start:.4f} seconds')
