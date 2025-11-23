package com.spike.springcloud.function.infra.functions;

import com.spike.springcloud.function.domain.OrderAcceptedMessage;
import com.spike.springcloud.function.domain.OrderDispatchedMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

import java.util.function.Function;

@Configuration
public class DispatchingFunctionConfig {
    private static final Logger LOG = LoggerFactory.getLogger(DispatchingFunctionConfig.class);

    // imperative function
    @Bean
    public Function<OrderAcceptedMessage, Long> pack() {
        return orderAcceptedMessage -> {
            LOG.info("The order with id {} is packed.", orderAcceptedMessage.getOrderId());
            return orderAcceptedMessage.getOrderId();
        };
    }

    // reactive function
    @Bean
    public Function<Flux<Long>, Flux<OrderDispatchedMessage>> label() {
        return orderFlux -> orderFlux.map(orderId -> {
            LOG.info("The order with id {} is labeled.", orderId);
            return new OrderDispatchedMessage(orderId);
        });
    }
}
