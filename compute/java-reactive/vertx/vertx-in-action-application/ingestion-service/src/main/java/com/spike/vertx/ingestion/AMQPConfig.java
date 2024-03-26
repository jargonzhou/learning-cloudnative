package com.spike.vertx.ingestion;

import com.spike.vertx.commons.Constants;
import io.vertx.amqp.AmqpClientOptions;
import io.vertx.amqp.AmqpReceiverOptions;

public class AMQPConfig {
    public static AmqpClientOptions clientOptions() {
        return new AmqpClientOptions()
                .setHost(Constants.IngestionService.AMQP_HOST)
                .setPort(Constants.IngestionService.AMQP_PORT)
                .setUsername(Constants.IngestionService.AMQP_USERNAME)
                .setPassword(Constants.IngestionService.AMQP_PASSWORD);
    }

    public static AmqpReceiverOptions receiverOptions() {
        return new AmqpReceiverOptions()
                .setAutoAcknowledgement(false)
                .setDurable(true);
    }
}
