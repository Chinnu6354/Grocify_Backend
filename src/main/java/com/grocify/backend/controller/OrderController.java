package com.grocify.backend.controller;

import java.util.List;

import com.grocify.backend.entity.Order;
import com.grocify.backend.entity.User;
import com.grocify.backend.repository.UserRepository;
import com.grocify.backend.service.OrderService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final UserRepository userRepository;

    public OrderController(
            OrderService orderService,
            UserRepository userRepository
    ) {
        this.orderService = orderService;
        this.userRepository = userRepository;
    }

    private User getAdmin(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        if (!"ADMIN".equals(user.getRole())) {
            throw new RuntimeException(
                    "Access denied. Admin only."
            );
        }

        return user;
    }


    @PostMapping
    public ResponseEntity<Order> placeOrder(

            @RequestParam String email,

            @RequestParam String deliveryName,
            @RequestParam String deliveryPhone,
            @RequestParam String deliveryAddress,
            @RequestParam String deliveryCity,
            @RequestParam String deliveryPincode,
            @RequestParam String paymentMethod

    ) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        Order order = orderService.placeOrder(
                user,
                deliveryName,
                deliveryPhone,
                deliveryAddress,
                deliveryCity,
                deliveryPincode,
                paymentMethod
        );

        return ResponseEntity.ok(order);
    }


    @GetMapping
    public ResponseEntity<List<Order>> getOrders(
            @RequestParam String email
    ) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        List<Order> orders =
                orderService.getOrders(user);

        return ResponseEntity.ok(orders);
    }

    @GetMapping("/admin")
    public ResponseEntity<List<Order>> getAllOrders(
            @RequestParam String email
    ) {

        getAdmin(email);

        List<Order> orders =
                orderService.getAllOrders();

        return ResponseEntity.ok(orders);
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<Order> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam String status,
            @RequestParam String email
    ) {

        getAdmin(email);

        Order order =
                orderService.updateOrderStatus(
                        orderId,
                        status
                );

        return ResponseEntity.ok(order);
    }
    @PutMapping("/{orderId}/cancel")
    public Order cancelOrder(
            @PathVariable Long orderId,
            @RequestParam String email
    ) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return orderService.cancelOrder(user, orderId);
    }
}