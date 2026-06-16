package com.quickcommerce.delivery_service.service;

import com.quickcommerce.delivery_service.entity.Delivery;
import com.quickcommerce.delivery_service.enums.DeliveryStatus;
import com.quickcommerce.delivery_service.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;

    public Delivery createDelivery(
            String orderEventId
    ) {

        Delivery delivery =
                Delivery.builder()
                        .orderEventId(orderEventId)
                        .deliveryPartner("Shadowfax")
                        .status(
                                DeliveryStatus.ASSIGNED.name()
                        )
                        .createdAt(
                                LocalDateTime.now()
                        )
                        .estimatedDeliveryTime(
                                LocalDateTime.now()
                                        .plusHours(2)
                        )
                        .build();

        return deliveryRepository.save(
                delivery
        );
    }

    public Delivery getDelivery(
            String eventId
    ) {

        return deliveryRepository
                .findByOrderEventId(eventId)
                .orElseThrow();
    }

    public Delivery updateStatus(
            Long id,
            String status
    ) {

        Delivery delivery =
                deliveryRepository
                        .findById(id)
                        .orElseThrow();

        delivery.setStatus(status);

        return deliveryRepository.save(
                delivery
        );
    }
}