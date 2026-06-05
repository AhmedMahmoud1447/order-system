package com.pioneers.order_system.entities;

import com.pioneers.order_system.enums.CustomerType;
import com.pioneers.order_system.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders") // ضروري جداً عشان اسم الجمع والكلمة المحجوزة
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerName;

    @Enumerated(EnumType.STRING) // عشان يتسجل كـ String في الداتابيز
    private CustomerType customerType;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    private double totalPrice;
    private double discountAmount;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    public void addOrderItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
    }
}