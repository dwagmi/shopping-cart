package com.example.demo.model.exception;

public class QuantityUnavailableException extends Exception {
    public QuantityUnavailableException(String errorMessage) {
        super(errorMessage);
    }
}
