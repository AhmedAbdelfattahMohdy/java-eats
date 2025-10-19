package com.food_delivery.javaeats.exception.custom;

public class UnauthorizedCartAccessException extends RuntimeException{
    public UnauthorizedCartAccessException(String message) {
        super(message);
    }

    public UnauthorizedCartAccessException() {
        super("Unauthorized: This cart does not belong to you");
    }
}
