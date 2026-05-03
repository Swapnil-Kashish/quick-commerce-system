package com.quickcommerce.inventory_service.kafka;

import com.quickcommerce.inventory_service.model.InventoryResponseEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class InventoryProducer {

    private final KafkaTemplate<String, InventoryResponseEvent> kafkaTemplate;

    public InventoryProducer(KafkaTemplate<String, InventoryResponseEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendResponse(InventoryResponseEvent event) {
        kafkaTemplate.send("inventory-response-topic", event);
    }
}