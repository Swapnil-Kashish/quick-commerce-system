package com.quickcommerce.payment_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentSuccessEvent {

    private String eventId;

    private Long productId;

    private Integer quantity;

    private String paymentStatus;
}