package com.grocify.backend.repository;

import com.grocify.backend.entity.Order;
import com.grocify.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUser(User user);
}