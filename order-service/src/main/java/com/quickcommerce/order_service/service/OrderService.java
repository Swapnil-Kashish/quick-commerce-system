package com.quickcommerce.order_service.service;

import com.quickcommerce.order_service.dto.InventoryResponse;
import com.quickcommerce.order_service.entity.Order;
import com.quickcommerce.order_service.kafka.OrderEvent;
import com.quickcommerce.order_service.kafka.OrderProducer;
import com.quickcommerce.order_service.repository.OrderRepository;
import com.quickcommerce.order_service.client.InventoryClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@Service
public class OrderService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private InventoryClient inventoryClient;

    private OrderProducer orderProducer;

    @Autowired
    private OrderRepository orderRepository;

//    @CircuitBreaker(
//            name = "inventoryService",
//            fallbackMethod = "fallbackOrder"
//    )
//    public Order createOrder(Order order) {
//        InventoryResponse response =
//                inventoryClient.checkInventory(
//                        order.getProductId(),
//                        order.getQuantity()
//                );
//        if (!response.isInStock()) {
//            throw new RuntimeException("Product out of stock");
//        }
//        System.out.println("📤 Sending order to Kafka...");
//        OrderEvent event = new OrderEvent();
//        event.setEventId(UUID.randomUUID().toString());
//        event.setProductId(order.getProductId());
//        event.setQuantity(order.getQuantity());
//        order.setEventId(event.getEventId());
//        order.setStatus("PROCESSING");
//        order.setCreatedAt(LocalDateTime.now());
//        Order savedOrder = orderRepository.save(order);
//        System.out.println(
//                "📤 Sending eventId: " + event.getEventId()
//        );
//        orderProducer.sendOrder(event);
//        return savedOrder;
//    }

    public Order createOrder(Order order) {

        System.out.println("📤 Sending order to Kafka...");
        OrderEvent event = new OrderEvent();

        event.setEventId(
                UUID.randomUUID().toString()
        );

        event.setProductId(
                order.getProductId()
        );

        event.setQuantity(
                order.getQuantity()
        );

        order.setEventId(
                event.getEventId()
        );

        order.setStatus(
                "PROCESSING"
        );

        order.setCreatedAt(
                LocalDateTime.now()
        );

        Order savedOrder =
                orderRepository.save(order);

        System.out.println(
                "📤 Sending eventId: "
                        + event.getEventId()
        );
        orderProducer.sendOrder(event);
        return savedOrder;
    }

    public Order fallbackOrder(Order order, Exception ex) {
        System.out.println("⚠️ Circuit Breaker Activated! Inventory service unavailable.");
        order.setStatus("FAILED");
        return order;
    }

    public String getOrderStatus(String eventId) {

        return orderRepository
                .findByEventId(eventId)
                .map(Order::getStatus)
                .orElse("NOT_FOUND");
    }
}
