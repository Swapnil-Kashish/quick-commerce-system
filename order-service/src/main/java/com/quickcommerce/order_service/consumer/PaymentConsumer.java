package com.quickcommerce.order_service.consumer;

import com.quickcommerce.order_service.dto.PaymentResponseEvent;
import com.quickcommerce.order_service.entity.Order;
import com.quickcommerce.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentConsumer {

    private final OrderRepository orderRepository;

    @KafkaListener(
            topics = "payment-response-topic",
            groupId = "order-payment-group"
    )
    public void consumePaymentResponse(
            PaymentResponseEvent event
    ) {

        System.out.println(
                "💳 Payment Response Received: "
                        + event.getEventId()
        );

        Order order =
                orderRepository
                        .findByEventId(
                                event.getEventId()
                        )
                        .orElse(null);

        if (order == null) {

            System.out.println(
                    "❌ Order not found for eventId: "
                            + event.getEventId()
            );

            return;
        }

        order.setStatus(
                event.getStatus()
        );

        orderRepository.save(order);

        System.out.println(
                "✅ Order Updated: "
                        + order.getStatus()
        );
    }
}