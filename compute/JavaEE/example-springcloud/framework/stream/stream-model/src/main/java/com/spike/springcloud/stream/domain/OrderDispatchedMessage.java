package com.spike.springcloud.stream.domain;

import java.util.Objects;

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

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        OrderDispatchedMessage that = (OrderDispatchedMessage) object;
        return Objects.equals(orderId, that.orderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId);
    }

    public static OrderDispatchedMessage of(Long orderId) {
        return new OrderDispatchedMessage(orderId);
    }
}
