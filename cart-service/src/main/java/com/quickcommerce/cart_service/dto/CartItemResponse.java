package com.quickcommerce.cart_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemResponse {

    private Long id;

    private Long productId;

    private Integer quantity;

    private Double price;
}