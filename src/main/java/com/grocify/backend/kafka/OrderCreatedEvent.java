package com.grocify.backend.kafka;

import java.time.LocalDateTime;

public class OrderCreatedEvent {

    private Long orderId;
    private Long userId;
    private Double totalAmount;
    private String status;
    private LocalDateTime createdAt;
    private String userEmail;

    public OrderCreatedEvent() {
    }

    public OrderCreatedEvent(
            Long orderId,
            Long userId,
            String userEmail,
            Double totalAmount,
            String status,
            LocalDateTime createdAt
    ) {
        this.orderId = orderId;
        this.userId = userId;
        this.userEmail = userEmail;
        this.totalAmount = totalAmount;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}