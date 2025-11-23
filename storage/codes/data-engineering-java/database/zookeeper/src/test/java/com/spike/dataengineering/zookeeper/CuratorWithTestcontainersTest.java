package com.spike.dataengineering.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryOneTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;

import java.nio.charset.StandardCharsets;

public class CuratorWithTestcontainersTest {
    // ref: https://github.com/testcontainers/testcontainers-java/blob/main/examples/zookeeper/src/test/java/com/example/ZookeeperContainerTest.java
    @Test
    public void example() throws Exception {
        final int ZK_PORT = 2181;
        try (GenericContainer<?> zookeeper = new GenericContainer<>("zookeeper:3.9.3")
                .withExposedPorts(ZK_PORT);) {

            zookeeper.start();

            String connectionString = zookeeper.getHost() + ":" + zookeeper.getMappedPort(ZK_PORT);

            CuratorFramework curatorFramework = CuratorFrameworkFactory
                    .builder()
                    .connectString(connectionString)
                    .retryPolicy(new RetryOneTime(100))
                    .build();
            curatorFramework.start();

            final String path = "/message/zk-tc";
            final String data = "Running ZK with Testcontainers";
            // write
            curatorFramework.create().creatingParentsIfNeeded().forPath(path, data.getBytes()); // mkdir -p
            // read
            byte[] bytes = curatorFramework.getData().forPath(path);
            String resultData = new String(bytes, StandardCharsets.UTF_8);
            System.err.println(resultData);
            Assertions.assertThat(resultData).isEqualTo(data);

            // cleanup
            curatorFramework.close();
        }
    }
}
