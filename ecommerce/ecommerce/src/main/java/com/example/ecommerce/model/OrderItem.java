package com.example.ecommerce.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a single item inside an Order.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    /** Product identifier. */
    private String productId;

    /** Human-readable product name. */
    private String productName;

    /** Quantity ordered. */
    private Integer quantity;

    /** Unit price for the product at the time of order. */
    private Double price;
}
