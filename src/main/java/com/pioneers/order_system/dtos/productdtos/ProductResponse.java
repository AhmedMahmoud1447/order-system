package com.pioneers.order_system.dtos.productdtos;

import lombok.Value;

@Value
public class ProductResponse {
    Long id;
    String name;
    double price;
    int stockQuantity;
}
