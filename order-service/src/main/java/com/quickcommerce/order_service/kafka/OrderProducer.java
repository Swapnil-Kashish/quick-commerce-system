package com.quickcommerce.order_service.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderProducer {

    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

    public OrderProducer(KafkaTemplate<String, OrderEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    public void sendOrder(OrderEvent event) {

        System.out.println("📤 Sending order event to Kafka...");
        kafkaTemplate.send("order-topic", String.valueOf(event.getProductId()), event)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        System.out.println("✅ Sent to partition: " +
                                result.getRecordMetadata().partition());
                    } else {
                        System.out.println("❌ Failed: " + ex.getMessage());
                    }
                });
    }
}