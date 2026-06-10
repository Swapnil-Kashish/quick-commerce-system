package com.quickcommerce.cart_service.service;

import com.quickcommerce.cart_service.client.InventoryClient;
import com.quickcommerce.cart_service.client.ProductClient;
import com.quickcommerce.cart_service.dto.*;
import com.quickcommerce.cart_service.entity.Cart;
import com.quickcommerce.cart_service.entity.CartItem;
import com.quickcommerce.cart_service.repository.CartItemRepository;
import com.quickcommerce.cart_service.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductClient productClient;
    private final InventoryClient inventoryClient;

    @CachePut(value = "carts", key = "#request.userId")
    public CartResponse addToCart(AddToCartRequest request) {

        ProductResponse product = productClient.getProduct(request.getProductId());

        InventoryRequest inventoryRequest =
                new InventoryRequest();

        inventoryRequest.setProductId(
                request.getProductId()
        );

        inventoryRequest.setQuantity(
                request.getQuantity()
        );

        inventoryClient.reserveStock(
                inventoryRequest
        );

        Cart cart = cartRepository.findByUserId(request.getUserId()).orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUserId(request.getUserId());

                    return cartRepository.save(newCart);
                });

        CartItem existingItem = cart.getItems().stream()
                        .filter(item -> item.getProductId().equals(request.getProductId()))
                        .findFirst()
                        .orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + request.getQuantity());
            cartItemRepository.save(existingItem);
        } else {
            CartItem item = new CartItem();
            item.setProductId(product.getId());
            item.setQuantity(request.getQuantity());
            item.setPrice(product.getPrice());
            item.setCart(cart);

            cart.getItems().add(item);
            cartItemRepository.save(item);
        }

        Cart savedCart = cartRepository.save(cart);

        return mapToResponse(savedCart);
    }

    @Cacheable(value = "carts", key = "#userId")
    public CartResponse getCart(Long userId) {
        System.out.println("🔥 DB HIT FOR CART: " + userId);
        Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("Cart not found"));

        return mapToResponse(cart);
    }

    @CacheEvict(value = "carts", allEntries = true)
    public void removeItem(Long itemId) {
        cartItemRepository.deleteById(itemId);
    }

    @CacheEvict(value = "carts", key = "#userId")
    public void clearCart(Long userId) {

        Cart cart = cartRepository.findByUserId(userId).orElseThrow();

        for (CartItem item : cart.getItems()) {

            InventoryRequest request =
                    new InventoryRequest();

            request.setProductId(
                    item.getProductId()
            );

            request.setQuantity(
                    item.getQuantity()
            );

            inventoryClient.releaseStock(
                    request
            );
        }

        cart.getItems().clear();

        cartRepository.save(cart);
    }

    private CartResponse mapToResponse(Cart cart) {

        List<CartItemResponse> items = cart.getItems().stream()
                        .map(item -> new CartItemResponse(item.getId(),
                                        item.getProductId(),
                                        item.getQuantity(),
                                        item.getPrice()
                                ))
                        .toList();
        double totalAmount = cart.getItems().stream()
                        .mapToDouble(item -> item.getPrice() * item.getQuantity())
                        .sum();

        return new CartResponse(
                cart.getId(),
                cart.getUserId(),
                totalAmount,
                items
        );
    }

}