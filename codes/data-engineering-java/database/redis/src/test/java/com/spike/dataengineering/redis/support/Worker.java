package com.spike.dataengineering.redis.support;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.atomic.AtomicInteger;

public class Worker implements Runnable {
    private final RedissonClient redissonClient;
    private final String lockName;
    private final int lockCount;
    private final AtomicInteger resultCollector;

    public Worker(RedissonClient redissonClient, String lockName, int lockCount,
                  AtomicInteger resultCollector) {
        this.resultCollector = resultCollector;
        if (lockCount <= 0) {
            lockCount = 1;
        }
        this.redissonClient = redissonClient;
        this.lockName = lockName;
        this.lockCount = lockCount;
    }

    @Override
    public void run() {
        // 获取锁实例
        RLock lock = redissonClient.getLock(lockName);

        for (int i = 0; i < lockCount; i++) {
            try {
                System.err.println(Thread.currentThread().getName() + " try to got the lock!");
                lock.lock(); // 加锁
                System.err.println(Thread.currentThread().getName() + " got the lock!!!");
            } finally {
                lock.unlock(); // 解锁
                System.err.println(Thread.currentThread().getName() + " released the lock!");
            }
            resultCollector.incrementAndGet();
        }
    }

}