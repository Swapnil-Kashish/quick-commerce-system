package com.quickcommerce.inventory_service.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class InventoryResponseEvent {

    private Long productId;
    private boolean inStock;
    private String status;

}