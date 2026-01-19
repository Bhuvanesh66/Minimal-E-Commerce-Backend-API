package com.example.ecommerce.repository;

import com.example.ecommerce.model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for managing Order documents in MongoDB.
 * Provides basic CRUD via MongoRepository and custom queries used by the service layer.
 */
@Repository
public interface OrderRepository extends MongoRepository<Order, String> {
    /**
     * Retrieve all orders placed by a specific user.
     *
     * @param userId id of the user
     * @return list of orders belonging to the user
     */
    List<Order> findByUserId(String userId);
}
