package com.quickcommerce.payment_service.producer;

import com.quickcommerce.payment_service.dto.RefundEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefundProducer {

    private final KafkaTemplate<String,Object> kafkaTemplate;

    public void sendRefund(
            RefundEvent event
    ) {

        kafkaTemplate.send(
                "refund-topic",
                event
        );

        System.out.println(
                "💸 Refund Event Sent: "
                        + event.getEventId()
        );
    }
}