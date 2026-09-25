package com.grocify.backend.repository;

import com.grocify.backend.entity.User;
import com.grocify.backend.entity.Product;
import com.grocify.backend.entity.WishlistItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WishlistItemRepository
        extends JpaRepository<WishlistItem, Long> {

    List<WishlistItem> findByUser(User user);

    Optional<WishlistItem> findByUserAndProduct(
            User user,
            Product product
    );

    void deleteByUserAndProduct(
            User user,
            Product product
    );
}