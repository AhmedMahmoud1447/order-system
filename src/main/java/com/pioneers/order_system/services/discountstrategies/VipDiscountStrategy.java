package com.pioneers.order_system.services.discountstrategies;

import com.pioneers.order_system.entities.Order;
import com.pioneers.order_system.enums.CustomerType;
import org.springframework.stereotype.Component;

/**
 * VipDiscountStrategy applies a 10% discount for VIP customers.
 */
@Component
public class VipDiscountStrategy implements DiscountStrategy {
    @Override
    public double calculate(Order order) {
        return order.getTotalPrice() * 0.10;
    }

    @Override
    public boolean isApplicable(Order order) {
        return order.getCustomerType() == CustomerType.VIP;
    }
}
