package com.grocify.backend.service;

import com.grocify.backend.entity.Cart;
import com.grocify.backend.entity.CartItem;
import com.grocify.backend.entity.Product;
import com.grocify.backend.entity.User;
import com.grocify.backend.repository.CartItemRepository;
import com.grocify.backend.repository.CartRepository;
import com.grocify.backend.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    public CartService(
            CartRepository cartRepository,
            CartItemRepository cartItemRepository,
            ProductRepository productRepository
    ) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
    }

    public Cart getOrCreateCart(User user) {

        return cartRepository.findByUser(user)
                .orElseGet(() -> {

                    Cart cart = new Cart();
                    cart.setUser(user);

                    return cartRepository.save(cart);
                });
    }

    public CartItem addToCart(
            User user,
            Long productId,
            Integer quantity
    ) {

        // 1. Get or create user's cart
        Cart cart = getOrCreateCart(user);

        // 2. Find product
        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new RuntimeException("Product not found")
                );

        // 3. Check whether product already exists in cart
        CartItem cartItem =
                cartItemRepository
                        .findByCartAndProduct(cart, product)
                        .orElse(null);

        // 4. If product already exists, increase quantity
        if (cartItem != null) {

            cartItem.setQuantity(
                    cartItem.getQuantity() + quantity
            );

        } else {

            // 5. Otherwise create new cart item
            cartItem = new CartItem();

            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
        }

        // 6. Save cart item
        return cartItemRepository.save(cartItem);
    }

    public void removeFromCart(User user, Long productId) {

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() ->
                        new RuntimeException("Cart not found")
                );

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new RuntimeException("Product not found")
                );

        CartItem cartItem = cartItemRepository
                .findByCartAndProduct(cart, product)
                .orElseThrow(() ->
                        new RuntimeException("Product not found in cart")
                );

        cartItemRepository.delete(cartItem);
    }
    public CartItem updateQuantity(
            User user,
            Long productId,
            Integer quantity
    ) {

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() ->
                        new RuntimeException("Cart not found")
                );

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new RuntimeException("Product not found")
                );

        CartItem cartItem = cartItemRepository
                .findByCartAndProduct(cart, product)
                .orElseThrow(() ->
                        new RuntimeException("Product not found in cart")
                );

        if (quantity <= 0) {
            cartItemRepository.delete(cartItem);
            return null;
        }

        cartItem.setQuantity(quantity);

        return cartItemRepository.save(cartItem);
    }

    @Transactional
    public void clearCart(User user) {

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() ->
                        new RuntimeException("Cart not found")
                );

        cartItemRepository.deleteByCart(cart);
    }
}