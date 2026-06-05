package com.pioneers.order_system.dtos.orderdtos;

import lombok.Data;

import java.util.List;
@Data
// داخل OrderRequest.java
public class OrderRequest {
    private Long customerId; // أو الـ Type بناءً على التصميم
    private List<OrderItemRequest> items;
}

