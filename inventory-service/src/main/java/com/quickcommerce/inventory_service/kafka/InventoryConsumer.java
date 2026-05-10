package com.quickcommerce.inventory_service.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quickcommerce.inventory_service.entity.Inventory;
import com.quickcommerce.inventory_service.entity.OutboxEvent;
import com.quickcommerce.inventory_service.entity.ProcessedEvent;
import com.quickcommerce.inventory_service.model.InventoryResponseEvent;
import com.quickcommerce.inventory_service.repository.InventoryRepository;
import com.quickcommerce.inventory_service.repository.OutboxEventRepository;
import com.quickcommerce.inventory_service.repository.ProcessedEventRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class InventoryConsumer {

    private final InventoryProducer inventoryProducer;

    @Autowired
    private ProcessedEventRepository processedEventRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OutboxEventRepository outboxEventRepository;

    public InventoryConsumer(InventoryProducer inventoryProducer) {
        this.inventoryProducer = inventoryProducer;
    }

    @Transactional
    @RetryableTopic(
            attempts = "3",
            backoff = @Backoff(delay = 5000, multiplier = 2),
            autoCreateTopics = "true"
    )
    @KafkaListener(topics = "order-topic", groupId = "inventory-group")
    public void consume(OrderEvent event) {
        String eventId = event.getEventId();
        // ✅ Validate eventId
        if (eventId == null) {
            throw new IllegalArgumentException(
                    "EventId is missing!"
            );
        }
        // ✅ Idempotency check
        if (processedEventRepository.existsById(eventId)) {
            System.out.println(
                    "⚠️ Duplicate event ignored: "
                            + eventId
            );
            return;
        }
        System.out.println(
                "📥 Received order: "
                        + event.getProductId()
        );
        // ✅ Fetch inventory from DB
        Inventory inventory = inventoryRepository
                .findById(event.getProductId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Product not found"
                        ));
        int available = inventory.getAvailableQuantity();
        System.out.println(
                "📦 Available stock: "
                        + available
        );
        // ✅ Failure scenario
        if (available < event.getQuantity()) {
            System.out.println("❌ Out of stock!");
            InventoryResponseEvent response =
                    new InventoryResponseEvent(
                            eventId,
                            event.getProductId(),
                            false,
                            "FAILED"
                    );
            saveOutboxEvent(response, eventId);
            throw new IllegalStateException(
                    "Stock not available"
            );
        }
        // ✅ Update inventory in DB
        inventory.setAvailableQuantity(
                available - event.getQuantity()
        );
        inventoryRepository.save(inventory);
        // ✅ Save processed event for idempotency
        processedEventRepository.save(
                new ProcessedEvent(eventId)
        );
        System.out.println(
                "✅ Inventory updated. Remaining: "
                        + inventory.getAvailableQuantity()
        );
        // ✅ Send SUCCESS response
        InventoryResponseEvent response =
                new InventoryResponseEvent(
                        eventId,
                        event.getProductId(),
                        true,
                        "SUCCESS"
                );
        saveOutboxEvent(response, eventId);
    }

    @KafkaListener(topics = "order-topic-dlt", groupId = "inventory-dlt-group")
    public void consumeDLQ(OrderEvent event) {
        System.out.println("💀 Message moved to DLQ: " + event.getProductId());
    }

    private void saveOutboxEvent(
            InventoryResponseEvent response,
            String eventId
    ) {
        try {
            OutboxEvent outboxEvent =
                    new OutboxEvent();
            outboxEvent.setEventId(eventId);
            outboxEvent.setTopic(
                    "inventory-response-topic"
            );
            outboxEvent.setPayload(
                    objectMapper.writeValueAsString(
                            response
                    )
            );
            outboxEvent.setPublished(false);
            outboxEvent.setCreatedAt(
                    LocalDateTime.now()
            );
            outboxEventRepository.save(outboxEvent);
            System.out.println(
                    "📦 Outbox event saved"
            );
        } catch (Exception ex) {
            throw new RuntimeException(
                    "Failed to save outbox event"
            );
        }
    }
}