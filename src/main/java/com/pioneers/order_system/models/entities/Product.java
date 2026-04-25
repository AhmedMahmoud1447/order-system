package com.pioneers.order_system.models.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private  Long id;
    private String name;
    private double price;
    private int stockQuantity;
}

