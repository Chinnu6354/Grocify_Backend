package com.grocify.backend.service;
import java.util.List;
import com.grocify.backend.entity.Cart;
import com.grocify.backend.entity.CartItem;
import com.grocify.backend.entity.Order;
import com.grocify.backend.entity.OrderItem;
import com.grocify.backend.entity.User;
import com.grocify.backend.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartService cartService;

    public OrderService(
            OrderRepository orderRepository,
            CartService cartService
    ) {
        this.orderRepository = orderRepository;
        this.cartService = cartService;
    }

    public Order placeOrder(
            User user,
            String deliveryName,
            String deliveryPhone,
            String deliveryAddress,
            String deliveryCity,
            String deliveryPincode,
            String paymentMethod
    ) {

        Cart cart = cartService.getOrCreateCart(user);

        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        Order order = new Order();

        order.setUser(user);
        order.setStatus("PLACED");
        order.setCreatedAt(LocalDateTime.now());

        // Delivery details
        order.setDeliveryName(deliveryName);
        order.setDeliveryPhone(deliveryPhone);
        order.setDeliveryAddress(deliveryAddress);
        order.setDeliveryCity(deliveryCity);
        order.setDeliveryPincode(deliveryPincode);
        order.setPaymentMethod(paymentMethod);

        double totalAmount = 0;

        for (CartItem cartItem : cart.getItems()) {

            OrderItem orderItem = new OrderItem();

            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getProduct().getPrice());

            order.getItems().add(orderItem);

            totalAmount +=
                    cartItem.getProduct().getPrice()
                            * cartItem.getQuantity();
        }

        order.setTotalAmount(totalAmount);

        Order savedOrder = orderRepository.save(order);

        cartService.clearCart(user);

        return savedOrder;
    }
    public List<Order> getOrders(User user) {
        return orderRepository.findByUser(user);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order cancelOrder(User user, Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new RuntimeException("Order not found")
                );

        if (!order.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You cannot cancel this order");
        }

        if (!order.getStatus().equals("PLACED")) {
            throw new RuntimeException(
                    "Only placed orders can be cancelled"
            );
        }

        order.setStatus("CANCELLED");

        return orderRepository.save(order);
    }

    public Order updateOrderStatus(Long orderId, String newStatus) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new RuntimeException("Order not found")
                );

        String currentStatus = order.getStatus();

        // PLACED → CONFIRMED
        if (currentStatus.equals("PLACED")
                && newStatus.equals("CONFIRMED")) {

            order.setStatus("CONFIRMED");
        }

        // CONFIRMED → OUT_FOR_DELIVERY
        else if (currentStatus.equals("CONFIRMED")
                && newStatus.equals("OUT_FOR_DELIVERY")) {

            order.setStatus("OUT_FOR_DELIVERY");
        }

        // OUT_FOR_DELIVERY → DELIVERED
        else if (currentStatus.equals("OUT_FOR_DELIVERY")
                && newStatus.equals("DELIVERED")) {

            order.setStatus("DELIVERED");
        }

        // PLACED → CANCELLED
        else if (currentStatus.equals("PLACED")
                && newStatus.equals("CANCELLED")) {

            order.setStatus("CANCELLED");
        }

        else {
            throw new RuntimeException(
                    "Invalid order status change: "
                            + currentStatus
                            + " → "
                            + newStatus
            );
        }

        return orderRepository.save(order);
    }
}