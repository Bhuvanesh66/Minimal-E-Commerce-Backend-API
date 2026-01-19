package com.example.ecommerce.service;

import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service to manage product lifecycle: create and list products.
 */
@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    /**
     * Persist a new Product.
     *
     * @param product product to create
     * @return saved Product
     */
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    /**
     * Retrieve all products available in the catalog.
     *
     * @return list of products
     */
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    /**
     * Search products by name (partial, case-insensitive).
     *
     * @param q search query
     * @return list of matching products
     */
    public List<Product> searchProducts(String q) {
        return productRepository.findByNameContainingIgnoreCase(q == null ? "" : q);
    }
}
