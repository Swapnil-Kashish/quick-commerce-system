package com.quickcommerce.order_service.service;

import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class OrderStatusStore {
    private final Map<String, String> orderStatusMap =
            new ConcurrentHashMap<>();
    public void updateStatus(String eventId, String status) {
        orderStatusMap.put(eventId, status);
    }
    public String getStatus(String eventId) {
        return orderStatusMap.getOrDefault(eventId, "NOT_FOUND");
    }
}