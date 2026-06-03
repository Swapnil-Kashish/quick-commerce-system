package com.quickcommerce.cart_service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductResponse {

    private Long id;

    private String name;

    private Double price;

    private Integer quantity;
}