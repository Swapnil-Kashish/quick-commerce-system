package com.quickcommerce.order_service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentResponseEvent {

    private String eventId;

    private String status;
}