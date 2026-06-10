package com.quickcommerce.inventory_service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "inventory")
public class Inventory {

    @Id
    private Long productId;
    @Column(nullable = false)
    private Integer availableQuantity;
    @Column(nullable = false)
    private Integer reservedQuantity;}