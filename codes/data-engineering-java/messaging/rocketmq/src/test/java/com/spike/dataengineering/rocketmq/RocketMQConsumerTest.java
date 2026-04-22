package com.spike.dataengineering.rocketmq;

import org.apache.rocketmq.client.apis.ClientConfiguration;
import org.apache.rocketmq.client.apis.ClientException;
import org.apache.rocketmq.client.apis.ClientServiceProvider;
import org.apache.rocketmq.client.apis.consumer.ConsumeResult;
import org.apache.rocketmq.client.apis.consumer.FilterExpression;
import org.apache.rocketmq.client.apis.consumer.FilterExpressionType;
import org.apache.rocketmq.client.apis.consumer.PushConsumer;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collections;

public class RocketMQConsumerTest {
    private static final Logger LOG = LoggerFactory.getLogger(RocketMQConsumerTest.class);

    @Test
    public void testConsume() throws ClientException, InterruptedException, IOException {
        final ClientServiceProvider provider = ClientServiceProvider.loadService();
        // Endpoint address, set to the Proxy address and port list, usually xxx:8080;xxx:8081
        String endpoints = "localhost:18081";
        ClientConfiguration clientConfiguration = ClientConfiguration.newBuilder()
                .setEndpoints(endpoints)
                .build();
        // Subscription message filtering rule, indicating subscription to all Tag messages.
        String tag = "*";
        FilterExpression filterExpression = new FilterExpression(tag, FilterExpressionType.TAG);
        // Specify the consumer group the consumer belongs to, Group needs to be created in advance.
        String consumerGroup = "YourConsumerGroup";
        // Specify which target Topic to subscribe to, Topic needs to be created in advance.
        String topic = "TestTopic";
        // Initialize PushConsumer
        PushConsumer pushConsumer = provider.newPushConsumerBuilder()
                .setClientConfiguration(clientConfiguration)
                // Set the consumer group.
                .setConsumerGroup(consumerGroup)
                // Set pre-bound subscription relationship.
                .setSubscriptionExpressions(Collections.singletonMap(topic, filterExpression))
                // Set the message listener.
                .setMessageListener(messageView -> {
                    // Handle messages and return the consumption result.
                    LOG.info("Consume message successfully, messageId={}", messageView.getMessageId());
                    return ConsumeResult.SUCCESS;
                })
                .build();

        Thread.sleep(3 * 1000);

        // If PushConsumer is no longer needed, this instance can be closed.
        pushConsumer.close();
    }
}
