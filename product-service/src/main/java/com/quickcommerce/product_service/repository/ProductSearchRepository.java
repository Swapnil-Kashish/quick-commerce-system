package com.quickcommerce.product_service.repository;

import com.quickcommerce.product_service.document.ProductDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ProductSearchRepository
        extends ElasticsearchRepository<ProductDocument, Long> {

    List<ProductDocument> findByNameContainingIgnoreCase(
            String keyword
    );

    List<ProductDocument> findByCategoryIgnoreCase(String category);
}