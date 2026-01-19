package com.example.ecommerce.controller;

import com.example.ecommerce.model.Payment;
import com.example.ecommerce.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin("*")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createPayment(@RequestBody Map<String, Object> request) {
        try {
            String orderId = (String) request.get("orderId");
            Double amount = ((Number) request.get("amount")).doubleValue();

            Payment payment = paymentService.createPayment(orderId, amount);

            // Simulate webhook callback after 3 seconds
            new Thread(() -> {
                try {
                    Thread.sleep(3000);
                    paymentService.handlePaymentWebhook(orderId, "SUCCESS");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }).start();

            return ResponseEntity.ok(payment);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/webhook")
    public ResponseEntity<?> paymentWebhook(@RequestBody Map<String, Object> request) {
        try {
            String orderId = (String) request.get("orderId");
            String status = (String) request.get("status");

            paymentService.handlePaymentWebhook(orderId, status);
            return ResponseEntity.ok(Map.of("message", "Webhook processed"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}
