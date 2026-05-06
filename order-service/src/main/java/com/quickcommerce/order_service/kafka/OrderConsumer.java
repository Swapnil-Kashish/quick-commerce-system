package com.quickcommerce.order_service.kafka;

import com.quickcommerce.order_service.model.InventoryResponseEvent;
import com.quickcommerce.order_service.service.OrderStatusStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service

public class OrderConsumer {

    @Autowired
    private OrderStatusStore orderStatusStore;

    @KafkaListener(topics = "inventory-response-topic", groupId = "order-group")
    public void consume(InventoryResponseEvent event) {
        String currentStatus = orderStatusStore.getStatus(event.getEventId());

        // ✅ Prevent duplicate final-state updates
        if ("SUCCESS".equals(currentStatus) || "FAILED".equals(currentStatus)) {
            System.out.println("⚠️ Duplicate status ignored for: " + event.getEventId());
            return;
        }
        orderStatusStore.updateStatus(
                event.getEventId(),
                event.getStatus()
        );
        System.out.println("📩 Inventory response received");
        System.out.println("🆔 EventId: " + event.getEventId());
        System.out.println("Product: " + event.getProductId());
        System.out.println("Status: " + event.getStatus());
        // later: update DB
    }
}