package com.pioneers.order_system.models.mappers;

import com.pioneers.order_system.models.dtos.productdtos.ProductRequest;
import com.pioneers.order_system.models.dtos.productdtos.ProductResponse;
import com.pioneers.order_system.models.entities.Product;
import lombok.extern.slf4j.Slf4j;

/**
    * The ProductMapper class provides static methods to convert between Product entities and their corresponding DTOs (Data Transfer Objects).
 */
public class ProductMapper {

    public static Product toEntity(ProductRequest productRequest) {
        Product product = new Product();
        product.setId(productRequest.getId());
        product.setName(productRequest.getName());
        product.setPrice(productRequest.getPrice());
        product.setStockQuantity(productRequest.getStockQuantity());
        return product;
    }
    public static ProductRequest toRequsetDto(Product product) {
        return new ProductRequest(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getStockQuantity()
        );
    }

    public static ProductResponse toResponseDto(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getStockQuantity()
        );
    }





}
