package com.quickcommerce.cart_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CartItemResponse {

    private Long id;

    private Long productId;

    private Integer quantity;

    private Double price;
}