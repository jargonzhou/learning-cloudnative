package com.spike.dataengineering.kafka;

import org.apache.kafka.clients.admin.*;
import org.apache.kafka.common.Node;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.config.ConfigResource;
import org.apache.kafka.common.config.TopicConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ExecutionException;


/**
 * @see MockAdminClient
 */
public class MockAdminClientTest {
    private static final Logger LOG = LoggerFactory.getLogger(MockAdminClientTest.class);

    private static MockAdminClient adminClient;
    private static final String TEST_TOPIC_NAME = "sample-new";

    @BeforeAll
    public static void beforeAll() {
        Node broker1 = new Node(0, "localhost", 9092);
        Node broker2 = new Node(1, "localhost", 9093);
        Node broker3 = new Node(2, "localhost", 9094);
        adminClient = MockAdminClient.create()
                .brokers(List.of(broker1, broker2, broker3))
                .controller(0)
                .usingRaftController(true)
                .build();
    }

    @Test
    public void testCreateTopic() throws ExecutionException, InterruptedException {
        TopicCreator tc = new TopicCreator(adminClient);
        final String topicName = "test.is.a.test.topic";
        tc.createTopic(topicName);

        TopicDescription td = adminClient.describeTopics(List.of(topicName))
                .topicNameValues().get(topicName).get();
        LOG.info("Topic {}: {}", topicName, td);
    }

    public static class TopicCreator {
        private static final Logger LOG = LoggerFactory.getLogger(TopicCreator.class);

        private final AdminClient adminClient;

        public TopicCreator(AdminClient adminClient) {
            this.adminClient = adminClient;
        }

        public void createTopic(String topicName) throws ExecutionException, InterruptedException {
            List<NewTopic> topics = new ArrayList<>();
            topics.add(new NewTopic(topicName, 1, (short) 3));
            if (topicName.toLowerCase().startsWith("test")) {
                CreateTopicsResult createResult = adminClient.createTopics(topics);
                LOG.info("Create topic {}: id={}, partitions={}, repliacs={}, config={}",
                        topicName, createResult.topicId(topicName).get(),
                        createResult.numPartitions(topicName).get(),
                        createResult.replicationFactor(topicName).get(),
                        createResult.config(topicName).get());

                ConfigResource configResource = new ConfigResource(ConfigResource.Type.TOPIC, topicName);
                ConfigEntry compaction = new ConfigEntry(TopicConfig.CLEANUP_POLICY_CONFIG,
                        TopicConfig.CLEANUP_POLICY_COMPACT);
                List<AlterConfigOp> configOps = new ArrayList<>();
                configOps.add(new AlterConfigOp(compaction, AlterConfigOp.OpType.SET));
                Map<ConfigResource, Collection<AlterConfigOp>> alterConf = new HashMap<>();
                alterConf.put(configResource, configOps);
                adminClient.incrementalAlterConfigs(alterConf).all().get();
            }
        }
    }

    @Test
    public void reassignReplica() throws ExecutionException, InterruptedException {
        // case: add more broker 0 => 0, 1
        // broker 0: 4 partitions, each one 1 replica

        // setup
        adminClient.createTopics(List.of(new NewTopic(TEST_TOPIC_NAME, 4, (short) 1))).all().get();
        LOG.info("Topic {}: {}", TEST_TOPIC_NAME, adminClient.describeTopics(List.of(TEST_TOPIC_NAME))
                .topicNameValues().get(TEST_TOPIC_NAME).get());

        Map<TopicPartition, Optional<NewPartitionReassignment>> reassignment = new HashMap<>();
        reassignment.put(new TopicPartition(TEST_TOPIC_NAME, 0),
                // add 1 replica to broker 1, leader unchanged
                Optional.of(new NewPartitionReassignment(List.of(0, 1))));
        reassignment.put(new TopicPartition(TEST_TOPIC_NAME, 1),
                // move the replica to broker 1, the leader
                Optional.of(new NewPartitionReassignment(List.of(1))));
        reassignment.put(new TopicPartition(TEST_TOPIC_NAME, 2),
                // add 1 replica to broker 1, the leader
                Optional.of(new NewPartitionReassignment(List.of(1, 0))));
        reassignment.put(new TopicPartition(TEST_TOPIC_NAME, 3),
                // no reassignment, if there was then cancel it
                Optional.empty());

        adminClient.alterPartitionReassignments(reassignment).all().get();

        // current reassigning
        LOG.info("Current reassigning: {}", adminClient.listPartitionReassignments().reassignments().get());
        LOG.info("Topic {}: {}", TEST_TOPIC_NAME, adminClient.describeTopics(List.of(TEST_TOPIC_NAME))
                .topicNameValues().get(TEST_TOPIC_NAME).get());
    }
}
