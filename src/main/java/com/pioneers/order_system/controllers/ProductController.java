package com.pioneers.order_system.controllers;

import com.pioneers.order_system.dtos.productdtos.ProductRequest;
import com.pioneers.order_system.dtos.productdtos.ProductResponse;
import com.pioneers.order_system.services.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping("/create")
    public ResponseEntity<ProductResponse> createProductApi(@Valid @RequestBody ProductRequest productRequest) {
        productService.addProduct(productRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> findProductByIdApi(@PathVariable long id) {
         ProductResponse productResponse =  productService.findProductById(id);
         return ResponseEntity.ok(productResponse);
    }
    @GetMapping("/low-stock")
    public ResponseEntity<List<ProductResponse>> getProductByLowStock(@RequestParam int value)
    {
        return ResponseEntity.ok(productService.getLowStockProducts(value));
    }


}
