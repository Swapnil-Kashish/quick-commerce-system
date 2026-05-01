package com.quickcommerce.order_service.controller;

import com.quickcommerce.order_service.entity.Order;
import com.quickcommerce.order_service.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService service;

    @PostMapping
    public Order createOrder(@RequestBody Order order) {
        return service.createOrder(order);
    }
}