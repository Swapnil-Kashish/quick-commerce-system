package com.quickcommerce.cart_service.client;

import com.quickcommerce.cart_service.dto.InventoryRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "inventory-service")
public interface InventoryClient {

    @PostMapping("/inventory/reserve")
    String reserveStock(
            @RequestBody InventoryRequest request
    );

    @PostMapping("/inventory/release")
    String releaseStock(
            @RequestBody InventoryRequest request
    );
}