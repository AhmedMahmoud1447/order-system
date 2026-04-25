package com.pioneers.order_system.services;

import com.pioneers.order_system.exceptions.BadRequestException;
import com.pioneers.order_system.models.dtos.orderdtos.OrderRequest;
import com.pioneers.order_system.models.dtos.orderdtos.OrderResponse;
import com.pioneers.order_system.models.entities.Order;
import com.pioneers.order_system.models.entities.Product;
import com.pioneers.order_system.models.enums.CustomerType;
import com.pioneers.order_system.models.enums.PaymentMethod;
import com.pioneers.order_system.models.mappers.OrderMapper;
import com.pioneers.order_system.repositories.OrderRepository;
import com.pioneers.order_system.repositories.ProductRepository;
import com.pioneers.order_system.services.discountstrategies.DiscountStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private OrderMapper orderMapper;
    @Mock
    private DiscountStrategy discountStrategy;

    private OrderService orderService;

    @BeforeEach
    void setUp() {
        List<DiscountStrategy> strategies = new ArrayList<>();
        strategies.add(discountStrategy);
        orderService = new OrderService(productRepository, orderRepository, orderMapper, strategies);
    }


    @DisplayName("Success: Test createOrder with valid request")
    @Test
    void createOrderWithValidRequest() {
        // 1. Arrange
        OrderRequest request = new OrderRequest("Ziad", CustomerType.VIP, PaymentMethod.CASH, List.of(10L));
        Long productId = 10L;
        Product product = new Product();
        product.setId(productId);
        product.setPrice(100.0);
        product.setStockQuantity(5);
        product.setName("Java Book");

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(discountStrategy.isApplicable(any())).thenReturn(true);
        when(discountStrategy.calculate(any())).thenReturn(10.0);

        when(orderRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(orderMapper.toResponse(any())).thenReturn(new OrderResponse(1L, "Ziad", 100.0, 10.0, 90.0));

        // 2. Act
        orderService.createOrder(request);

        // 3. Assert

        verify(orderRepository).save(orderCaptor.capture());
        Order savedOrder = orderCaptor.getValue();

        assertEquals(100.0, savedOrder.getTotalPrice());
        assertEquals(10.0, savedOrder.getDiscountAmount());
        assertEquals(4, product.getStockQuantity());
        verify(discountStrategy, times(1)).calculate(any());
    }


    @DisplayName("Fail: Throw Exception when Product is Out of Stock")
    @Test
    void throwExceptionWhenProductIsOutOfStock() {
        //Arrange
        OrderRequest request = new OrderRequest("Ziad", CustomerType.VIP, PaymentMethod.CASH, List.of(10L));
        Long productId = 10L;
        Product outOfStockProduct = new Product();
        outOfStockProduct.setStockQuantity(0); // خلصان
        outOfStockProduct.setId(productId);

        when(productRepository.findById(productId)).thenReturn(Optional.of(outOfStockProduct));


        //Act and Assert
        assertThrows(BadRequestException.class, () -> orderService.createOrder(request));
        verify(orderRepository, never()).save(any());
    }
}


