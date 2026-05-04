package com.quickcommerce.inventory_service.kafka;

import com.quickcommerce.inventory_service.model.InventoryResponseEvent;
import jakarta.annotation.PostConstruct;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class InventoryConsumer {

    private final InventoryProducer inventoryProducer;
    private final Set<String> processedEvents = ConcurrentHashMap.newKeySet();

    public InventoryConsumer(InventoryProducer inventoryProducer) {
        this.inventoryProducer = inventoryProducer;
    }
    private final Map<Long, Integer> inventory = new ConcurrentHashMap<>();

    @PostConstruct
    public void initInventory() {
        inventory.put(1L, 10); // demo data
    }

    @RetryableTopic(
            attempts = "3",
            backoff = @Backoff(delay = 5000, multiplier = 2),
            autoCreateTopics = "true"
    )
    @KafkaListener(topics = "order-topic", groupId = "inventory-group")
    public void consume(OrderEvent event) {
        String eventId = event.getEventId();
        if (eventId == null) {
            throw new IllegalArgumentException("EventId is missing!");
        }
        if (processedEvents.contains(eventId)) {
            System.out.println("⚠️ Duplicate event ignored: " + eventId);
            return;
        }
        System.out.println("📥 Received order: " + event.getProductId());
        int available = inventory.getOrDefault(event.getProductId(), 0);
        System.out.println("📦 Available stock: " + available);
        if (available < event.getQuantity()) {
            System.out.println("❌ Out of stock!");
            throw new IllegalStateException("Stock not available"); // triggers retry
        }
        inventory.put(event.getProductId(), available - event.getQuantity());
        processedEvents.add(eventId);
        System.out.println("✅ Inventory updated. Remaining: " +
                inventory.get(event.getProductId()));
        InventoryResponseEvent response = new InventoryResponseEvent(
                event.getProductId(),
                true,
                "SUCCESS"
        );
        inventoryProducer.sendResponse(response);
    }

    @KafkaListener(topics = "order-topic-dlt", groupId = "inventory-dlt-group")
    public void consumeDLQ(OrderEvent event) {
        System.out.println("💀 Message moved to DLQ: " + event.getProductId());
    }
}