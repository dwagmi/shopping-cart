package com.example.demo.model.exception;

public class ProductNotFoundException extends Exception {
    public ProductNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
