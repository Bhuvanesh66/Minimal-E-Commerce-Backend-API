package com.example.ecommerce.service;

import com.example.ecommerce.dto.AddToCartRequest;
import com.example.ecommerce.dto.CartItemResponse;
import com.example.ecommerce.model.CartItem;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.CartRepository;
import com.example.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service responsible for cart operations:
 * - adding items to a user's cart
 * - retrieving a user's cart with product details
 * - clearing a user's cart
 */
@Service
public class CartService {

    /** Repository for cart items. */
    @Autowired
    private CartRepository cartRepository;

    /** Repository to fetch product details. */
    @Autowired
    private ProductRepository productRepository;

    /**
     * Add an item to the user's cart. If the product already exists in the cart,
     * increases the quantity; otherwise creates a new cart item.
     *
     * @param request contains userId, productId and quantity
     * @return saved CartItem
     */
    public CartItem addToCart(AddToCartRequest request) {
        // Validate product exists
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Check if item already in cart
        Optional<CartItem> existingItem = cartRepository.findByUserIdAndProductId(request.getUserId(), request.getProductId());

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + request.getQuantity());
            return cartRepository.save(item);
        } else {
            CartItem newItem = new CartItem();
            newItem.setUserId(request.getUserId());
            newItem.setProductId(request.getProductId());
            newItem.setQuantity(request.getQuantity());
            return cartRepository.save(newItem);
        }
    }

    /**
     * Fetch the cart for a user and map to CartItemResponse containing product details.
     *
     * @param userId id of the user
     * @return list of CartItemResponse
     */
    public List<CartItemResponse> getUserCart(String userId) {
        List<CartItem> items = cartRepository.findByUserId(userId);
        List<CartItemResponse> response = new ArrayList<>();

        for (CartItem item : items) {
            Product product = productRepository.findById(item.getProductId()).orElse(null);
            if (product != null) {
                response.add(new CartItemResponse(item.getId(), product, item.getQuantity()));
            }
        }
        return response;
    }

    /**
     * Remove all items from the specified user's cart.
     *
     * @param userId id of the user
     */
    public void clearCart(String userId) {
        cartRepository.deleteByUserId(userId);
    }
}
