package com.quickcommerce.cart_service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddToCartRequest {

    private Long userId;

    private Long productId;

    private Integer quantity;
}