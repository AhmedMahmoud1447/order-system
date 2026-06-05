package com.pioneers.order_system.services.discountstrategies;

import com.pioneers.order_system.entities.Order;
import org.springframework.stereotype.Component;

/**
 * BulkDiscountStrategy applies a 5% discount if the order contains 10 or more items.
 */
@Component
public class BulkDiscountStrategy implements DiscountStrategy {
    @Override
    public double calculate(Order order) {
        return order.getTotalPrice() * 0.05;
    }

    @Override
    public boolean isApplicable(Order order) {
        return order.getItems().size() >= 10;
    }
}
