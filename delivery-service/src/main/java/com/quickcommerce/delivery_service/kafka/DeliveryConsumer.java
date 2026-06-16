package com.quickcommerce.delivery_service.kafka;

import com.quickcommerce.delivery_service.entity.Delivery;
import com.quickcommerce.delivery_service.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DeliveryConsumer {

    private final DeliveryRepository deliveryRepository;

    @KafkaListener(
            topics = "order-topic",
            groupId = "delivery-group"
    )
    public void consume(
            OrderEvent event
    ) {

        System.out.println(
                "🚚 Order received for delivery: "
                        + event.getEventId()
        );

        Delivery delivery =
                new Delivery();

        delivery.setOrderEventId(
                event.getEventId()
        );

        delivery.setStatus(
                "PENDING"
        );
        delivery.setDeliveryPartner("Shadowfax");
        delivery.setCreatedAt(LocalDateTime.now());
        delivery.setEstimatedDeliveryTime(
                LocalDateTime.now().plusMinutes(30)
        );

        deliveryRepository.save(
                delivery
        );

        System.out.println(
                "✅ Delivery created"
        );
    }
}