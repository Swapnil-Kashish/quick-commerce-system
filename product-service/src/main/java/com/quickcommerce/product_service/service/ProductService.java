package com.quickcommerce.product_service.service;

import com.quickcommerce.product_service.entity.Product;
import com.quickcommerce.product_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;

    public Product create(Product product) {
        product.setCreatedAt(LocalDateTime.now());
        product.setActive(true);
        return repository.save(product);
    }

    public List<Product> getAll() {
        return repository.findAll();
    }

    @Cacheable(value = "products", key = "#id")
    public Product getById(Long id) {
        System.out.println("🔥 DB HIT FOR PRODUCT: " + id);
        return repository.findById(id).orElseThrow();
    }

    @CachePut(value = "products", key = "#id")
    public Product update(Long id, Product request) {

        Product product = repository.findById(id).orElseThrow();

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setCategory(request.getCategory());
        product.setImageUrl(request.getImageUrl());

        return repository.save(product);
    }

    @CacheEvict(value = "products", key = "#id")
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
