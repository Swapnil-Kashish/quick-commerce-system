package com.quickcommerce.order_service.controller;

import com.quickcommerce.order_service.entity.Order;
import com.quickcommerce.order_service.service.OrderService;
import com.quickcommerce.order_service.service.OrderStatusStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService service;
    @Autowired
    private OrderStatusStore orderStatusStore;

    @PostMapping
    public Order createOrder(@RequestBody Order order) {
        return service.createOrder(order);
    }

    @GetMapping("/status/{eventId}")
    public String getStatus(@PathVariable String eventId) {
        return orderStatusStore.getStatus(eventId);
    }
}