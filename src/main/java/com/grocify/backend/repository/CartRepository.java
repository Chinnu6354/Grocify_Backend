package com.grocify.backend.repository;

import com.grocify.backend.entity.Cart;
import com.grocify.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByUser(User user);
}