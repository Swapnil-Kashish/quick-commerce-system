package com.quickcommerce.inventory_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "outbox_events")
public class OutboxEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String eventId;
    private String topic;
    @Column(columnDefinition = "TEXT")
    private String payload;
    private boolean published;
    private LocalDateTime createdAt;
}