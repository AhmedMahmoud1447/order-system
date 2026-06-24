package com.pioneers.order_system.dtos.orderdtos;

import lombok.Data;

import java.util.List;
@Data
public class OrderRequest {
    private Long customerId;
    private List<OrderItemRequest> items;
}

