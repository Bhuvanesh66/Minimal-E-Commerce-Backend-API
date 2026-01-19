package com.example.ecommerce.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

/**
 * Represents a customer's order stored in the "orders" collection.
 * Status values: CREATED, PAID, FAILED, CANCELLED
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "orders")
public class Order {
    /** Order unique identifier (MongoDB ObjectId). */
    @Id
    private String id;

    /** Identifier of the user who placed the order. */
    private String userId;

    /** Total monetary amount for the order. */
    private Double totalAmount;

    /** Current status of the order (CREATED/PAID/FAILED/CANCELLED). */
    private String status;

    /** Timestamp when the order was created. */
    private Instant createdAt;

    /** List of items included in the order. */
    private List<OrderItem> items;
}
