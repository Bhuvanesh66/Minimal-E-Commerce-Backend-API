package com.example.ecommerce.service;

import com.example.ecommerce.model.Payment;
import com.example.ecommerce.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.UUID;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderService orderService;

    public PaymentService(PaymentRepository paymentRepository, OrderService orderService) {
        this.paymentRepository = paymentRepository;
        this.orderService = orderService;
    }

    public Payment createPayment(String orderId, Double amount) {
        Payment payment = new Payment();
        payment.setId(UUID.randomUUID().toString());
        payment.setOrderId(orderId);
        payment.setAmount(amount);
        payment.setStatus("PENDING");
        payment.setPaymentId("pay_" + UUID.randomUUID().toString().substring(0, 8));
        payment.setCreatedAt(Instant.now());

        return paymentRepository.save(payment);
    }

    public void handlePaymentWebhook(String orderId, String status) {
        Payment payment = paymentRepository.findByOrderId(orderId);
        if (payment != null) {
            payment.setStatus(status);
            paymentRepository.save(payment);

            // Update order status
            if ("SUCCESS".equals(status)) {
                orderService.updateOrderStatus(orderId, "PAID");
            } else {
                orderService.updateOrderStatus(orderId, "FAILED");
            }
        }
    }

    public Payment getPaymentByOrderId(String orderId) {
        return paymentRepository.findByOrderId(orderId);
    }
}
