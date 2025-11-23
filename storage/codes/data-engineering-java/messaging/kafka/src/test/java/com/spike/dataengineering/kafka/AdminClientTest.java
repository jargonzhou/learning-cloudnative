package com.spike.dataengineering.kafka;

import org.apache.kafka.clients.admin.*;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.*;
import org.apache.kafka.common.config.ConfigResource;
import org.apache.kafka.common.config.TopicConfig;
import org.apache.kafka.common.errors.UnknownTopicOrPartitionException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @see AdminClient
 * @see AdminClientConfig
 * @see KafkaFuture
 */
public class AdminClientTest {
    private static final Logger LOG = LoggerFactory.getLogger(AdminClientTest.class);


    private static AdminClient adminClient;
    private static final String TEST_TOPIC_NAME = "sample-new";
    private static final int NUM_PARTITIONS = 3;
    private static final short REPLICATION_FACTOR = 1;

    @BeforeAll
    public static void beforeAll() {
        Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(AdminClientConfig.REQUEST_TIMEOUT_MS_CONFIG, 120 * 1000); // default: 120s
        adminClient = AdminClient.create(props);


        forceCreateTopic(TEST_TOPIC_NAME);
    }

    @AfterAll
    public static void afterAll() {
        adminClient.close(Duration.ofSeconds(30));
    }

    //
    // topic management
    //

    /**
     * @see ListTopicsResult
     */
    @Test
    public void listTopics() throws ExecutionException, InterruptedException {
        ListTopicsResult topics = adminClient.listTopics();
        LOG.info("Topics: {}", topics.names().get());
    }

