package com.quickcommerce.cart_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CartResponse {

    private Long id;

    private Long userId;

    private Double totalAmount;

    private List<CartItemResponse> items;
}