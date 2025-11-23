package com.spike.springcloud.stream;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spike.springcloud.stream.domain.OrderAcceptedMessage;
import com.spike.springcloud.stream.domain.OrderDispatchedMessage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;

import java.io.IOException;

@SpringBootTest
@Import({TestChannelBinderConfiguration.class}) // 测试用Channel Binder配置
public class StreamProcessorApplicationTest {

    @Autowired
    private InputDestination inputDestination; // 输入

    @Autowired
    private OutputDestination outputDestination; // 输出

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void contextLoads() throws IOException {
        final Long orderId = 1234L;

        Message<OrderAcceptedMessage> inputMessage = MessageBuilder
                .withPayload(new OrderAcceptedMessage(orderId))
                .build();
        Message<OrderDispatchedMessage> expectedOutputMessage = MessageBuilder
                .withPayload(new OrderDispatchedMessage(orderId))
                .build();

        inputDestination.send(inputMessage, "order-accepted");

        Assertions.assertThat(objectMapper.readValue(
                        outputDestination.receive(0L, "order-dispatched").getPayload(),
                        OrderDispatchedMessage.class))
                .isEqualTo(expectedOutputMessage.getPayload());
    }
}
