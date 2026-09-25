package com.grocify.backend.controller;

import com.grocify.backend.dto.AddToCartRequest;
import com.grocify.backend.entity.Cart;
import com.grocify.backend.entity.CartItem;
import com.grocify.backend.entity.User;
import com.grocify.backend.repository.UserRepository;
import com.grocify.backend.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;
    private final UserRepository userRepository;

    public CartController(
            CartService cartService,
            UserRepository userRepository
    ) {
        this.cartService = cartService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<Cart> getCart(
            Authentication authentication
    ) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        Cart cart = cartService.getOrCreateCart(user);

        return ResponseEntity.ok(cart);
    }

    @PostMapping
    public ResponseEntity<CartItem> addToCart(
            Authentication authentication,
            @RequestBody AddToCartRequest request
    ) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        CartItem cartItem = cartService.addToCart(
                user,
                request.getProductId(),
                request.getQuantity()
        );

        return ResponseEntity.ok(cartItem);
    }
    @DeleteMapping("/{productId}")
    public ResponseEntity<String> removeFromCart(
            Authentication authentication,
            @PathVariable Long productId
    ) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        cartService.removeFromCart(user, productId);

        return ResponseEntity.ok("Product removed from cart");
    }
    @PutMapping("/{productId}")
    public ResponseEntity<CartItem> updateQuantity(
            Authentication authentication,
            @PathVariable Long productId,
            @RequestParam Integer quantity
    ) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        CartItem cartItem = cartService.updateQuantity(
                user,
                productId,
                quantity
        );

        return ResponseEntity.ok(cartItem);
    }
}