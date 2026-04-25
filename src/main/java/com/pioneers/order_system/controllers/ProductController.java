package com.pioneers.order_system.controllers;

import com.pioneers.order_system.models.dtos.productdtos.ProductRequest;
import com.pioneers.order_system.models.dtos.productdtos.ProductResponse;
import com.pioneers.order_system.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/create")
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest productRequest) {
        productService.createProduct(productRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable long id) {
         ProductResponse productResponse =  productService.getProductById(id);
         return ResponseEntity.ok(productResponse);
    }
    @GetMapping("/low-stock")
    public ResponseEntity<List<ProductResponse>> getProductByLowStock(@RequestParam int value)
    {
        return ResponseEntity.ok(productService.getLowStockProducts(value));
    }


}
