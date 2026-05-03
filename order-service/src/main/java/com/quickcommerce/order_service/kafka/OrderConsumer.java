package com.quickcommerce.order_service.kafka;

import com.quickcommerce.order_service.model.InventoryResponseEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service

public class OrderConsumer {
    @KafkaListener(topics = "inventory-response-topic", groupId = "order-group")
    public void consume(InventoryResponseEvent event) {
        System.out.println("📩 Inventory response received");
        System.out.println("Product: " + event.getProductId());
        System.out.println("Status: " + event.getStatus());
        // later: update DB
    }
}