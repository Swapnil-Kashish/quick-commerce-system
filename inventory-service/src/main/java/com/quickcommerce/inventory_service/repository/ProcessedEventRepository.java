package com.quickcommerce.inventory_service.repository;

import com.quickcommerce.inventory_service.entity.ProcessedEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessedEventRepository
        extends JpaRepository<ProcessedEvent, String> {
}