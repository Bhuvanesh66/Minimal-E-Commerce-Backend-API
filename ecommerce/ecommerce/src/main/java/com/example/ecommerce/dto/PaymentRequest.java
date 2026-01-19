package com.example.ecommerce.dto;

import lombok.Data;

/**
 * DTO used to request a payment initiation for a specific order.
 */
@Data
public class PaymentRequest {
    /** Id of the order to be paid. */
    private String orderId;

    /** Amount to charge for the payment. */
    private Double amount;
}
