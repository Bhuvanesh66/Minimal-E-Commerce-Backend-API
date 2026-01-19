package com.example.ecommerce.repository;

import com.example.ecommerce.model.Payment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for Payment records.
 */
@Repository
public interface PaymentRepository extends MongoRepository<Payment, String> {
    /**
     * Find a payment record by its associated order id.
     */
    Optional<Payment> findByOrderId(String orderId);
}
