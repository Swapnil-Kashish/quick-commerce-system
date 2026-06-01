package com.quickcommerce.inventory_service.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quickcommerce.inventory_service.dto.PaymentSuccessEvent;
import com.quickcommerce.inventory_service.entity.Inventory;
import com.quickcommerce.inventory_service.entity.OutboxEvent;
import com.quickcommerce.inventory_service.entity.ProcessedEvent;
import com.quickcommerce.inventory_service.model.InventoryResponseEvent;
import com.quickcommerce.inventory_service.repository.InventoryRepository;
import com.quickcommerce.inventory_service.repository.OutboxEventRepository;
import com.quickcommerce.inventory_service.repository.ProcessedEventRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class InventoryConsumer {

    private final InventoryRepository inventoryRepository;
    private final ProcessedEventRepository processedEventRepository;
    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    @RetryableTopic(
            attempts = "3",
            backoff = @Backoff(delay = 5000, multiplier = 2),
            autoCreateTopics = "true"
    )
    @KafkaListener(
            topics = "payment-success-topic",
            groupId = "inventory-group"
    )
    public void consume(PaymentSuccessEvent event) {

        String eventId = event.getEventId();

        if (eventId == null) {
            throw new IllegalArgumentException(
                    "EventId is missing!"
            );
        }

        if (processedEventRepository.existsById(eventId)) {
            System.out.println(
                    "⚠️ Duplicate event ignored: "
                            + eventId
            );
            return;
        }
        System.out.println(
                "💳 Payment Success Received: "
                        + eventId
        );

        Inventory inventory =
                inventoryRepository
                        .findById(event.getProductId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Product not found"
                                ));

        int available =
                inventory.getAvailableQuantity();

        System.out.println(
                "📦 Available stock: "
                        + available
        );

        if (available < event.getQuantity()) {

            System.out.println(
                    "❌ Out of stock!"
            );

            InventoryResponseEvent response =
                    new InventoryResponseEvent(
                            eventId,
                            event.getProductId(),
                            false,
                            "FAILED"
                    );

            saveOutboxEvent(
                    response,
                    eventId
            );

            return;
        }

        inventory.setAvailableQuantity(
                available - event.getQuantity()
        );

        inventoryRepository.save(
                inventory
        );

        processedEventRepository.save(
                new ProcessedEvent(
                        eventId
                )
        );
        System.out.println(
                "✅ Inventory updated. Remaining: "
                        + inventory.getAvailableQuantity()
        );

        InventoryResponseEvent response =
                new InventoryResponseEvent(
                        eventId,
                        event.getProductId(),
                        true,
                        "SUCCESS"
                );

        saveOutboxEvent(
                response,
                eventId
        );
    }

    @KafkaListener(
            topics = "payment-success-topic-dlt",
            groupId = "inventory-dlt-group"
    )
    public void consumeDLQ(
            PaymentSuccessEvent event
    ) {

        System.out.println(
                "💀 Message moved to DLQ: "
                        + event.getProductId()
        );
    }

    private void saveOutboxEvent(
            InventoryResponseEvent response,
            String eventId
    ) {
        try {
            OutboxEvent outboxEvent =
                    new OutboxEvent();
            outboxEvent.setEventId(
                    eventId
            );
            outboxEvent.setTopic(
                    "inventory-response-topic"
            );
            outboxEvent.setPayload(
                    objectMapper.writeValueAsString(
                            response
                    )
            );
            outboxEvent.setPublished(
                    false
            );
            outboxEvent.setCreatedAt(
                    LocalDateTime.now()
            );
            outboxEventRepository.save(
                    outboxEvent
            );
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