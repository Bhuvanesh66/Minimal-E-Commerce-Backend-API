package com.example.ecommerce.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Product document stored in the "products" collection.
 * Contains basic product details and inventory information.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "products")
public class Product {
    /** MongoDB document id. */
    @Id
    private String id;

    /** Product name displayed to customers. */
    private String name;

    /** Short product description. */
    private String description;

    /** Unit price for the product. */
    private Double price;

    /** Available stock quantity. */
    private Integer stock;
}
