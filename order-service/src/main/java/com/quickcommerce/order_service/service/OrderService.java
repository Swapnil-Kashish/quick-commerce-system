package com.quickcommerce.order_service.service;

import com.quickcommerce.order_service.entity.Order;
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


    @CircuitBreaker(name = "inventoryService", fallbackMethod = "fallbackOrder")
    public Order createOrder(Order order) {

        String url = "http://localhost:8082/inventory/check?productId="
                + order.getProductId() + "&quantity=" + order.getQuantity();
        System.out.println("👉 Calling Inventory Service...");
        Boolean inStock = inventoryClient.checkInventory(url);
        if (Boolean.FALSE.equals(inStock)) {
            order.setStatus("FAILED");
            return order;
        }
        order.setStatus("SUCCESS");
        return order;
    }
    public Order fallbackOrder(Order order, Exception ex) {

        System.out.println("⚠️ Circuit Breaker Activated! Inventory service unavailable.");
        order.setStatus("FAILED");
        return order;
    }
}
