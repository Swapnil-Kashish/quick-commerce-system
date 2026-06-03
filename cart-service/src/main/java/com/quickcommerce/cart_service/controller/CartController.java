package com.quickcommerce.cart_service.controller;

import com.quickcommerce.cart_service.dto.AddToCartRequest;
import com.quickcommerce.cart_service.dto.CartResponse;
import com.quickcommerce.cart_service.entity.Cart;
import com.quickcommerce.cart_service.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/add")
    public CartResponse addToCart(
            @RequestBody AddToCartRequest request
    ) {

        return cartService.addToCart(
                request
        );
    }

    @GetMapping("/{userId}")
    public CartResponse getCart(
            @PathVariable Long userId
    ) {

        return cartService.getCart(
                userId
        );
    }

    @DeleteMapping("/item/{itemId}")
    public void removeItem(
            @PathVariable Long itemId
    ) {

        cartService.removeItem(
                itemId
        );
    }

    @DeleteMapping("/clear/{userId}")
    public void clearCart(
            @PathVariable Long userId
    ) {

        cartService.clearCart(
                userId
        );
    }
}