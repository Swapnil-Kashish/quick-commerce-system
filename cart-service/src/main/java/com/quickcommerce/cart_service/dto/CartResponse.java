package com.quickcommerce.cart_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartResponse {

    private Long id;

    private Long userId;

    private Double totalAmount;

    private List<CartItemResponse> items;
}