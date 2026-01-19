package com.example.ecommerce.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

/**
 * Payment record stored in the "payments" collection.
 * Status values: PENDING, SUCCESS, FAILED
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "payments")
public class Payment {
    /** Payment record id (MongoDB ObjectId). */
    @Id
    private String id;

    /** Associated order id. */
    private String orderId;

    /** Amount charged for the payment. */
    private Double amount;

    /** Payment status: PENDING, SUCCESS, FAILED. */
    private String status;

    /** External payment gateway id (if available). */
    private String paymentId;

    /** When the payment record was created. */
    private Instant createdAt;
}
