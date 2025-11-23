package com.spike.springcloud.stream.domain;

public class OrderAcceptedMessage {
    private Long orderId;

    public OrderAcceptedMessage() {
    }

    public OrderAcceptedMessage(Long orderId) {
        this.orderId = orderId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public static OrderAcceptedMessage of(Long orderId) {
        return new OrderAcceptedMessage(orderId);
    }

}
