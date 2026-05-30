package com.quickcommerce.payment_service.producer;

import com.quickcommerce.payment_service.dto.PaymentEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendPaymentEvent(
            PaymentEvent event
    ) {

        kafkaTemplate.send(
                "payment-response-topic",
                event
        );

        System.out.println(
                "💰 Payment event sent: "
                        + event.getEventId()
        );
    }
}