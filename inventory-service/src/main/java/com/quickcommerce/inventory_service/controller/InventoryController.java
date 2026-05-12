package com.quickcommerce.inventory_service.controller;

import com.quickcommerce.inventory_service.dto.InventoryResponse;
import com.quickcommerce.inventory_service.entity.Inventory;
import com.quickcommerce.inventory_service.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private InventoryRepository inventoryRepository;

    @PostMapping
    public Inventory saveInventory(@RequestBody Inventory inventory) {
        return inventoryRepository.save(inventory);
    }

    @GetMapping("/product/{productId}")
    public Inventory getInventory(@PathVariable Long productId) {
        return inventoryRepository
                .findById(productId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Product not found"
                        ));
    }

    @GetMapping("/check")
    public InventoryResponse checkInventory(
            @RequestParam Long productId,
            @RequestParam Integer quantity
    ) {
        Inventory inventory =
                inventoryRepository.findByProductId(productId)
                        .orElse(null);
        InventoryResponse response = new InventoryResponse();
        if (inventory != null &&
                inventory.getAvailableQuantity() >= quantity) {
            response.setInStock(true);
        } else {
            response.setInStock(false);
        }
        return response;
    }
}