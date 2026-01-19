package com.example.ecommerce.dto;

import lombok.Data;

/**
 * Request DTO to add a product to a user's cart.
 */
@Data
public class AddToCartRequest {
    /** Id of the user adding the item. */
    private String userId;

    /** Id of the product to add. */
    private String productId;

    /** Quantity to add for the product. */
    private Integer quantity;
}
