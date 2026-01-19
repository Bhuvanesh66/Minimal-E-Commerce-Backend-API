package com.example.ecommerce.service;

import com.example.ecommerce.dto.CreateOrderRequest;
import com.example.ecommerce.model.*;
import com.example.ecommerce.repository.CartRepository;
import com.example.ecommerce.repository.OrderRepository;
import com.example.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Service that handles order creation and retrieval.
 * The createOrder operation is transactional to ensure consistency between
 * order persistence and cart clearing.
 */
@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartService cartService; // To reuse clearCart

    /**
     * Create an order from the user's current cart.
     * Steps:
     * 1) Read cart items, 2) snapshot product details into order items,
     * 3) compute total and save order, 4) clear the cart.
     *
     * Annotated with @Transactional so failures roll back the entire operation.
     *
     * @param request create order request containing userId
     * @return saved Order
     */
    @Transactional // Ensures if one part fails, everything rolls back
    public Order createOrder(CreateOrderRequest request) {
        // 1. Get cart items
        List<CartItem> cartItems = cartRepository.findByUserId(request.getUserId());

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        List<OrderItem> orderItems = new ArrayList<>();
        double totalAmount = 0.0;

        // 2. Process each item
        for (CartItem cartItem : cartItems) {
            Product product = productRepository.findById(cartItem.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            // Calculate item total
            double itemTotal = product.getPrice() * cartItem.getQuantity();
            totalAmount += itemTotal;

            // Create OrderItem snapshot
            OrderItem orderItem = new OrderItem(
                    product.getId(),
                    product.getName(),
                    cartItem.getQuantity(),
                    product.getPrice()
            );
            orderItems.add(orderItem);
        }

        // 3. Create Order
        Order order = new Order();
        order.setUserId(request.getUserId());
        order.setTotalAmount(totalAmount);
        order.setStatus("CREATED");
        order.setCreatedAt(Instant.now());
        order.setItems(orderItems);

        Order savedOrder = orderRepository.save(order);

        // 4. Clear Cart
        cartService.clearCart(request.getUserId());

        return savedOrder;
    }

    /**
     * Retrieve an order by id.
     *
     * @param orderId id of the order
     * @return the found Order
     */
    public Order getOrder(String orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    /**
     * Retrieve all orders for a user.
     *
     * @param userId id of the user
     * @return list of orders
     */
    public List<Order> getOrdersByUser(String userId) {
        return orderRepository.findByUserId(userId);
    }

    /**
     * Cancel an order if it's not PAID. Restores stock for each OrderItem and
     * updates the order status to CANCELLED. Operation is transactional.
     *
     * @param orderId id of the order to cancel
     * @return updated Order
     * @throws IllegalStateException if order is already PAID or not found
     */
    @Transactional
    public Order cancelOrder(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalStateException("Order not found: " + orderId));

        String status = order.getStatus();
        if ("PAID".equalsIgnoreCase(status)) {
            throw new IllegalStateException("Cannot cancel a paid order");
        }
        if ("CANCELLED".equalsIgnoreCase(status)) {
            return order;
        }

        // restore stock for each item
        if (order.getItems() != null) {
            for (OrderItem item : order.getItems()) {
                productRepository.findById(item.getProductId()).ifPresent(p -> {
                    Integer current = p.getStock() == null ? 0 : p.getStock();
                    p.setStock(current + (item.getQuantity() == null ? 0 : item.getQuantity()));
                    productRepository.save(p);
                });
            }
        }

        order.setStatus("CANCELLED");
        return orderRepository.save(order);
    }
}
