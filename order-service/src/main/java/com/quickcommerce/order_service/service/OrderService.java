package com.quickcommerce.order_service.service;

import com.quickcommerce.order_service.entity.Order;
import com.quickcommerce.order_service.kafka.OrderEvent;
import com.quickcommerce.order_service.kafka.OrderProducer;
import com.quickcommerce.order_service.webclient.InventoryClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@AllArgsConstructor
@Service
public class OrderService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private InventoryClient inventoryClient;

    private OrderProducer orderProducer;

    @CircuitBreaker(name = "inventoryService", fallbackMethod = "fallbackOrder")
    public Order createOrder(Order order) {

        System.out.println("📤 Sending order to Kafka...");
        OrderEvent event = new OrderEvent(
                order.getProductId(),
                order.getQuantity()
        );
        orderProducer.sendOrder(event);
        order.setStatus("PROCESSING");
        return order;

    }
    public Order fallbackOrder(Order order, Exception ex) {

        System.out.println("⚠️ Circuit Breaker Activated! Inventory service unavailable.");
        order.setStatus("FAILED");
        return order;
    }
}
