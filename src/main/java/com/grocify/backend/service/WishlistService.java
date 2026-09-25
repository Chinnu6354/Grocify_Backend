package com.grocify.backend.service;

import com.grocify.backend.entity.Product;
import com.grocify.backend.entity.User;
import com.grocify.backend.entity.WishlistItem;
import com.grocify.backend.repository.ProductRepository;
import com.grocify.backend.repository.WishlistItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WishlistService {

    private final WishlistItemRepository wishlistItemRepository;
    private final ProductRepository productRepository;

    public WishlistService(
            WishlistItemRepository wishlistItemRepository,
            ProductRepository productRepository
    ) {
        this.wishlistItemRepository = wishlistItemRepository;
        this.productRepository = productRepository;
    }

    public WishlistItem addToWishlist(
            User user,
            Long productId
    ) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new RuntimeException("Product not found")
                );

        return wishlistItemRepository
                .findByUserAndProduct(user, product)
                .orElseGet(() -> {

                    WishlistItem item = new WishlistItem();

                    item.setUser(user);
                    item.setProduct(product);

                    return wishlistItemRepository.save(item);
                });
    }

    @Transactional
    public void removeFromWishlist(
            User user,
            Long productId
    ) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new RuntimeException("Product not found")
                );

        wishlistItemRepository.deleteByUserAndProduct(
                user,
                product
        );
    }

    public List<WishlistItem> getWishlist(User user) {

        return wishlistItemRepository.findByUser(user);
    }

    public boolean isInWishlist(
            User user,
            Long productId
    ) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new RuntimeException("Product not found")
                );

        return wishlistItemRepository
                .findByUserAndProduct(user, product)
                .isPresent();
    }
}