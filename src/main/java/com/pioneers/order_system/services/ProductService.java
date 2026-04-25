package com.pioneers.order_system.services;

import com.pioneers.order_system.exceptions.ResourceNotFoundException;
import com.pioneers.order_system.models.dtos.productdtos.ProductRequest;
import com.pioneers.order_system.models.dtos.productdtos.ProductResponse;
import com.pioneers.order_system.models.entities.Product;
import com.pioneers.order_system.models.mappers.ProductMapper;
import com.pioneers.order_system.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.pioneers.order_system.models.mappers.ProductMapper.*;
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;


    public Product createProduct(ProductRequest  productRequest) {
        Product product = toEntity(productRequest);
        log.info("Creating product: {}", product);
        return productRepository.save(product);
    }

    public ProductResponse getProductById(Long id) {
        log.info("Getting product by id: {}", id);
        return productRepository.
                findById(id)
                .map(ProductMapper::toResponseDto)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    public List<ProductResponse> getLowStockProducts(int value){
        log.info("Getting low stock products for value {}", value);
      return productRepository.findAll()
               .stream()
               .filter(product -> product.getStockQuantity() <= value)
               .map(ProductMapper::toResponseDto)
               .toList();
    }




}
