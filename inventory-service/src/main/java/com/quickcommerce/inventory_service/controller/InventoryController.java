package com.quickcommerce.inventory_service.controller;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private Map<Long, Integer> stock = new HashMap<>();

    @PostMapping("/add")
    public void addStock(@RequestParam Long productId,
                         @RequestParam int quantity) {

        stock.put(productId,
                stock.getOrDefault(productId, 0) + quantity);
    }

    @GetMapping("/check")
    public boolean checkStock(@RequestParam Long productId,
                              @RequestParam int quantity) {

        return stock.getOrDefault(productId, 0) >= quantity;
    }
}