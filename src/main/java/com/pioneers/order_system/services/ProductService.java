package com.pioneers.order_system.services;

import com.pioneers.order_system.errors.exceptions.ResourceNotFoundException;
import com.pioneers.order_system.dtos.productdtos.ProductRequest;
import com.pioneers.order_system.dtos.productdtos.ProductResponse;
import com.pioneers.order_system.entities.Product;
import com.pioneers.order_system.mappers.ProductMapper;
import com.pioneers.order_system.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.pioneers.order_system.mappers.ProductMapper.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Product addProduct(ProductRequest productRequest) {
        Product product = toProduct(productRequest);
        log.info("Creating product: {}", product);
        return productRepository.save(product);
    }

    public ProductResponse findProductById(Long id) {
        log.debug("Getting product by id: {}", id);
        return productRepository.
                findById(id)
                .map(ProductMapper::toResponseDto)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    public List<ProductResponse> getLowStockProducts(int value) {
        log.debug("Getting low stock products for value {}", value);

        return productRepository.findAll()
                .stream()
                .filter(product -> product.getStockQuantity() <= value)
                .map(ProductMapper::toResponseDto)
                .toList();
    }


}