    /**
     * @see DescribeTopicsResult
     * @see TopicDescription
     * @see TopicPartitionInfo
     * @see CreateTopicsResult
     */
    @Test
    public void describeTopics() {
        final String topicName = TEST_TOPIC_NAME;
        DescribeTopicsResult topicsResult = adminClient.describeTopics(List.of(topicName));
        try {
            // name, topicId, partitions[partition, leader, [replicas], [isr]]
            TopicDescription topicDescription = topicsResult.topicNameValues().get(topicName).get();
            LOG.info("Topic description: {}", topicDescription);
        } catch (ExecutionException | InterruptedException e) {
            if (!(e.getCause() instanceof UnknownTopicOrPartitionException)) {
                LOG.error("FAILED", e);
            } else {
                // create topic
                CreateTopicsResult newTopic = adminClient.createTopics(List.of(
                        new NewTopic(topicName, NUM_PARTITIONS, REPLICATION_FACTOR)));
                try {
                    int actualPartitionNumber = newTopic.numPartitions(topicName).get();
                    if (actualPartitionNumber != NUM_PARTITIONS) {
                        LOG.warn("Wrong number of partitions");
                    }
                } catch (InterruptedException | ExecutionException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    private static void forceCreateTopic(String topicName) {
        DescribeTopicsResult topicsResult = adminClient.describeTopics(List.of(topicName));
        try {
            TopicDescription topicDescription = topicsResult.topicNameValues().get(topicName).get();
            LOG.info("Topic description: {}", topicDescription);
        } catch (ExecutionException | InterruptedException e) {
            if (!(e.getCause() instanceof UnknownTopicOrPartitionException)) {
                LOG.error("FAILED", e);
            } else {
                // create topic
                CreateTopicsResult newTopic = adminClient.createTopics(List.of(
                        new NewTopic(topicName, NUM_PARTITIONS, REPLICATION_FACTOR)));
                try {
                    int actualPartitionNumber = newTopic.numPartitions(topicName).get();
                    if (actualPartitionNumber != NUM_PARTITIONS) {
                        LOG.warn("Wrong number of partitions");
                    }
                } catch (InterruptedException | ExecutionException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    @Test
    public void deleteTopic() {
        final String topicName = TEST_TOPIC_NAME;
        DescribeTopicsResult topicDescription = adminClient.describeTopics(List.of(topicName));

        try {
            adminClient.deleteTopics(List.of(topicName)).all().get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        try {
            TopicDescription td = topicDescription.topicNameValues().get(topicName).get();
            LOG.warn("Topic still exist: {}", td);
        } catch (InterruptedException | ExecutionException e) {
            LOG.info("Topic deleted");
        }
    }

    //
    // configuration management
    // broker, broker logger, topic
    // kafka-config.sh
    //

    /**
     * @see ConfigResource
     * @see DescribeConfigsResult
     * @see TopicConfig
     * @see AlterConfigOp
     */
    @Test
    public void configResource() throws ExecutionException, InterruptedException {
        // topic
        final String topicName = "sample-new";
        ConfigResource resource = new ConfigResource(ConfigResource.Type.TOPIC, topicName);
        DescribeConfigsResult configsResult = adminClient.describeConfigs(List.of(resource));
        Config config = configsResult.all().get().get(resource);
        LOG.info("{}", config);

        // check whether compacted
        ConfigEntry compaction = new ConfigEntry(TopicConfig.CLEANUP_POLICY_CONFIG, TopicConfig.CLEANUP_POLICY_COMPACT);
        if (!config.entries().contains(compaction)) {
            // set to compact cleanup policy
            List<AlterConfigOp> configOps = new ArrayList<>();
            configOps.add(new AlterConfigOp(compaction, AlterConfigOp.OpType.SET));
            Map<ConfigResource, Collection<AlterConfigOp>> alterConf = new HashMap<>();
            alterConf.put(resource, configOps);
            adminClient.incrementalAlterConfigs(alterConf).all().get();
        } else {
            LOG.info("Topic {} is compacted topic", topicName);
        }
    }


    //
    // consumer group management
    //

    /**
     * @see ConsumerGroupListing
     * @see ConsumerGroupDescription
     */
    @Test
    public void listConsumerGroups() throws ExecutionException, InterruptedException {
        Collection<ConsumerGroupListing> consumerGroups = adminClient.listConsumerGroups()
                // all()
                // errors()
                .valid()
                .get();
        LOG.info("Consumer groups: {}", consumerGroups);
        if (Objects.nonNull(consumerGroups) && !consumerGroups.isEmpty()) {
            final List<String> groupIds = consumerGroups.stream()
                    .map(ConsumerGroupListing::groupId).toList();

            DescribeConsumerGroupsResult groupDescription = adminClient.describeConsumerGroups(groupIds);
            groupDescription.describedGroups().forEach((groupId, f) -> {
                // group members, partition assigned, partition algorithm, group coordinator
                try {
                    LOG.info("Consumer group [{}]: {}", groupId, f.get());
                } catch (InterruptedException | ExecutionException e) {
                    LOG.error("Got consumer group [{}] failed", groupId, e);
                }

                // offset
                try {
                    Map<TopicPartition, OffsetAndMetadata> offsets = adminClient.listConsumerGroupOffsets(groupId)
                            .partitionsToOffsetAndMetadata(groupId).get();
                    // latest offset
                    Map<TopicPartition, OffsetSpec> latestOffsetRequest = new HashMap<>(offsets.keySet().stream()
                            .collect(Collectors.toMap(Function.identity(), t -> OffsetSpec.latest()))); // latest
                    Map<TopicPartition, ListOffsetsResult.ListOffsetsResultInfo> latestOffsets =
                            adminClient.listOffsets(latestOffsetRequest).all().get();
                    for (TopicPartition tp : offsets.keySet()) {
                        long committedOffset = latestOffsets.get(tp).offset();
                        long latestOffset = offsets.get(tp).offset();
                        LOG.info("Consumer group [{}] has committed offset {}/{} to topic {} partition {}",
                                groupId, committedOffset, latestOffset, tp.topic(), tp.partition());
                    }
                } catch (InterruptedException | ExecutionException e) {
                    LOG.error("Got consumer group [{}] offsets failed", groupId, e);
                }
            });
        }
    }

    // delete group
    // remove member
    // delete committed offset
    // modify offset
    @Test
    public void modifyConsumerGroup() throws ExecutionException, InterruptedException {
        // when read offset:
        // a consumer is assigned a new partition or on startup

        // when modify offset:
        // the consumer group is NOT active

        // case: reset consumer group to the earliest offset
        final String consumerGroupId = "app-cg"; // TODO: add test case

        Map<TopicPartition, OffsetAndMetadata> offsets = adminClient.listConsumerGroupOffsets(consumerGroupId)
                .partitionsToOffsetAndMetadata(consumerGroupId).get();

        Map<TopicPartition, OffsetSpec> earliestOffsetRequest = new HashMap<>(offsets.keySet().stream()
                .collect(Collectors.toMap(Function.identity(), t -> OffsetSpec.earliest()))); // earliest
        Map<TopicPartition, ListOffsetsResult.ListOffsetsResultInfo> earliestOffsets =
                adminClient.listOffsets(earliestOffsetRequest).all().get();

        Map<TopicPartition, OffsetAndMetadata> resetOffsets = new HashMap<>();
        for (TopicPartition tp : earliestOffsets.keySet()) {
            resetOffsets.put(tp, new OffsetAndMetadata(earliestOffsets.get(tp).offset()));
        }
        // Alters offsets for the specified group. In order to succeed, the group must be empty.
        adminClient.alterConsumerGroupOffsets(consumerGroupId, resetOffsets).all().get();
    }

    //
    //  cluster metadata
    //
    @Test
    public void clusterMetadata() throws ExecutionException, InterruptedException {
        DescribeClusterResult cluster = adminClient.describeCluster();
        // cluster id
        LOG.info("Cluster: {}", cluster.clusterId().get());
        // brokers
        LOG.info("Nodes: {}", cluster.nodes().get());
        // controller
        LOG.info("Controller: {}", cluster.controller().get());
    }


    //
    // advanced operation:
    // add partitions to a topic
    // delete record from a topic
    // leader election
    // reassigning replicas
    //

    /**
     * @see NewPartitions
     */
    @Test
    public void addPartitionToTopic() throws ExecutionException, InterruptedException {
        final String topicName = TEST_TOPIC_NAME;
        Map<String, NewPartitions> newPartitions = new HashMap<>();
        newPartitions.put(topicName, NewPartitions.increaseTo(NUM_PARTITIONS + 1));
        adminClient.createPartitions(newPartitions).all().get();
    }

    @Test
    public void deleteTopicRecord() throws ExecutionException, InterruptedException {
        // delete records older than specific timestamp
        final String topicName = TEST_TOPIC_NAME;
        final long timestamp = new Date().getTime() - 3 * 24 * 60 * 60 * 1000; // 3 days ago
        TopicDescription topicDescription = adminClient.describeTopics(List.of(topicName))
                .topicNameValues().get(topicName).get();
        List<TopicPartitionInfo> partitions = topicDescription.partitions();
        if (Objects.nonNull(partitions) && !partitions.isEmpty()) {
            // timestamp => offset
            Map<TopicPartition, OffsetSpec> olderOffsetRequest = new HashMap<>(partitions.stream()
                    .collect(Collectors.toMap(p -> new TopicPartition(topicName, p.partition()),
                            // Used to retrieve the earliest offset whose timestamp is
                            // greater than or equal to the given timestamp in the corresponding partition
                            p -> OffsetSpec.forTimestamp(timestamp)))); // for timestamp
            Map<TopicPartition, ListOffsetsResult.ListOffsetsResultInfo> olderOffsets =
                    adminClient.listOffsets(olderOffsetRequest).all().get();
            // delete
            Map<TopicPartition, RecordsToDelete> recordsToDelete = new HashMap<>();
            for (TopicPartition tp : olderOffsets.keySet()) {
                recordsToDelete.put(tp, RecordsToDelete.beforeOffset(olderOffsets.get(tp).offset())); // before offset
            }
            adminClient.deleteRecords(recordsToDelete).all().get();
        }
    }

    /**
     * @see ElectionType
     */
    @Test
    public void leaderElection() throws ExecutionException, InterruptedException {
        Set<TopicPartition> electableTopics = new HashSet<>();
        electableTopics.add(new TopicPartition(TEST_TOPIC_NAME, 0));

        // electing the preferred leader
        adminClient.electLeaders(ElectionType.PREFERRED, electableTopics).all().get();
    }

    /**
     * @see Node#id()
     */
    @Test
    public void reassignReplica() throws ExecutionException, InterruptedException {
        // case: add more broker 0 => 0, 1
        // broker 0: 4 partitions, each one 1 replica
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
        TopicDescription topicDescription = adminClient.describeTopics(List.of(TEST_TOPIC_NAME))
                .topicNameValues().get(TEST_TOPIC_NAME).get();
        LOG.info("Topic {}: {}", TEST_TOPIC_NAME, topicDescription);
    }

}
