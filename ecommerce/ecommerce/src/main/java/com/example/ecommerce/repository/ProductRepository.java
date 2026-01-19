package com.example.ecommerce.repository;

import com.example.ecommerce.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for managing Product documents.
 */
@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
    /**
     * Search products by name (case-insensitive, partial match).
     *
     * @param name fragment to search for in product names
     * @return list of matching products
     */
    List<Product> findByNameContainingIgnoreCase(String name);
}
