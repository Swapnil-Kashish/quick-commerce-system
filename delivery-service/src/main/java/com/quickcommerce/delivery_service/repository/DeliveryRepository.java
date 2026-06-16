package com.quickcommerce.delivery_service.repository;

import com.quickcommerce.delivery_service.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeliveryRepository
        extends JpaRepository<Delivery, Long> {

    Optional<Delivery> findByOrderEventId(
            String orderEventId
    );
}