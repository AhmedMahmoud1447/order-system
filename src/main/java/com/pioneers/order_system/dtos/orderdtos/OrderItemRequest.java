package com.pioneers.order_system.dtos.orderdtos;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class OrderItemRequest {
    private Long productId;
    private Integer quantity;
}