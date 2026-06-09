package com.quickcommerce.product_service.service;

import com.quickcommerce.product_service.document.ProductDocument;
import com.quickcommerce.product_service.entity.Product;
import com.quickcommerce.product_service.repository.ProductRepository;
import com.quickcommerce.product_service.repository.ProductSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;

    private final ProductSearchRepository productSearchRepository;

//    public Product create(Product product) {
//        product.setCreatedAt(LocalDateTime.now());
//        product.setActive(true);
//        Product savedProduct = repository.save(product);
//        productSearchRepository.save(toDocument(savedProduct));
//        return savedProduct;
//    }

    public Product create(Product product) {

        product.setCreatedAt(LocalDateTime.now());
        product.setActive(true);
        Product savedProduct = repository.save(product);
        System.out.println("BEFORE ES SAVE");
        productSearchRepository.save(toDocument(savedProduct));
        System.out.println("AFTER ES SAVE");
        return savedProduct;
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
        Product updatedProduct = repository.save(product);
        productSearchRepository.save(toDocument(updatedProduct));
        return updatedProduct;
    }

    @CacheEvict(value = "products", key = "#id")
    public void delete(Long id) {
        repository.deleteById(id);
        productSearchRepository.deleteById(id);
    }

    private ProductDocument toDocument(
            Product product
    ) {

        return ProductDocument.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .category(product.getCategory())
                .imageUrl(product.getImageUrl())
                .build();
    }

    public List<ProductDocument> searchProducts(
            String keyword
    ) {
        System.out.println("SEARCH KEYWORD : " + keyword);
        return productSearchRepository
                .findByNameContainingIgnoreCase(keyword);
    }

    public List<ProductDocument> searchByCategory(
            String category
    ) {
        return productSearchRepository
                .findByCategoryIgnoreCase(
                        category
                );
    }

    public Page<ProductDocument> getAllProducts(int page, int size) {
        return productSearchRepository.findAll(PageRequest.of(page, size));
    }

    public List<ProductDocument> getProductsSortedByPrice() {

        return StreamSupport.stream(productSearchRepository.findAll().spliterator(), false)
                        .sorted(Comparator.comparing(ProductDocument::getPrice))
                        .toList();
    }

}
