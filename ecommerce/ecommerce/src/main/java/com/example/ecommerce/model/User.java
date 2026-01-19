package com.example.ecommerce.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * User document stored in the "users" collection.
 * Represents an application user with basic identity and role information.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class User {
    /** MongoDB document id. */
    @Id
    private String id;

    /** Username chosen by the user. */
    private String username;

    /** Contact email for the user. */
    private String email;

    /** Role assigned to the user (e.g., "USER", "ADMIN"). */
    private String role;
}
