package com.example.ecommerce.controller;

import com.example.ecommerce.dto.AddToCartRequest;
import com.example.ecommerce.dto.CartItemResponse;
import com.example.ecommerce.model.CartItem;
import com.example.ecommerce.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller that exposes cart-related operations:
 * - Add an item to a user's cart
 * - Retrieve a user's cart
 * - Clear a user's cart
 *
 * Base path: /api/cart
 */
@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    /**
     * Add an item to the specified user's cart.
     *
     * @param request request containing userId, productId and quantity
     * @return the created CartItem entity
     */
    @PostMapping("/add")
    public ResponseEntity<CartItem> addToCart(@RequestBody AddToCartRequest request) {
        return ResponseEntity.ok(cartService.addToCart(request));
    }

    /**
     * Retrieve the list of cart items for a given user.
     *
     * @param userId id of the user
     * @return list of CartItemResponse DTOs representing the user's cart
     */
    @GetMapping("/{userId}")
    public ResponseEntity<List<CartItemResponse>> getCart(@PathVariable String userId) {
        return ResponseEntity.ok(cartService.getUserCart(userId));
    }

    /**
     * Remove all items from the specified user's cart.
     *
     * @param userId id of the user
     * @return confirmation message
     */
    @DeleteMapping("/{userId}/clear")
    public ResponseEntity<String> clearCart(@PathVariable String userId) {
        cartService.clearCart(userId);
        return ResponseEntity.ok("Cart cleared successfully");
    }
}
