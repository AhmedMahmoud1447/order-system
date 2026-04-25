package com.pioneers.order_system.models.mappers;

import com.pioneers.order_system.models.dtos.orderdtos.OrderResponse;
import com.pioneers.order_system.models.entities.Order;
import org.springframework.stereotype.Component;

/**
 * Mapper class responsible for converting Order entities to OrderResponse DTOs. It provides a method to transform the Order entity into a format suitable for API responses, including calculating the final price after applying discounts.
 */
@Component
public class OrderMapper {

    public OrderResponse toResponse(Order entity) {
        return OrderResponse.builder()
                .id(entity.getId())
                .customerName(entity.getCustomerName())
                .totalPrice(entity.getTotalPrice())
                .discountAmount(entity.getDiscountAmount())
                .finalPrice(entity.getTotalPrice() - entity.getDiscountAmount())
                .build();
    }
}