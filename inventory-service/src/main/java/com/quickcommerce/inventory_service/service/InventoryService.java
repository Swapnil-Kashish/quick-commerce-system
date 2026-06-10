package com.quickcommerce.inventory_service.service;

import com.quickcommerce.inventory_service.entity.Inventory;
import com.quickcommerce.inventory_service.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    public void reserveStock(
            Long productId,
            Integer quantity
    ) {

        Inventory inventory =
                inventoryRepository.findByProductId(productId)
                        .orElseThrow();
        if (inventory.getAvailableQuantity() < quantity) {
            throw new RuntimeException(
                    "Insufficient stock"
            );
        }
        inventory.setAvailableQuantity(
                inventory.getAvailableQuantity() - quantity
        );
        inventory.setReservedQuantity(
                inventory.getReservedQuantity() + quantity
        );
        inventoryRepository.save(inventory);
    }

    public void releaseStock(
            Long productId,
            Integer quantity
    ) {

        Inventory inventory =
                inventoryRepository.findByProductId(productId)
                        .orElseThrow();

        inventory.setAvailableQuantity(
                inventory.getAvailableQuantity() + quantity
        );
        inventory.setReservedQuantity(
                inventory.getReservedQuantity() - quantity
        );
        inventoryRepository.save(inventory);
    }
}
