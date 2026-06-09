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


import org.springframework.security.access.prepost.PreAuthorize; // الأنوتيشن الحتمية
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products") // 👈 تعديل: أضفنا /api لتصبح متناسقة مع السيستم
public class ProductController {

    private final ProductService productService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')") // 👈 قفل: الأدمن فقط من يضيف منتج
    public ResponseEntity<Void> createProductApi(@Valid @RequestBody ProductRequest productRequest) {
        productService.addProduct(productRequest);
        return new ResponseEntity<>(HttpStatus.CREATED); // الـ Void متناسق هنا طالما لا نرجع بادي
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')") // 👈 تعديل أمني: الأدمن يشوفه والزبون يشوفه عشان يشتريه
    public ResponseEntity<ProductResponse> findProductByIdApi(@PathVariable long id) {
        ProductResponse productResponse = productService.findProductById(id);
        return ResponseEntity.ok(productResponse);
    }

    @GetMapping("/low-stock")
    @PreAuthorize("hasRole('ADMIN')") // 👈 قفل: صاحب المطعم/الأدمن فقط من يراقب المخزون المنخفض
    public ResponseEntity<List<ProductResponse>> getProductByLowStock(@RequestParam int value) {
        return ResponseEntity.ok(productService.getLowStockProducts(value));
    }
}