package com.example.ecommerce.dto;

import lombok.Data;

/**
 * Request DTO used to create an order for a user.
 */
@Data
public class CreateOrderRequest {
    /** Id of the user placing the order. */
    private String userId;
}
