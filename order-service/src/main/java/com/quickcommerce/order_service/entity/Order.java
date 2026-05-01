package com.quickcommerce.order_service.entity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Order {
    private Long productId;
    private int quantity;
    private String status;

}