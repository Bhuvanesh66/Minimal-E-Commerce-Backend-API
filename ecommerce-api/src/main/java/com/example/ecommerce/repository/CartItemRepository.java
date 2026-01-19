package com.example.ecommerce.repository;

import com.example.ecommerce.model.CartItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface CartItemRepository extends MongoRepository<CartItem, String> {
    List<CartItem> findByUserId(String userId);
    void deleteByUserId(String userId);
    CartItem findByUserIdAndProductId(String userId, String productId);
}
