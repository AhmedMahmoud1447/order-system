package com.pioneers.order_system.repositories;

import com.pioneers.order_system.models.entities.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Slf4j
@Repository
public class OrderRepository {
    private final List<Order> orders = new ArrayList<>();

    public Order save(Order order) {
        orders.add(order);
        log.info("Order saved successfully");
        return order;
    }

    public List<Order> findAll() {
        log.info("Finding all orders");
        return new ArrayList<>(orders);
    }

    public Optional<Order> findById(Long id) {
        log.info("Finding order by id: {}", id);
        return orders.stream()
                .filter(order -> order.getId().equals(id))
                .findFirst();
    }
}
