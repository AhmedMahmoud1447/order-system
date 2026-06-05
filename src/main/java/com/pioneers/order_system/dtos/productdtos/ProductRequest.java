package com.pioneers.order_system.dtos.productdtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Value;

import java.math.BigDecimal;

@Value
public class ProductRequest {
    Long id;
    @NotBlank(message = "Product name cannot be empty")
    @Size(min = 2, max = 100, message = "Product name must be between 2 and 100 characters")
    String name;
    @Positive(message = "Price must be greater than zero")
    double price;
    @Min(value = 0, message = "Stock quantity cannot be less than zero")
    int stockQuantity;
}
