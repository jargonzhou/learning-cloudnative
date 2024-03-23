import asyncio


async def delay(delay_seconds: int) -> int:
    print(f'sleeping for {delay_seconds} seconds')
    await asyncio.sleep(delay=delay_seconds)
    print(f'finished sleeping for {delay_seconds} seconds')
    return delay_seconds
