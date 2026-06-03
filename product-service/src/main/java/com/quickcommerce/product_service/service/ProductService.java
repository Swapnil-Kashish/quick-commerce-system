package com.quickcommerce.product_service.service;

import com.quickcommerce.product_service.entity.Product;
import com.quickcommerce.product_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
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

    public Product getById(Long id) {
        return repository.findById(id)
                .orElseThrow();
    }

    public Product update(Long id, Product request) {

        Product product = repository.findById(id)
                .orElseThrow();

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setCategory(request.getCategory());
        product.setImageUrl(request.getImageUrl());

        return repository.save(product);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
