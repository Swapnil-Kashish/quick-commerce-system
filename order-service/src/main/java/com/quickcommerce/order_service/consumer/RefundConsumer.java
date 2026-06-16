package com.quickcommerce.order_service.consumer;

import com.quickcommerce.order_service.dto.RefundEvent;
import com.quickcommerce.order_service.entity.Order;
import com.quickcommerce.order_service.enums.OrderStatus;
import com.quickcommerce.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefundConsumer {

    private final OrderRepository orderRepository;

    @KafkaListener(
            topics = "refund-topic",
            groupId = "order-refund-group"
    )
    public void consume(
            RefundEvent event
    ) {

        Order order =
                orderRepository
                        .findByEventId(
                                event.getEventId()
                        )
                        .orElseThrow();

        order.setStatus(
                OrderStatus.REFUNDED
        );

        orderRepository.save(order);

        System.out.println(
                "💸 Order Refunded: "
                        + event.getEventId()
        );
    }
}