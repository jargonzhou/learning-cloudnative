#!/usr/bin/env python3
# -*-coding: UTF-8 -*-
import asyncio

from util import delay


async def main_cancel() -> None:
    long_task = asyncio.create_task(delay(10))

    seconds_elapsed = 0
    while not long_task.done():
        print('Task not finished, checking again in 1 second')
        await delay(1)
        seconds_elapsed = seconds_elapsed + 1
        if seconds_elapsed == 5:
            long_task.cancel()  # cancel task

    try:
        await long_task
    except asyncio.CancelledError:
        print('Task was cancelled')


async def main_timeout() -> None:
    delay_task = asyncio.create_task(delay(2))
    try:
        result = await asyncio.wait_for(delay_task, timeout=1)  # timeout
        print(result)
    except asyncio.TimeoutError:
        print(
            f'Got timeout error, was task cancelled? {delay_task.cancelled()}')


async def main_timeout_shield() -> None:
    delay_task = asyncio.create_task(delay(10))
    try:
        # shield
        result = await asyncio.wait_for(asyncio.shield(delay_task), timeout=5)
        print(result)
    except asyncio.TimeoutError:
        print('Task took longer than 5 seconds, it will finish soon!')
        result = await delay_task
        print(result)


if __name__ == "__main__":
    # asyncio.run(main=main_cancel())

    # asyncio.run(main=main_timeout())
    asyncio.run(main=main_timeout_shield())
