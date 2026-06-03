package com.quickcommerce.cart_service.service;

import com.quickcommerce.cart_service.client.ProductClient;
import com.quickcommerce.cart_service.dto.AddToCartRequest;
import com.quickcommerce.cart_service.dto.CartItemResponse;
import com.quickcommerce.cart_service.dto.CartResponse;
import com.quickcommerce.cart_service.dto.ProductResponse;
import com.quickcommerce.cart_service.entity.Cart;
import com.quickcommerce.cart_service.entity.CartItem;
import com.quickcommerce.cart_service.repository.CartItemRepository;
import com.quickcommerce.cart_service.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductClient productClient;

    public CartResponse addToCart(
            AddToCartRequest request
    ) {

        ProductResponse product =
                productClient.getProduct(
                        request.getProductId()
                );

        Cart cart =
                cartRepository.findByUserId(
                        request.getUserId()
                ).orElseGet(() -> {

                    Cart newCart = new Cart();
                    newCart.setUserId(
                            request.getUserId()
                    );

                    return cartRepository.save(
                            newCart
                    );
                });

        CartItem existingItem =
                cart.getItems()
                        .stream()
                        .filter(item ->
                                item.getProductId()
                                        .equals(
                                                request.getProductId()
                                        ))
                        .findFirst()
                        .orElse(null);

        if (existingItem != null) {

            existingItem.setQuantity(
                    existingItem.getQuantity()
                            + request.getQuantity()
            );

            cartItemRepository.save(
                    existingItem
            );

        } else {

            CartItem item =
                    new CartItem();

            item.setProductId(
                    product.getId()
            );

            item.setQuantity(
                    request.getQuantity()
            );

            item.setPrice(
                    product.getPrice()
            );

            item.setCart(
                    cart
            );

            cart.getItems().add(
                    item
            );

            cartItemRepository.save(
                    item
            );
        }

        Cart savedCart =
                cartRepository.save(
                        cart
                );

        return mapToResponse(
                savedCart
        );
    }

    public CartResponse getCart(
            Long userId
    ) {

        Cart cart =
                cartRepository
                        .findByUserId(userId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Cart not found"
                                ));

        return mapToResponse(
                cart
        );
    }

    public void removeItem(
            Long itemId
    ) {

        cartItemRepository.deleteById(
                itemId
        );
    }

    public void clearCart(
            Long userId
    ) {

        Cart cart =
                cartRepository
                        .findByUserId(userId)
                        .orElseThrow();

        cart.getItems().clear();

        cartRepository.save(cart);
    }

    private CartResponse mapToResponse(
            Cart cart
    ) {

        List<CartItemResponse> items =
                cart.getItems()
                        .stream()
                        .map(item ->
                                new CartItemResponse(
                                        item.getId(),
                                        item.getProductId(),
                                        item.getQuantity(),
                                        item.getPrice()
                                ))
                        .toList();

        double totalAmount =
                cart.getItems()
                        .stream()
                        .mapToDouble(item ->
                                item.getPrice()
                                        * item.getQuantity())
                        .sum();

        return new CartResponse(
                cart.getId(),
                cart.getUserId(),
                totalAmount,
                items
        );
    }

}