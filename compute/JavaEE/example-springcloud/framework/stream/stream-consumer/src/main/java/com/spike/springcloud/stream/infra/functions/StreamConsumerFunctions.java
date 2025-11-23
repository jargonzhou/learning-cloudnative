package com.spike.springcloud.stream.infra.functions;

import com.spike.springcloud.stream.domain.OrderDispatchedMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

import java.util.function.Consumer;

@Configuration
public class StreamConsumerFunctions {
    private static final Logger DISPATCH_ORDER_LOG = LoggerFactory.getLogger("dispatchOrder");

    @Bean
    public Consumer<Flux<OrderDispatchedMessage>> dispatchOrder() {
        // 输出日志
        return flux -> flux.subscribe(orderDispatchedMessage -> {
            DISPATCH_ORDER_LOG.info("The order with id {} is dispatched", orderDispatchedMessage.getOrderId());
        });

    }
}
