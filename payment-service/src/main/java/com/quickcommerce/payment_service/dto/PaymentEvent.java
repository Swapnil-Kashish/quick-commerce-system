package com.quickcommerce.payment_service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentEvent {

    private String eventId;

    private String status;
}