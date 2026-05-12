package com.quickcommerce.order_service.client;

import com.quickcommerce.order_service.dto.InventoryResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "INVENTORY-SERVICE")
public interface InventoryClient {

    @GetMapping("/inventory/check")
    InventoryResponse checkInventory(
            @RequestParam Long productId,
            @RequestParam Integer quantity
    );
}