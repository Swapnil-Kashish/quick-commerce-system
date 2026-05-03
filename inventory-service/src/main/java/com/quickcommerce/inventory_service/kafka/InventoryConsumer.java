package com.quickcommerce.inventory_service.kafka;

import com.quickcommerce.inventory_service.model.InventoryResponseEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class InventoryConsumer {

    private final InventoryProducer inventoryProducer;

    public InventoryConsumer(InventoryProducer inventoryProducer) {
        this.inventoryProducer = inventoryProducer;
    }

    @KafkaListener(topics = "order-topic", groupId = "inventory-group")
    public void consume(OrderEvent event) {
        System.out.println("📥 Received order for product: " + event.getProductId());
        boolean inStock = event.getQuantity() <= 5;
        if (inStock) {
            System.out.println("✅ Stock available. Updating inventory...");
        } else {
            System.out.println("❌ Out of stock!");
        }
        // 🔥 SEND RESPONSE BACK
        InventoryResponseEvent response = new InventoryResponseEvent(
                event.getProductId(),
                inStock,
                inStock ? "SUCCESS" : "FAILED"
        );
        inventoryProducer.sendResponse(response);
    }

}