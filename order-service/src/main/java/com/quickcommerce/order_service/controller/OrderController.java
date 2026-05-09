package com.quickcommerce.order_service.controller;

import com.quickcommerce.order_service.entity.Order;
import com.quickcommerce.order_service.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public Order createOrder(@RequestBody Order order) {
        return orderService.createOrder(order);
    }

    @GetMapping("/status/{eventId}")
    public String getStatus(
            @PathVariable String eventId
    ) {
        return orderService.getOrderStatus(eventId);
    }
}