package com.example.ecommerce.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Represents an item in a user's shopping cart stored in the "cart_items" collection.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "cart_items")
public class CartItem {
    /** Unique identifier for the cart item (MongoDB ObjectId). */
    @Id
    private String id;

    /** Identifier of the user who owns this cart item. */
    private String userId;

    /** Identifier of the product added to the cart. */
    private String productId;

    /** Quantity of the product in the cart. */
    private Integer quantity;
}
