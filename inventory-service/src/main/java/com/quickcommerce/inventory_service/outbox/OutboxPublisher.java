package com.quickcommerce.inventory_service.outbox;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quickcommerce.inventory_service.entity.OutboxEvent;
import com.quickcommerce.inventory_service.model.InventoryResponseEvent;
import com.quickcommerce.inventory_service.repository.OutboxEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OutboxPublisher {

    @Autowired
    private OutboxEventRepository outboxEventRepository;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Scheduled(fixedRate = 5000)
    public void publishOutboxEvents() {
        List<OutboxEvent> events =
                outboxEventRepository
                        .findByPublishedFalse();
        for (OutboxEvent event : events) {
            try {
                InventoryResponseEvent response =
                        objectMapper.readValue(
                                event.getPayload(),
                                InventoryResponseEvent.class
                        );
                kafkaTemplate.send(
                        event.getTopic(),
                        response
                );
                event.setPublished(true);
                outboxEventRepository.save(event);
                System.out.println(
                        "🚀 Published outbox event: "
                                + event.getEventId()
                );
            } catch (Exception ex) {
                System.out.println(
                        "❌ Failed to publish outbox event"
                );
                ex.printStackTrace();
            }
        }
    }
}