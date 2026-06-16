package com.quickcommerce.order_service.kafka;

import com.quickcommerce.order_service.entity.Order;
import com.quickcommerce.order_service.enums.OrderStatus;
import com.quickcommerce.order_service.model.InventoryResponseEvent;
import com.quickcommerce.order_service.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OrderConsumer {

    @Autowired
    private OrderRepository orderRepository;

    @KafkaListener(
            topics = "inventory-response-topic",
            groupId = "order-group"
    )
    public void consume(InventoryResponseEvent event) {
        Order order = orderRepository
                .findByEventId(event.getEventId())
                .orElseThrow(() ->
                        new RuntimeException("Order not found"));
        OrderStatus currentStatus = order.getStatus();
        if (currentStatus == OrderStatus.CONFIRMED
                || currentStatus == OrderStatus.FAILED) {
            System.out.println(
                    "⚠️ Duplicate status ignored for: "
                            + event.getEventId()
            );
            return;
        }
        order.setStatus(OrderStatus.valueOf(event.getStatus()));
        orderRepository.save(order);
        System.out.println("📩 Inventory response received");
        System.out.println("🆔 EventId: "
                + event.getEventId());
        System.out.println("📦 Product: "
                + event.getProductId());
        System.out.println("✅ Status: "
                + event.getStatus());
    }
}