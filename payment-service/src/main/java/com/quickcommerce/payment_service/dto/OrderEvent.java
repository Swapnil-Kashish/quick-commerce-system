package com.quickcommerce.payment_service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderEvent {

    private String eventId;

    private Long productId;

    private Integer quantity;
}