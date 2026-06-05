package com.pioneers.order_system.dtos.orderdtos;
import lombok.Builder;

@Builder
 public record OrderResponse (
      Long id,
      String customerName,
      double totalPrice,
      double discountAmount,
      double finalPrice
){}