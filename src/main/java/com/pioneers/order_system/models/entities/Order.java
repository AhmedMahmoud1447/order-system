package com.pioneers.order_system.models.entities;

import com.pioneers.order_system.models.enums.CustomerType;
import com.pioneers.order_system.models.enums.PaymentMethod;
import lombok.Data;

import java.util.List;

@Data
public class Order {
    private Long id;
    private String customerName;
    private CustomerType customerType;
    private PaymentMethod paymentMethod;
    private List<Product> orderedItems;
    private double totalPrice;
    private double discountAmount;
}
