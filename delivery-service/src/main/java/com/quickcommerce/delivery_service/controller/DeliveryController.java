package com.quickcommerce.delivery_service.controller;

import com.quickcommerce.delivery_service.entity.Delivery;
import com.quickcommerce.delivery_service.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/deliveries")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

    @GetMapping("/{eventId}")
    public Delivery getDelivery(
            @PathVariable String eventId
    ) {

        return deliveryService.getDelivery(
                eventId
        );
    }

    @PutMapping("/{id}/status")
    public Delivery updateStatus(
            @PathVariable Long id,
            @RequestParam String status
    ) {

        return deliveryService.updateStatus(
                id,
                status
        );
    }
}