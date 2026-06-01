package com.quickcommerce.payment_service.producer;

import com.quickcommerce.payment_service.dto.PaymentSuccessEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentSuccessProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendPaymentSuccess(
            PaymentSuccessEvent event
    ) {

        kafkaTemplate.send(
                "payment-success-topic",
                event
        );

        System.out.println(
                "✅ Payment Success Event Sent: "
                        + event.getEventId()
        );
    }
}