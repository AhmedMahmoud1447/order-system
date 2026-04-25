package com.pioneers.order_system.models.entities;

import lombok.Data;

@Data
public class Product {
    private  Long id;
    private String name;
    private double price;
    private int stockQuantity;
}

