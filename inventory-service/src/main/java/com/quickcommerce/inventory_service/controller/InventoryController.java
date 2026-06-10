package com.quickcommerce.inventory_service.controller;

import com.quickcommerce.inventory_service.dto.InventoryRequest;
import com.quickcommerce.inventory_service.dto.InventoryResponse;
import com.quickcommerce.inventory_service.entity.Inventory;
import com.quickcommerce.inventory_service.repository.InventoryRepository;
import com.quickcommerce.inventory_service.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private InventoryService inventoryService;

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

    @PostMapping("/reserve")
    public String reserveStock(
            @RequestBody InventoryRequest request
    ) {

        inventoryService.reserveStock(
                request.getProductId(),
                request.getQuantity()
        );
        return "Stock Reserved";
    }

    @PostMapping("/release")
    public String releaseStock(
            @RequestBody InventoryRequest request
    ) {

        inventoryService.releaseStock(
                request.getProductId(),
                request.getQuantity()
        );

        return "Stock Released";
    }
}