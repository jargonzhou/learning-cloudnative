package com.spike.springcloud.function.domain;

public class OrderDispatchedMessage {
    private Long orderId;

    public OrderDispatchedMessage() {
    }

    public OrderDispatchedMessage(Long orderId) {
        this.orderId = orderId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public static OrderDispatchedMessage of(Long orderId) {
        return new OrderDispatchedMessage(orderId);
    }
}
