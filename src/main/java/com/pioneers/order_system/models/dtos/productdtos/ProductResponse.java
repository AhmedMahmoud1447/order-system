package com.pioneers.order_system.models.dtos.productdtos;

import lombok.Value;

@Value
public class ProductResponse {
    Long id;
    String name;
    double price;
    int stockQuantity;
}
