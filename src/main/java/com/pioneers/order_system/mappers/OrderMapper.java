package com.pioneers.order_system.mappers;

import com.pioneers.order_system.dtos.orderdtos.OrderItemRequest;
import com.pioneers.order_system.dtos.orderdtos.OrderResponse;
import com.pioneers.order_system.entities.Order;
import com.pioneers.order_system.entities.OrderItem;
import com.pioneers.order_system.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Mapper class responsible for converting Order entities to OrderResponse DTOs. It provides a method to transform the Order entity into a format suitable for API responses, including calculating the final price after applying discounts.
 */
@Component
@RequiredArgsConstructor
public class OrderMapper {

    private final ProductRepository productRepository;

    public OrderResponse toResponse(Order entity) {
        return OrderResponse.builder()
                .id(entity.getId())
                .customerName(entity.getCustomerName())
                .totalPrice(entity.getTotalPrice())
                .discountAmount(entity.getDiscountAmount())
                .finalPrice(entity.getTotalPrice() - entity.getDiscountAmount())
                .build();
    }

    public OrderItem toOrderItem(OrderItemRequest orderItemRequest) {
        var product = productRepository.findById(orderItemRequest.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + orderItemRequest.getProductId()));
        return OrderItem.builder()
                .product(product)
                .quantity(orderItemRequest.getQuantity())
                .build();
    }
}