package com.quickcommerce.payment_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryResponseEvent {

    private String eventId;

    private Long productId;

    private boolean inStock;

    private String status;
}