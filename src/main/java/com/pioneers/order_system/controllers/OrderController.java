package com.pioneers.order_system.controllers;

import com.pioneers.order_system.dtos.orderdtos.OrderRequest;
import com.pioneers.order_system.dtos.orderdtos.OrderResponse;
import com.pioneers.order_system.services.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('CUSTOMER')") // 👈 قفل: الزبون فقط هو من يصنع أوردر
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderRequest request) {
        OrderResponse response = orderService.createOrder(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}") // 👈 تعديل بسيط: أضفنا السلاش النواقص / لضمان دقة الرابط
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')") // 👈 قفل: الزبون يشوف أوردره، والأدمن يتابع الأوردرات
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable long id) {
        OrderResponse response = orderService.findOrderById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
