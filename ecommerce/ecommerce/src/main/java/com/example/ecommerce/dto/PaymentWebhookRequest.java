package com.example.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO representing an incoming payment webhook payload from a payment gateway.
 * Contains minimal fields to correlate payment events to orders.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentWebhookRequest {
    /** Associated order id for this payment event. */
    private String orderId;

    /** External payment gateway id (transaction id). */
    private String paymentId;

    /** Payment outcome: expected values e.g. "SUCCESS" or "FAILED". */
    private String status;
}
