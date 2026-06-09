package com.quickcommerce.product_service.controller;

import com.quickcommerce.product_service.document.ProductDocument;
import com.quickcommerce.product_service.entity.Product;
import com.quickcommerce.product_service.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public Product create(
            @RequestBody Product product) {

        return productService.create(product);
    }

    @GetMapping
    public List<Product> getAll() {

        return productService.getAll();
    }

    @GetMapping("/{id}")
    public Product getById(
            @PathVariable Long id) {

        return productService.getById(id);
    }

    @PutMapping("/{id}")
    public Product update(
            @PathVariable Long id,
            @RequestBody Product product) {

        return productService.update(id, product);
    }

    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable Long id) {

        productService.delete(id);
    }

    @GetMapping("/search")
    public List<ProductDocument> searchProducts(
            @RequestParam String keyword
    ) {
        return productService.searchProducts(keyword);
    }

    @GetMapping("/category/{category}")
    public List<ProductDocument> searchByCategory(
            @PathVariable String category
    ) {
        return productService.searchByCategory(
                category
        );
    }

    @GetMapping("/page")
    public Page<ProductDocument> getProducts(
            @RequestParam int page,
            @RequestParam int size
    ) {

        return productService.getAllProducts(
                page,
                size
        );
    }

    @GetMapping("/sort/price")
    public List<ProductDocument> sortByPrice() {

        return productService
                .getProductsSortedByPrice();
    }

}
