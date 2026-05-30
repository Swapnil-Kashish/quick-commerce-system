package com.quickcommerce.payment_service.consumer;

import com.quickcommerce.payment_service.dto.OrderEvent;
import com.quickcommerce.payment_service.dto.PaymentEvent;
import com.quickcommerce.payment_service.entity.Payment;
import com.quickcommerce.payment_service.producer.PaymentProducer;
import com.quickcommerce.payment_service.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderConsumer {

    private final PaymentRepository paymentRepository;

    private final PaymentProducer paymentProducer;

    @KafkaListener(
            topics = "order-topic",
            groupId = "payment-group"
    )
    public void consumeOrder(OrderEvent event) {

        System.out.println("📥 Received Order Event: " + event.getEventId());
        Payment payment = new Payment();
        payment.setOrderEventId(event.getEventId());
        payment.setAmount(500.0);
        payment.setCreatedAt(LocalDateTime.now());
        if (event.getQuantity() > 5) {
            payment.setStatus("FAILED");
        } else {
            payment.setStatus("SUCCESS");
        }
        paymentRepository.save(payment);
        PaymentEvent paymentEvent =
                new PaymentEvent();
        paymentEvent.setEventId(
                event.getEventId()
        );
        paymentEvent.setStatus(
                payment.getStatus()
        );
        paymentProducer.sendPaymentEvent(
                paymentEvent
        );
    }
}