package com.pioneers.order_system.services;

import com.pioneers.order_system.errors.exceptions.ResourceNotFoundException;
import com.pioneers.order_system.dtos.productdtos.ProductRequest;
import com.pioneers.order_system.entities.Product;
import com.pioneers.order_system.repositories.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.pioneers.order_system.mappers.ProductMapper.toProduct;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Nested
    @DisplayName("Positive Tests")
    class PositiveTests {

        @Test
        void should_CreateProduct() {
            ProductRequest productRequest = new ProductRequest(1L,"Test Product", 10.0, 100);

            when(productRepository.save(any())).thenReturn(toProduct(productRequest));

            Product product = productService.addProduct(productRequest);

            assertNotNull(product);
            assertEquals(productRequest.getName(), product.getName());
            assertEquals(productRequest.getPrice(), product.getPrice());
            assertEquals(productRequest.getStockQuantity(), product.getStockQuantity());
        }

        @Test
        void shouldgGetProductById() {
            Long id = 1L;
            ProductRequest productRequest = new ProductRequest(id,"Test Product", 10.0, 100);
            when(productRepository.findById(id)).thenReturn(java.util.Optional.of(toProduct(productRequest)));
            var productResponse = productService.findProductById(id);
            assertNotNull(productResponse);
            assertEquals(productRequest.getName(), productResponse.getName());
            assertEquals(productRequest.getPrice(), productResponse.getPrice());
            assertEquals(productRequest.getStockQuantity(), productResponse.getStockQuantity());
        }

        @Test
        void shouldGetLowStockProducts() {
            int value = 500;
            Product productRequest = new Product(1L,"TestProduct1",100,50);
            Product productRequest2 = new Product(2L,"TestProduct2",100,600);
            when(productRepository.findAll()).thenReturn(List.of(productRequest, productRequest2));
            var lowStockProducts = productService.getLowStockProducts(value);
            assertNotNull(lowStockProducts);
            assertFalse(lowStockProducts.isEmpty());
            assertEquals(1, lowStockProducts.size());
            assertEquals("TestProduct1", lowStockProducts.get(0).getName());

        }

        @DisplayName("Negative Tests")
        @Nested
        class NegativeTests {
            @Test
            void should_Not_GetProduct() {
                Long id = 1L;
                ProductRequest productRequest = new ProductRequest(id,"Test Product", 10.0, 100);
                when(productRepository.findById(id)).thenReturn(java.util.Optional.empty());
                assertThrows(ResourceNotFoundException.class, () -> productService.findProductById(id));
            }
        }

    }



}