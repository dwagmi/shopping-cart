package com.example.demo.model.exception;

public class CartNotFoundException extends Exception {
    public CartNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
