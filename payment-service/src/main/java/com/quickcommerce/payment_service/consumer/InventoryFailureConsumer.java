package com.quickcommerce.payment_service.consumer;

import com.quickcommerce.payment_service.dto.InventoryResponseEvent;
import com.quickcommerce.payment_service.dto.RefundEvent;
import com.quickcommerce.payment_service.producer.RefundProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryFailureConsumer {

    private final RefundProducer refundProducer;

    @KafkaListener(
            topics = "inventory-response-topic",
            groupId = "payment-refund-group"
    )
    public void consume(
            InventoryResponseEvent event
    ) {

        if(!"FAILED".equals(event.getStatus())) {
            return;
        }

        System.out.println(
                "❌ Inventory Failed. Triggering refund..."
        );

        refundProducer.sendRefund(
                new RefundEvent(
                        event.getEventId(),
                        "OUT_OF_STOCK"
                )
        );
    }
}