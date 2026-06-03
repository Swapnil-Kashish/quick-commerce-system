package com.quickcommerce.cart_service.repository;

import com.quickcommerce.cart_service.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository
        extends JpaRepository<CartItem, Long> {
}