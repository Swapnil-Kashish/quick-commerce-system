package com.quickcommerce.order_service.webclient;

import io.github.resilience4j.retry.annotation.Retry;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@AllArgsConstructor
@Service
public class InventoryClient {
    private RestTemplate restTemplate;
    @Retry(name = "inventoryService")
    public Boolean checkInventory(String url) {
        System.out.println("🔁 Retry layer calling inventory...");
        return restTemplate.getForObject(url, Boolean.class);
    }
}