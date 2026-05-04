package com.quickcommerce.order_service.kafka;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderEvent {
    private String eventId;
    private Long productId;
    private int quantity;

    public OrderEvent(Long productId, int quantity) {
        this.eventId = UUID.randomUUID().toString();
        this.productId = productId;
        this.quantity = quantity;

    }
}