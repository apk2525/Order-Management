package com.furniture.orders.exception;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
public class OrderNotFoundException extends RuntimeException {

    private final Long orderId;

    public OrderNotFoundException(Long orderId) {
        super("Order not found: " + orderId);
        this.orderId = orderId;
    }

}
