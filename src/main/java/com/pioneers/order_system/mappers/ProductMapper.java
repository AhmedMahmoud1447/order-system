package com.pioneers.order_system.mappers;

import com.pioneers.order_system.dtos.productdtos.ProductRequest;
import com.pioneers.order_system.dtos.productdtos.ProductResponse;
import com.pioneers.order_system.entities.Product;

/**
    * The ProductMapper class provides static methods to convert between Product entities and their corresponding DTOs (Data Transfer Objects).
 */
public class ProductMapper {

    public static Product toProduct(ProductRequest productRequest) {
        Product product = new Product();
        // Only set ID if it's not null (for updates), otherwise let DB generate it
        if (productRequest.getId() != null && productRequest.getId() > 0) {
            product.setId(productRequest.getId());
        }
        product.setName(productRequest.getName());
        product.setPrice(productRequest.getPrice());
        product.setStockQuantity(productRequest.getStockQuantity());
        return product;
    }
    public static ProductRequest toRequestDto(Product product) {
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
