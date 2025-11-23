package com.spike.dataengineering.zookeeper;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListener;
import org.apache.curator.framework.recipes.locks.InterProcessSemaphoreMutex;
import org.apache.curator.framework.recipes.shared.SharedCount;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.x.async.AsyncCuratorFramework;
import org.apache.curator.x.async.modeled.JacksonModelSerializer;
import org.apache.curator.x.async.modeled.ModelSpec;
import org.apache.curator.x.async.modeled.ModeledFramework;
import org.apache.curator.x.async.modeled.ZPath;
import org.assertj.core.api.Assertions;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

// async usage: https://curator.apache.org/docs/async-details
// example: https://www.baeldung.com/apache-curator
public class CuratorTest {

    static final RetryPolicy retryPolicy = new RetryNTimes(3, 100);
    static final String connectString = "192.168.0.101:2181,192.168.0.101:2182,192.168.0.101:2183";

    private static CuratorFramework curatorFramework;
    private static AsyncCuratorFramework asyncCuratorFramework;

    @BeforeAll
    public static void beforeAll() {
        curatorFramework = CuratorFrameworkFactory.newClient(connectString, retryPolicy);
        curatorFramework.start();

        asyncCuratorFramework = AsyncCuratorFramework.wrap(curatorFramework);
    }

    @Disabled("spike test")
    @Test
    public void create() throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.newClient(connectString, retryPolicy);
        client.start();

        Assertions.assertThat(client.checkExists().forPath("/")).isNotNull();
    }

    @Disabled("spike test")
    @Test
    public void async() {
        CuratorFramework client = CuratorFrameworkFactory.newClient(connectString, retryPolicy);
        client.start();

        AsyncCuratorFramework asyncClient = AsyncCuratorFramework.wrap(client);
        AtomicBoolean exists = new AtomicBoolean(false);
        asyncClient.checkExists()
                .forPath("/")
                .thenAcceptAsync(s -> exists.set(Objects.nonNull(s)));

        Awaitility.await().untilTrue(exists);
        Assertions.assertThat(exists.get()).isTrue();
    }


    @Test
    public void configurationManagement() throws Exception {
        final String key = "/example/my_key";
        final String data = "my_value";
        curatorFramework.create().creatingParentsIfNeeded().forPath(key);
        asyncCuratorFramework.setData().forPath(key, data.getBytes());

        AtomicBoolean equals = new AtomicBoolean(false);
        asyncCuratorFramework.getData().forPath(key)
                .thenAccept(bytes -> equals.set(new String(bytes).equals(data)));
        Awaitility.await().untilTrue(equals);
        Assertions.assertThat(equals.get()).isTrue();

        // cleanup
        curatorFramework.delete().forPath(key);
    }

    @Test
    public void watchers() throws Exception {
        final String key = "/example/my_key";
        final String data = "my_value";
        curatorFramework.create().creatingParentsIfNeeded().forPath(key);

        List<String> changes = new ArrayList<>();
        asyncCuratorFramework.watched()
                .getData()
                .forPath(key)
                .event()
                .thenAccept(event -> {
                    System.err.println(event);
                    try {
                        changes.add(new String(curatorFramework.getData().forPath(event.getPath())));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });

        asyncCuratorFramework.setData()
                .forPath(key, data.getBytes());

        Awaitility.await().untilAsserted(() -> Assertions.assertThat(changes.size()).isEqualTo(1));

        // cleanup
        curatorFramework.delete().forPath(key);
    }

    // https://curator.apache.org/docs/modeled
    @Test
    public void stronglyTypedModels() {
        ModelSpec<HostConfig> modelSpec = ModelSpec.builder(
                        ZPath.parseWithIds("/config/dev"),
                        JacksonModelSerializer.build(HostConfig.class))
                .build();

        ModeledFramework<HostConfig> modeledFramework = ModeledFramework.wrap(asyncCuratorFramework, modelSpec);

        modeledFramework.set(new HostConfig("host-name", 8080));

        AtomicBoolean valued = new AtomicBoolean(false);
        modeledFramework.read()
                .whenComplete((value, e) -> {
                    if (e != null) {
                        Assertions.fail(e);
                    } else {
                        System.err.println(value);
                        Assertions.assertThat(value).isNotNull();
                        Assertions.assertThat(value.getHostname()).isEqualTo("host-name");
                        Assertions.assertThat(value.getPort()).isEqualTo(8080);
                        valued.set(true);
                    }
                });

        Awaitility.await().untilTrue(valued);
    }


    public static class HostConfig {
        private String hostname;
        private int port;

        public HostConfig() {
        }

        public HostConfig(String hostname, int port) {
            this.hostname = hostname;
            this.port = port;
        }

        public String getHostname() {
            return hostname;
        }

        public void setHostname(String hostname) {
            this.hostname = hostname;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        @Override
        public String toString() {
            return "HostConfig{" +
                    "hostname='" + hostname + '\'' +
                    ", port=" + port +
                    '}';
        }
    }

    //
    // recipes
    //

    @Test
    public void leaderElection() {
        AtomicBoolean leader = new AtomicBoolean(false);
        LeaderSelector leaderSelector = new LeaderSelector(curatorFramework, "/mutex/select/leader",
                new LeaderSelectorListener() {
                    @Override
                    public void takeLeadership(CuratorFramework curatorFramework) throws Exception {
                        System.err.println("takeLeadership");

                        Thread.sleep(3000); // mock delay
                        leader.set(true);
                    }

                    @Override
                    public void stateChanged(CuratorFramework client, ConnectionState newState) {
                        System.err.println("stateChanged: " + newState);
                    }
                });

        leaderSelector.start();

        Awaitility.await().untilTrue(leader);

        // cleanup
        leaderSelector.close();
    }

    @Test
    public void sharedLocks() {
        InterProcessSemaphoreMutex mutex = new InterProcessSemaphoreMutex(curatorFramework, "/mutex/process");

        try {
            mutex.acquire();
            System.err.println("acquired");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try {
            mutex.release();
            System.err.println("released");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void counter() {
        SharedCount count = new SharedCount(curatorFramework, "/counters/A", 0);
        try {
            count.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try {
            count.setCount(count.getCount() + 1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Assertions.assertThat(count.getCount()).isEqualTo(1);
    }

}
