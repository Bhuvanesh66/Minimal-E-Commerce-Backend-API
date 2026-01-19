package com.example.ecommerce.repository;

import com.example.ecommerce.model.CartItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for accessing cart items stored in MongoDB.
 */
@Repository
public interface CartRepository extends MongoRepository<CartItem, String> {
    /**
     * Find all cart items for a given user.
     */
    List<CartItem> findByUserId(String userId);

    /**
     * Find a specific cart item by user and product.
     */
    Optional<CartItem> findByUserIdAndProductId(String userId, String productId);

    /**
     * Delete all cart items for a user.
     */
    void deleteByUserId(String userId);
}
