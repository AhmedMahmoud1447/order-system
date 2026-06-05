package com.pioneers.order_system.services;

import com.pioneers.order_system.errors.exceptions.BadRequestException;
import com.pioneers.order_system.errors.exceptions.ResourceNotFoundException;
import com.pioneers.order_system.dtos.orderdtos.OrderRequest;
import com.pioneers.order_system.dtos.orderdtos.OrderResponse;
import com.pioneers.order_system.entities.Order;
import com.pioneers.order_system.entities.OrderItem;
import com.pioneers.order_system.entities.Product;
import com.pioneers.order_system.mappers.OrderMapper;
import com.pioneers.order_system.repositories.OrderRepository;
import com.pioneers.order_system.repositories.ProductRepository;
import com.pioneers.order_system.services.discountstrategies.DiscountStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.pioneers.order_system.payment.Payment.processPayment;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final List<DiscountStrategy> discountStrategies;

    @Transactional
    public OrderResponse createOrder(OrderRequest request) {
        log.info("Creating a new order for customer with id: {}", request.getCustomerId());
        Order savedOrder = processAndSaveOrder(request);
        log.info("Order created successfully with ID: {}", savedOrder.getId());
        return orderMapper.toResponse(savedOrder);
    }

    private Order processAndSaveOrder(OrderRequest request) {
        log.info("Starting to process order for customer with id: {}", request.getCustomerId());

        Order newOrder = new Order();
        double rawTotalPrice = 0.0;
        List<Product> productsToUpdate = new ArrayList<>();

        // نقوم بالمرور على الـ Items المرسلة في الـ Request
        for (var itemRequest : request.getItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> {
                        log.warn("Order failed: Product ID {} not found", itemRequest.getProductId());
                        return new ResourceNotFoundException("Product with id: " + itemRequest.getProductId() + " is not found!");
                    });

            // 1. التحقق من المخزن بناءً على الكمية المطلوبة فعلياً
            validateAndCalculateStock(product, itemRequest.getQuantity());

            // 2. إنشاء كائن الـ OrderItem وتثبيت السعر الحالي
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setPriceAtPurchase(product.getPrice()); // السعر الحالي حُمي تماماً هنا

            // 3. ربط الـ OrderItem بالـ Order باستخدام الـ Helper Method
            newOrder.addOrderItem(orderItem);

            // 4. حساب السعر الإجمالي الكلي (السعر * الكمية)
            rawTotalPrice += product.getPrice() * itemRequest.getQuantity();

            productsToUpdate.add(product);
        }

        newOrder.setTotalPrice(rawTotalPrice);
        log.debug("Raw total price calculated: {}", rawTotalPrice);

        // 5. تطبيق الـ Strategy Pattern للخصومات
        log.debug("Applying discount strategies for order");
        double totalDiscount = discountStrategies.stream()
                .filter(strategy -> strategy.isApplicable(newOrder))
                .mapToDouble(strategy -> strategy.calculate(newOrder))
                .sum();

        newOrder.setDiscountAmount(totalDiscount);
        log.info("Total discount applied: {}", totalDiscount);

        // 6. معالجة الدفع وحفظ البيانات دفعة واحدة (Batch Update للمنتجات)
        processPayment(newOrder);
        productRepository.saveAll(productsToUpdate); // حفظ جماعي أسرع بمليون مرة في الأداء من الـ Loop

        return orderRepository.save(newOrder);
    }

    private void validateAndCalculateStock(Product product, int requestedQuantity) {
        if (product.getStockQuantity() < requestedQuantity) {
            log.warn("Stock validation failed for product: {}. Current stock: {}, Requested: {}",
                    product.getName(), product.getStockQuantity(), requestedQuantity);
            throw new BadRequestException("The product " + product.getName() + " does not have enough stock!");
        }
        product.setStockQuantity(product.getStockQuantity() - requestedQuantity);
    }

    public OrderResponse findOrderById(long id) {
        log.debug("Fetching order with ID: {}", id);
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Order lookup failed: ID {} not found", id);
                    return new ResourceNotFoundException("Order not found with id: " + id);
                });
        return orderMapper.toResponse(order);
    }
}