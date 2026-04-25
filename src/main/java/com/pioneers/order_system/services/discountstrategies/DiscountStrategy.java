package com.pioneers.order_system.services.discountstrategies;

import com.pioneers.order_system.models.entities.Order;

/**
 * Interface for discount strategies. Each strategy must implement the calculate method to determine the discount amount
 */
public interface DiscountStrategy {
    double calculate(Order order);
    boolean isApplicable(Order order);
}
