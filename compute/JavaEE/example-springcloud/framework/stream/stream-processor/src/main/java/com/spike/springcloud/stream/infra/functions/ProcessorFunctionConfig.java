package com.spike.springcloud.stream.infra.functions;

import com.spike.springcloud.stream.domain.OrderAcceptedMessage;
import com.spike.springcloud.stream.domain.OrderDispatchedMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

import java.util.function.Function;

@Configuration
public class ProcessorFunctionConfig {
    private static final Logger PACK_LOG = LoggerFactory.getLogger("pack");
    private static final Logger LABEL_LOG = LoggerFactory.getLogger("label");

    @Bean
    public Function<OrderAcceptedMessage, Long> pack() {
        return orderAcceptedMessage -> {
            PACK_LOG.info("The order with id {} is packed.", orderAcceptedMessage.getOrderId());
            return orderAcceptedMessage.getOrderId();
        };
    }


    @Bean
    public Function<Flux<Long>, Flux<OrderDispatchedMessage>> label() {
        return orderFlux -> orderFlux.map(orderId -> {
            LABEL_LOG.info("The order with id {} is labeled.", orderId);
            return new OrderDispatchedMessage(orderId);
        });
    }
}
