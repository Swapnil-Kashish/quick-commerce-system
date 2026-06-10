package com.quickcommerce.cart_service.dto;

import lombok.Data;

@Data
public class InventoryRequest {

    private Long productId;

    private Integer quantity;
}