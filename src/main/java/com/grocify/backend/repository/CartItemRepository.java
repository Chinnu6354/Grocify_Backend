package com.grocify.backend.repository;

import com.grocify.backend.entity.Cart;
import com.grocify.backend.entity.CartItem;
import com.grocify.backend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);

    @Modifying
    @Query("DELETE FROM CartItem c WHERE c.cart = :cart")
    void deleteByCart(@Param("cart") Cart cart);
}