package com.food_delivery.javaeats.exception.custom;

public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException(String message) {
        super(message);
    }

    public CustomerNotFoundException(Long userId) {
        super("Customer not found for user ID: " + userId);
    }
}
