package com.pioneers.order_system.payment;

import com.pioneers.order_system.entities.Order;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Payment {

    /**
     * Simulates payment processing for an order. In a real application, this would integrate with a payment gateway.
     * @param order entity
     */
    public static void processPayment(Order order) {
        System.out.println("Processing payment for Order ID: " + order.getId() +
                " via " + order.getPaymentMethod());
    }
}
