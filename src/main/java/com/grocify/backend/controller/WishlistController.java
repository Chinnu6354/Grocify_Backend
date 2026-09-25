package com.grocify.backend.controller;

import com.grocify.backend.entity.User;
import com.grocify.backend.entity.WishlistItem;
import com.grocify.backend.repository.UserRepository;
import com.grocify.backend.service.WishlistService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {

    private final WishlistService wishlistService;
    private final UserRepository userRepository;

    public WishlistController(
            WishlistService wishlistService,
            UserRepository userRepository
    ) {
        this.wishlistService = wishlistService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<List<WishlistItem>> getWishlist(
            Authentication authentication
    ) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        return ResponseEntity.ok(
                wishlistService.getWishlist(user)
        );
    }

    @PostMapping("/{productId}")
    public ResponseEntity<WishlistItem> addToWishlist(
            Authentication authentication,
            @PathVariable Long productId
    ) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        WishlistItem item =
                wishlistService.addToWishlist(
                        user,
                        productId
                );

        return ResponseEntity.ok(item);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<String> removeFromWishlist(
            Authentication authentication,
            @PathVariable Long productId
    ) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        wishlistService.removeFromWishlist(
                user,
                productId
        );

        return ResponseEntity.ok(
                "Product removed from wishlist"
        );
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Boolean> isInWishlist(
            Authentication authentication,
            @PathVariable Long productId
    ) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        return ResponseEntity.ok(
                wishlistService.isInWishlist(
                        user,
                        productId
                )
        );
    }
}