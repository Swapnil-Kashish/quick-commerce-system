package com.quickcommerce.delivery_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "deliveries")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderEventId;

    private String deliveryPartner;

    private String status;

    private LocalDateTime createdAt;

    private LocalDateTime estimatedDeliveryTime;
}