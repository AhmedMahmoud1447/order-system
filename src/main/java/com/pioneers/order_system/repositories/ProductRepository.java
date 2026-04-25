package com.pioneers.order_system.repositories;

import com.pioneers.order_system.models.entities.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Slf4j
@Repository
public class ProductRepository {

    private List<Product> products = new ArrayList<>();

    public Product save(Product product) {
        log.info("Saving product: {}", product);
        products.add(product);
        return product;
    }

    public Optional<Product> findById(long id) {
        log.info("Finding product by id: {}", id);
       return products.stream()
                .filter(product -> product.getId().equals(id))
                .findFirst();
    }

    public List<Product> findAll() {
        log.info("Finding all products");
        return products;
    }

}
