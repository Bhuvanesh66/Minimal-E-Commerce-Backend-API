package com.example.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the E-commerce Spring Boot application.
 * Run this class to start the embedded server and initialize the application context.
 */
@SpringBootApplication
public class EcommerceApplication {

    /**
     * Main method used by Spring Boot to launch the application.
     *
     * @param args command line arguments
     */
	public static void main(String[] args) {
		SpringApplication.run(EcommerceApplication.class, args);
	}

}
