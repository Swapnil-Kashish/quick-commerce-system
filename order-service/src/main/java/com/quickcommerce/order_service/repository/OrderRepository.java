package com.quickcommerce.order_service.repository;

import com.quickcommerce.order_service.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByEventId(String eventId);
}