package com.pioneers.order_system.models.dtos.orderdtos;
import lombok.Builder;
import lombok.Data;

@Builder
 public record OrderResponse (
      Long id,
      String customerName,
      double totalPrice,
      double discountAmount,
      double finalPrice
){}