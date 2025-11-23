package com.spike.springcloud.stream.web;

import com.spike.springcloud.stream.domain.OrderAcceptedMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private static final Logger LOG = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private StreamBridge streamBridge; // 流桥接: 生产消息

    @Transactional // 事务
    @PostMapping
    public String postOrder(@RequestBody OrderAcceptedMessage message,
                            @RequestParam(value = "rollback", defaultValue = "false") boolean rollback) {
        if (Objects.isNull(message.getOrderId())) {
            return "Null orderId";
        }

        LOG.info("Sending order accepted event with id: {}", message.getOrderId());
        boolean result = streamBridge.send("acceptOrder-out-0", message);
        LOG.info("Result of sending data for order with id {}: {}", message.getOrderId(), result);
        if (rollback) {
            throw new RuntimeException("Rollback!");
        }
        return "Ok";
    }
}
