package com.spike.dataengineering.redis;

import com.redis.testcontainers.RedisContainer;
import com.spike.dataengineering.redis.support.ExampleRedisInAction;
import com.spike.dataengineering.redis.support.Worker;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.utility.DockerImageName;

import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * More: <a href="https://github.com/redisson/redisson">Redisson - Valkey & Redis Java client. Real-Time Data Platform.</a>
 */
public class RedissonTest {
    private static final Logger LOG = LoggerFactory.getLogger(RedissonTest.class);
    private static RedisContainer redisContainer;

    @BeforeAll
    public static void beforeAll() {
        redisContainer = new RedisContainer(DockerImageName.parse("redis:7"));
        redisContainer.start();
    }

    @Test
    public void testLock() {
        // 创建客户端
        Config config = new Config();
        config.useSingleServer().setAddress(redisContainer.getRedisURI());
        final RedissonClient redissonClient = Redisson.create(config);

        final int lockCount = 3;
        final AtomicInteger result = new AtomicInteger(0);

        final String lockName = "biz1:module1:lock";

        Worker worker1 = new Worker(redissonClient, lockName, lockCount, result);
        Worker worker2 = new Worker(redissonClient, lockName, lockCount, result);

        Thread thread1 = new Thread(worker1, "Worker1");
        Thread thread2 = new Thread(worker2, "Worker2");
        thread1.start();
        thread2.start();

        try {
            thread1.join();
        } catch (InterruptedException e) {
            LOG.error("", e);
        }
        try {
            thread2.join();
        } catch (InterruptedException e) {
            LOG.error("", e);
        }

        Assertions.assertThat(result).hasValue(lockCount * 2);
    }


    /**
     * <pre>
     * see "Hello Redis" in "Redis in Action".
     *
     * articleId: STRING, AtomicLong
     * article: HASH, Article
     * vote: SET, "user:xxx"
     * score: ZSET, "article:xxx"
     * time: ZSET, "article:xxx"
     * </pre>
     */
    @Test
    public void testDataStructure() {
        // 创建客户端
        Config config = new Config();
        config.useSingleServer().setAddress(redisContainer.getRedisURI());
        final RedissonClient redissonClient = Redisson.create(config);

        ExampleRedisInAction exampleHelloRedis = new ExampleRedisInAction();

        String user1 = "Alice";
        String user2 = "Bob";

        ExampleRedisInAction.Article article1 = new ExampleRedisInAction.Article();
        article1.title = "article1 title";
        article1.link = "article1 link";
        article1.poster = user1;
        article1.time = new Date();

        exampleHelloRedis.postArticle(redissonClient, article1);
        List<ExampleRedisInAction.Article> articles = exampleHelloRedis.queryArticle(redissonClient, 1);
        for (ExampleRedisInAction.Article article : articles) {
            System.err.println(article);
        }

        exampleHelloRedis.voteArticle(redissonClient, user2, 1L);

        articles = exampleHelloRedis.queryArticle(redissonClient, 1);
        for (ExampleRedisInAction.Article article : articles) {
            System.err.println(article);
            Assertions.assertThat(article.votes).isEqualTo(2);
        }
    }


    @AfterAll
    public static void afterAll() {
        redisContainer.close();
    }
}
