package com.pioneers.order_system.services;

import com.pioneers.order_system.exceptions.BadRequestException;
import com.pioneers.order_system.exceptions.ResourceNotFoundException;
import com.pioneers.order_system.models.dtos.orderdtos.OrderRequest;
import com.pioneers.order_system.models.dtos.orderdtos.OrderResponse;
import com.pioneers.order_system.models.entities.Order;
import com.pioneers.order_system.models.entities.Product;
import com.pioneers.order_system.models.mappers.OrderMapper;
import com.pioneers.order_system.repositories.OrderRepository;
import com.pioneers.order_system.repositories.ProductRepository;
import com.pioneers.order_system.services.discountstrategies.DiscountStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import static com.pioneers.order_system.utils.Payment.processPayment;
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final AtomicLong idGenerator = new AtomicLong(1);
    private final List<DiscountStrategy> discountStrategies;

    /**
     * Creates a new order based on the provided OrderRequest. It processes the order, applies discounts, and saves it to the repository.
     * @param request dto
     * @return the response dto
     */
    public OrderResponse createOrder(OrderRequest request) {
        log.info("Creating a new order for customer: {}", request.customerName());
        Order savedOrder = processAndSaveOrder(request);
        log.info("Order created successfully with ID: {}", savedOrder.getId());
        return orderMapper.toResponse(savedOrder);
    }

    private Order processAndSaveOrder(OrderRequest request) {
        log.info("Starting to process order for customer: {}", request.customerName());
        List<Product> productsToOrder = request.productIds().stream()
                .map(productId -> productRepository.findById(productId)
                        .orElseThrow(() -> {
                            log.warn("Order failed: Product ID {} not found", productId);
                           return new ResourceNotFoundException("product with id: " + productId + " is not found!");
                        }))
                .peek(this::updateProductStock)
                .collect(Collectors.toList());

        double rawTotalPrice = productsToOrder.stream()
                .mapToDouble(Product::getPrice)
                .sum();
        log.debug("Raw total price calculated: {} for {} items", rawTotalPrice, productsToOrder.size());

        Order newOrder = new Order();
        newOrder.setId(idGenerator.getAndIncrement());
        newOrder.setCustomerName(request.customerName());
        newOrder.setCustomerType(request.customerType());
        newOrder.setPaymentMethod(request.paymentMethod());
        newOrder.setOrderedItems(productsToOrder);
        newOrder.setTotalPrice(rawTotalPrice);

        log.debug("Applying discount strategies for order ID: {}", newOrder.getId());
        double totalDiscount = discountStrategies.stream()
                .filter(strategy -> strategy.isApplicable(newOrder))
                .mapToDouble(strategy -> strategy.calculate(newOrder))
                .sum();

        newOrder.setDiscountAmount(totalDiscount);
        log.info("Total discount applied: {}", totalDiscount);

        processPayment(newOrder);
        log.info("Payment processed using: {}", newOrder.getPaymentMethod());

        return orderRepository.save(newOrder);
    }

    private void updateProductStock(Product product) {
        if (product.getStockQuantity() <= 0) {
            log.warn("Stock validation failed for product: {}. Current stock: {}", product.getName(), product.getStockQuantity());
            throw new BadRequestException("The product " + product.getName() + " is out of stock!");
        }
        log.debug("Stock updated for product: {}. Remaining: {}", product.getName(), product.getStockQuantity());
        product.setStockQuantity(product.getStockQuantity() - 1);
    }

    public OrderResponse getOrderById(Long id) {
        log.info("Fetching order with ID: {}", id);
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Order lookup failed: ID {} not found", id);
                    return new RuntimeException("Order not found with id: " + id);
                });

        return orderMapper.toResponse(order);
    }
}

