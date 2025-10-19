package com.food_delivery.javaeats.exception.custom;

import com.food_delivery.javaeats.enums.CartStatus;

public class InvalidCartStatusException extends RuntimeException {
    public InvalidCartStatusException(String message) {
        super(message);
    }

    public InvalidCartStatusException(CartStatus status) {
        super("Cannot modify cart with status: " + status);
    }
}
